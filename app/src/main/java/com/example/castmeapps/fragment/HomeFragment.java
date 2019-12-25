package com.example.castmeapps.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.castmeapps.R;
import com.example.castmeapps.object.Posting;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private RecyclerView rvHomePost;
    private ArrayList<Posting> listPosting;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        listPosting = new ArrayList<>();

        rvHomePost = getActivity().findViewById(R.id.rv_home_post);

        // Inflate the layout for this fragment
        return view;
    }

}
