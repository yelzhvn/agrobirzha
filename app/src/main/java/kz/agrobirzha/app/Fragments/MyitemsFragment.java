package kz.agrobirzha.app.Fragments;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import kz.agrobirzha.app.Classes.MyItemsProduct;
import kz.agrobirzha.app.Classes.MyItemsProductsAdapter;
import kz.agrobirzha.app.Classes.Product;
import kz.agrobirzha.app.Classes.ProductsAdapter;
import kz.agrobirzha.app.Helpers.SQLiteHandler;
import kz.agrobirzha.app.R;

public class MyitemsFragment extends Fragment {

    private static final String URL_PRODUCTS = "http://agrobirzha.cf/myitems.php";
    ScrollView scrollView;
    private SQLiteHandler db;
    List<MyItemsProduct> productList;
    String uid;
    RecyclerView recyclerView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        scrollView = (ScrollView) getActivity().findViewById(R.id.scrollView);
        scrollView.setBackgroundColor(Color.WHITE);
        recyclerView = rootView.findViewById(R.id.recylcerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        db = new SQLiteHandler(getContext());
        productList = new ArrayList<>();


        loadProducts();
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

                                productList.add(new MyItemsProduct(
                                        product.getInt("id"),
                                        product.getString("title"),
                                        product.getString("shortdesc"),
                                        product.getInt("price"),
                                        product.getString("image")
                                ));
                            }


                            MyItemsProductsAdapter adapter = new MyItemsProductsAdapter(getActivity(), productList);
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
                uid = db.getUserDetails().get("uid");
                params.put("category_id", uid);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}
