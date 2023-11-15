package kz.agrobirzha.app.Fragments;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.google.android.gms.maps.MapView;

import kz.agrobirzha.app.R;

public class AboutFragment extends Fragment {

    ScrollView scrollView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_about, container, false);
        scrollView = (ScrollView) getActivity().findViewById(R.id.scrollView);
        scrollView.setBackgroundColor(Color.WHITE);


        return v;
    }

}
