package com.github.zigcat.blogplatform.fragments.userpage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.zigcat.blogplatform.R;
import com.github.zigcat.blogplatform.api.UserOkHttpHelper;
import com.github.zigcat.blogplatform.api.UserRequest;
import com.github.zigcat.blogplatform.models.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserUpdateInfoFragment extends Fragment {
    private int userId;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_user_update_info, container, false);
        UserOkHttpHelper userOkHttpHelper = new UserOkHttpHelper();
        SharedPreferences sharedPref = getActivity().getSharedPreferences("blogplatform", Context.MODE_PRIVATE);

        EditText username = rootView.findViewById(R.id.user_editinfo_username);
        username.setText(getUser().getUsername());
        EditText nickname = rootView.findViewById(R.id.user_editinfo_nickname);
        nickname.setText(getUser().getNickname());
        EditText email = rootView.findViewById(R.id.user_editinfo_email);
        email.setText(getUser().getEmail());

        Button editPwdButton = rootView.findViewById(R.id.editpwd_button);
        Button apply = rootView.findViewById(R.id.editinfo_apply);
        Button cancel = rootView.findViewById(R.id.editinfo_cancel);

        editPwdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.user_fragment_container, new UserUpdatePwdFragment(getUserId(), getUser()))
                        .commit();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.user_fragment_container, new UserInfoFragment(getUserId(), getUser()))
                        .commit();
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int userId = sharedPref.getInt("id", -1);
                String credentials = sharedPref.getString("auth", null);
                if(userId != -1 && credentials != null){
                    UserRequest request = new UserRequest(
                            username.getText().toString(),
                            nickname.getText().toString(),
                            email.getText().toString(),
                            "password",
                            "USER");
                    userOkHttpHelper.updateUserInfo(credentials, userId, request, new UserOkHttpHelper.CallbackUser() {
                        @Override
                        public void onSuccess(User response) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), R.string.success, Toast.LENGTH_LONG);
                                    toast.show();
                                    getActivity().getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.user_fragment_container, new UserInfoFragment(getUserId()))
                                            .commit();
                                }
                            });
                        }

                        @Override
                        public void onFailure(Exception e) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), R.string.server_error, Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            });
                        }
                    });
                } else {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), R.string.error_message, Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
        return rootView;
    }
}