package kz.agrobirzha.app.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import kz.agrobirzha.app.Activities.MainActivity;
import kz.agrobirzha.app.R;
public class HomeFragment extends Fragment {
    ImageButton agroculture, cargo, repair;
    public HomeFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        agroculture = rootView.findViewById(R.id.agroculture);
        cargo = rootView.findViewById(R.id.cargo);
        repair = rootView.findViewById(R.id.repair);

        agroculture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).replaceFragments(1);
            }
        });
        cargo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).replaceFragments(2);
            }
        });
        repair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).replaceFragments(3);
            }
        });
        return rootView;
    }
}
