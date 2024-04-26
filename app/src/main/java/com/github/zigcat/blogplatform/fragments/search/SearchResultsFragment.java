package com.github.zigcat.blogplatform.fragments.search;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.zigcat.blogplatform.R;
import com.github.zigcat.blogplatform.adapters.CommentAdapter;
import com.github.zigcat.blogplatform.adapters.UserAdapter;
import com.github.zigcat.blogplatform.api.UserOkHttpHelper;
import com.github.zigcat.blogplatform.models.User;

import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SearchResultsFragment extends Fragment {
    private String key;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_results, container, false);
        UserOkHttpHelper userOkHttpHelper = new UserOkHttpHelper();
        TextView searchResTitle = rootView.findViewById(R.id.searchres_title);
        userOkHttpHelper.search(getKey(), new UserOkHttpHelper.CallbackUsers() {
            @Override
            public void onSuccess(List<User> response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(response.isEmpty()){
                            searchResTitle.setText(R.string.no_items);
                        } else {
                            String message = "Results for "+getKey();
                            searchResTitle.setText(message);
                            RecyclerView recyclerView = getActivity().findViewById(R.id.user_recyclerview);
                            Collections.reverse(response);
                            UserAdapter userAdapter = new UserAdapter(response);
                            recyclerView.setAdapter(userAdapter);
                            userAdapter.notifyDataSetChanged();
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                        }
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = Toast.makeText(getContext(), R.string.server_error, Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
            }
        });

        return rootView;
    }
}