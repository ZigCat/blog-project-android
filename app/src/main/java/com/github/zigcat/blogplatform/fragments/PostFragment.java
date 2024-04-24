package com.github.zigcat.blogplatform.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.zigcat.blogplatform.R;
import com.github.zigcat.blogplatform.api.PostOkHttpHelper;
import com.github.zigcat.blogplatform.fragments.postpage.PostInfoFragment;
import com.github.zigcat.blogplatform.models.Post;

import org.w3c.dom.Text;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostFragment extends Fragment {
    private int postId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post, container, false);
        TextView nickname = rootView.findViewById(R.id.post_page_nickname);
        TextView username = rootView.findViewById(R.id.post_page_username);
        TextView error = rootView.findViewById(R.id.post_page_error);
        ImageView delete = rootView.findViewById(R.id.post_page_delete);
        SharedPreferences sharedPref = getActivity().getSharedPreferences("blogplatform", Context.MODE_PRIVATE);
        int loggedUserId = sharedPref.getInt("id", -1);
        PostOkHttpHelper postOkHttpHelper = new PostOkHttpHelper();
        postOkHttpHelper.getPostById(postId, new PostOkHttpHelper.CallbackGetPostListener() {
            @Override
            public void onSuccess(Post post) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        nickname.setText(post.getUser().getNickname());
                        username.setText(post.getUser().getUsername());
                        if(loggedUserId == post.getUser().getId()){
                            delete.setVisibility(View.VISIBLE);
                        }
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .add(R.id.post_page_container, new PostInfoFragment(post.getId(), post.getContent(), post.getUser().getId()))
                                .commit();
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                error.setVisibility(View.VISIBLE);
                String errorMessage = "Error: "+e.getMessage();
                error.setText(errorMessage);
            }
        });

        Dialog deleteDialog = new Dialog(requireContext());
        deleteDialog.setContentView(R.layout.dialog_post_delete);
        deleteDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        deleteDialog.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.rounded_border));
        deleteDialog.setCancelable(false);

        Button dialogConfirm = deleteDialog.findViewById(R.id.post_delete_dialog_confirm);
        Button dialogCancel = deleteDialog.findViewById(R.id.post_delete_dialog_cancel);

        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });

        dialogConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postOkHttpHelper.deletePost(postId, sharedPref.getString("auth", ""), new PostOkHttpHelper.CallbackCreateListener() {
                    @Override
                    public void onSuccess(String response) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast toast = Toast.makeText(getActivity().getApplicationContext(), R.string.success, Toast.LENGTH_LONG);
                                toast.show();
                                getActivity().getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, new HomeFragment())
                                        .commit();
                                deleteDialog.dismiss();
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
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.show();
            }
        });

        return rootView;
    }
}