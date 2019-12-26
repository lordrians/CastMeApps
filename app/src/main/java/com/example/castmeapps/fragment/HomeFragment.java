package com.example.castmeapps.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.castmeapps.R;
import com.example.castmeapps.adapter.PostingAdapter;
import com.example.castmeapps.object.Posting;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private RecyclerView rvHomePost;
    private ArrayList<Posting> listPosting;

    private FirebaseFirestore firestore;

    private PostingAdapter postingAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        listPosting = new ArrayList<>();

        rvHomePost = view.findViewById(R.id.rv_home_post);

        postingAdapter = new PostingAdapter(listPosting);

        rvHomePost.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvHomePost.setAdapter(postingAdapter);

        firestore = FirebaseFirestore.getInstance();
        firestore.collection("Post").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){

                    if (doc.getType() == DocumentChange.Type.ADDED){

                        Posting posting = doc.getDocument().toObject(Posting.class);
                        listPosting.add(posting);

                        postingAdapter.notifyDataSetChanged();

                    }

                }

            }
        });

        // Inflate the layout for this fragment
        return view;
    }

}
