package kz.agrobirzha.app.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kz.agrobirzha.app.Activities.MainActivity;
import kz.agrobirzha.app.Classes.Product;
import kz.agrobirzha.app.Classes.ProductsAdapter;
import kz.agrobirzha.app.Helpers.SQLiteHandler;
import kz.agrobirzha.app.Helpers.SessionManager;
import kz.agrobirzha.app.R;

public class ListFragment extends Fragment {

    private static final String URL_PRODUCTS = "http://agrobirzha.cf/RecyclerView.php";

    List<Product> productList;
    private SQLiteHandler db;
    String category, uid;
    private SessionManager session;
    RecyclerView recyclerView;
    CheckBox addtoFav;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = rootView.findViewById(R.id.recylcerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        db = new SQLiteHandler(getContext());
        session = new SessionManager(getContext());
        productList = new ArrayList<>();
        addtoFav = (CheckBox) rootView.findViewById(R.id.addtoFav);
        loadProducts();
        category = getArguments().getString("category");
        uid = db.getUserDetails().get("uid");
        return rootView;
    }
    private void loadProducts() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {


                                JSONObject product = array.getJSONObject(i);

                                productList.add(new Product(
                                        product.getInt("id"),
                                        product.getString("unique_id"),
                                        product.getString("title"),
                                        product.getString("shortdesc"),
                                        product.getInt("price"),
                                        product.getString("image")
                                ));
                            }


                            ProductsAdapter adapter = new ProductsAdapter(getActivity(), productList);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("category_id", category);
                if(session.isLoggedIn()) {
                    params.put("uid", uid);
                }
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
    public void addtoFavorite(){

    }
    public void removefromFavorite(){

    }
}
