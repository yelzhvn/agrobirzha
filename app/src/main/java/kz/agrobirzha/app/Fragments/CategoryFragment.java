package kz.agrobirzha.app.Fragments;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import kz.agrobirzha.app.R;

public class CategoryFragment extends Fragment {
    ImageButton agroculture, cargo, repair;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_category, container, false);

        agroculture = rootView.findViewById(R.id.agroculture);
        cargo = rootView.findViewById(R.id.cargo);
        repair = rootView.findViewById(R.id.repair);

        agroculture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return rootView;
    }
}
