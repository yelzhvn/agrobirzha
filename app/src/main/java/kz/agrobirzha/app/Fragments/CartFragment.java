package kz.agrobirzha.app.Fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kz.agrobirzha.app.Activities.MainActivity;
import kz.agrobirzha.app.Classes.CartItemsProduct;
import kz.agrobirzha.app.Classes.CartItemsProductsAdapter;
import kz.agrobirzha.app.Helpers.SQLiteHandler;
import kz.agrobirzha.app.R;

public class CartFragment extends Fragment {
    TextView summ;
    List<CartItemsProduct> productList;
    private CartItemsProductsAdapter adapter;
    private SQLiteHandler db;
    RecyclerView recyclerView;
    Button orderbtn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);
        db = new SQLiteHandler(getContext());

        productList = new ArrayList<>();
        recyclerView = rootView.findViewById(R.id.cartitemsrecylcerView);
        productList.addAll(db.allCartProducts());
        adapter = new CartItemsProductsAdapter(getContext(), productList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        summ = rootView.findViewById(R.id.summdetail);
        int totalPrice = 0;
        for (int i = 0; i<productList.size(); i++)
        {
            totalPrice += productList.get(i).getPrice();
        }
        summ.setText(String.valueOf(totalPrice) + "тг.");
        orderbtn = rootView.findViewById(R.id.orderbtn);
        orderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Ваш заказ был оформлен, ждите подверждения", Toast.LENGTH_SHORT).show();
                Intent toMain = new Intent(getContext(), MainActivity.class);
                startActivity(toMain);
            }
        });
        return rootView;
    }
}
