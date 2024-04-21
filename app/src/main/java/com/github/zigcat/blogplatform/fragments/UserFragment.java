package com.github.zigcat.blogplatform.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.zigcat.blogplatform.R;
import com.github.zigcat.blogplatform.fragments.userpage.UserInfoFragment;

public class UserFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.user_fragment_container, new UserInfoFragment()).commit();
        return rootView;
    }
}