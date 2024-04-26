package com.github.zigcat.blogplatform.fragments.userpage;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.zigcat.blogplatform.R;
import com.github.zigcat.blogplatform.RegistrationPage;
import com.github.zigcat.blogplatform.api.UserOkHttpHelper;
import com.github.zigcat.blogplatform.models.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserInfoFragment extends Fragment {
    private int userId;
    private User user;

    public UserInfoFragment(int userId) {
        this.userId = userId;
        this.user = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_info, container, false);
        TextView username = rootView.findViewById(R.id.user_username);
        TextView nickname = rootView.findViewById(R.id.user_nickname);
        TextView regdate = rootView.findViewById(R.id.user_regdate);
        ImageView editButton = rootView.findViewById(R.id.user_editbutton);
        Button deleteButton = rootView.findViewById(R.id.user_deletebutton);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("blogplatform", Context.MODE_PRIVATE);
        int loggedUserId = sharedPref.getInt("id", -1);
        UserOkHttpHelper userOkHttpHelper = new UserOkHttpHelper();
        if(getUser() == null){
            userOkHttpHelper.getUserById(userId, new UserOkHttpHelper.CallbackUser() {
                @Override
                public void onSuccess(User response) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setUser(response);
                            username.setText(response.getUsername());
                            nickname.setText(response.getNickname());
                            regdate.setText(response.getCreationDate());
                        }
                    });
                }

                @Override
                public void onFailure(Exception e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LinearLayout userInfo = rootView.findViewById(R.id.user_info_fragment);
                            userInfo.setVisibility(View.GONE);
                            TextView error = getActivity().findViewById(R.id.user_fragment_error);
                            error.setVisibility(View.VISIBLE);
                        }
                    });
                }
            });
        } else {
            username.setText(getUser().getUsername());
            nickname.setText(getUser().getNickname());
            regdate.setText(getUser().getCreationDate());
        }

        Dialog deleteDialog = new Dialog(requireContext());
        deleteDialog.setContentView(R.layout.dialog_user_delete);
        deleteDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        deleteDialog.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.rounded_border));
        deleteDialog.setCancelable(false);

        Button dialogConfirm = deleteDialog.findViewById(R.id.user_delete_dialog_confirm);
        Button dialogCancel = deleteDialog.findViewById(R.id.user_delete_dialog_cancel);

        if(loggedUserId == userId){
            deleteButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.VISIBLE);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteDialog.show();
                }
            });

            dialogCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteDialog.dismiss();
                }
            });

            dialogConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String credentials = sharedPref.getString("auth", "");
                    userOkHttpHelper.deleteUser(getUserId(), credentials, new UserOkHttpHelper.CallbackResponseString() {
                        @Override
                        public void onSuccess(String response) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast toast = Toast.makeText(getContext(), R.string.success, Toast.LENGTH_LONG);
                                    toast.show();
                                    sharedPref.edit().clear().apply();
                                    Intent intent = new Intent(getContext(), RegistrationPage.class);
                                    startActivity(intent);
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
                }
            });

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.user_fragment_container, new UserUpdateInfoFragment(getUserId(), getUser()))
                            .commit();
                }
            });
        }

        return rootView;
    }
}