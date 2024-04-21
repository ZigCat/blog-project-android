package com.github.zigcat.blogplatform.fragments.userpage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.zigcat.blogplatform.R;
import com.github.zigcat.blogplatform.api.UserOkHttpHelper;
import com.github.zigcat.blogplatform.models.User;

import lombok.Getter;
import lombok.Setter;

public class UserInfoFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_info, container, false);
        TextView username = rootView.findViewById(R.id.user_username);
        TextView nickname = rootView.findViewById(R.id.user_nickname);
        TextView regdate = rootView.findViewById(R.id.user_regdate);
        ImageView editButton = rootView.findViewById(R.id.user_editbutton);
        Button deleteButton = rootView.findViewById(R.id.user_deletebutton);

        UserOkHttpHelper userOkHttpHelper = new UserOkHttpHelper();
        SharedPreferences sharedPref = getActivity().getSharedPreferences("blogplatform", Context.MODE_PRIVATE);
        int loggedUserId = sharedPref.getInt("id", -1);
        int pageUserId = sharedPref.getInt("page_user_id", -1);
        if(pageUserId != -1){
            userOkHttpHelper.getUserById(pageUserId, new UserOkHttpHelper.CallbackUser() {
                @Override
                public void onSuccess(User response) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            username.setText(response.getUsername());
                            nickname.setText(response.getNickname());
                            String regdateText = "Signed up "+response.getCreationDate();
                            regdate.setText(regdateText);
                        }
                    });
                }

                @Override
                public void onFailure(Exception e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            nickname.setText(R.string.server_error);
                        }
                    });
                }
            });
        } else {
            nickname.setText(R.string.server_error);
        }
        if(loggedUserId == pageUserId){
            deleteButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.VISIBLE);
        }

        return rootView;
    }
}