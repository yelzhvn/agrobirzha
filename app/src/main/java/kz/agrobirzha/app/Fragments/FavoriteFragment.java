package kz.agrobirzha.app.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kz.agrobirzha.app.Classes.FavItemsProduct;
import kz.agrobirzha.app.Classes.FavItemsProductsAdapter;
import kz.agrobirzha.app.Helpers.SQLiteHandler;
import kz.agrobirzha.app.R;

public class FavoriteFragment extends Fragment {

    private static final String URL_PRODUCTS = "http://agrobirzha.cf/RecyclerView.php";

    List<FavItemsProduct> productList;
    private FavItemsProductsAdapter adapter;
    private SQLiteHandler db;
    RecyclerView recyclerView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);

        db = new SQLiteHandler(getContext());

        productList = new ArrayList<>();
        recyclerView = rootView.findViewById(R.id.favRecylcerView);
        productList.addAll(db.allFavProducts());
        adapter = new FavItemsProductsAdapter(getContext(), productList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        return rootView;
    }

}
