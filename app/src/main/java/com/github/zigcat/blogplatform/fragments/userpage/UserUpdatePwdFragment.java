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
import com.github.zigcat.blogplatform.models.User;

import okhttp3.Credentials;

public class UserUpdatePwdFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_update_pwd, container, false);
        UserOkHttpHelper userOkHttpHelper = new UserOkHttpHelper();

        Button editInfoButton = rootView.findViewById(R.id.editinfo_button);
        EditText password1 = rootView.findViewById(R.id.user_editpwd1);
        EditText password2 = rootView.findViewById(R.id.user_editpwd2);
        Button apply = rootView.findViewById(R.id.editpwd_apply);
        Button cancel = rootView.findViewById(R.id.editpwd_cancel);

        editInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.user_fragment_container, new UserUpdateInfoFragment()).commit();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.user_fragment_container, new UserInfoFragment()).commit();
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password1.getText().toString().equals(password2.getText().toString())){
                    SharedPreferences sharedPref = getActivity().getSharedPreferences("blogplatform", Context.MODE_PRIVATE);
                    int userId = sharedPref.getInt("id", -1);
                    String credentials = sharedPref.getString("auth", null);
                    String username = sharedPref.getString("username", "");
                    if(userId != -1 && credentials != null){
                        userOkHttpHelper.updatePassword(credentials, userId, password1.getText().toString(), new UserOkHttpHelper.CallbackUser() {
                            @Override
                            public void onSuccess(User response) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), R.string.success, Toast.LENGTH_LONG);
                                        toast.show();
                                        String newCredentials = Credentials.basic(username, password1.getText().toString());
                                        sharedPref.edit().putString("auth", newCredentials).apply();
                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.user_fragment_container, new UserInfoFragment()).commit();
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
                } else {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), R.string.editpwd_nomatch, Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        return rootView;
    }
}