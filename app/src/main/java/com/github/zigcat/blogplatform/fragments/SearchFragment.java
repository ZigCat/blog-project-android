package com.github.zigcat.blogplatform.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.github.zigcat.blogplatform.R;
import com.github.zigcat.blogplatform.fragments.search.SearchResultsFragment;

public class SearchFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        EditText key = rootView.findViewById(R.id.search_pole);
        ImageView searchButton = rootView.findViewById(R.id.search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyString = key.getText().toString();
                if(keyString.isEmpty()){
                    key.setHint(R.string.missing_content);
                } else {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.search_fragment_container, new SearchResultsFragment(keyString))
                            .commit();
                }
            }
        });

        return rootView;
    }
}