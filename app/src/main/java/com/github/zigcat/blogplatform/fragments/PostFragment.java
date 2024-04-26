package com.github.zigcat.blogplatform.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.zigcat.blogplatform.R;
import com.github.zigcat.blogplatform.adapters.CommentAdapter;
import com.github.zigcat.blogplatform.api.CommentOkHttpHelper;
import com.github.zigcat.blogplatform.api.CommentRequest;
import com.github.zigcat.blogplatform.api.PostOkHttpHelper;
import com.github.zigcat.blogplatform.fragments.postpage.PostInfoFragment;
import com.github.zigcat.blogplatform.models.Comment;
import com.github.zigcat.blogplatform.models.Post;

import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
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

        LinearLayout user = rootView.findViewById(R.id.post_page_userinfo);
        TextView nickname = rootView.findViewById(R.id.post_page_nickname);
        TextView username = rootView.findViewById(R.id.post_page_username);
        TextView error = rootView.findViewById(R.id.post_page_error);
        ImageView delete = rootView.findViewById(R.id.post_page_delete);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("blogplatform", Context.MODE_PRIVATE);
        int loggedUserId = sharedPref.getInt("id", -1);

        PostOkHttpHelper postOkHttpHelper = new PostOkHttpHelper();
        CommentOkHttpHelper commentOkHttpHelper = new CommentOkHttpHelper();

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
                        user.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getActivity().getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, new UserFragment(post.getUser().getId()))
                                        .commit();
                            }
                        });
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

        commentOkHttpHelper.getCommentsByPost(postId, new CommentOkHttpHelper.CallbackComments() {
            @Override
            public void onSuccess(List<Comment> response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RecyclerView recyclerView = getActivity().findViewById(R.id.comment_recyclerview);
                        Collections.reverse(response);
                        CommentAdapter commentAdapter = new CommentAdapter(response);
                        recyclerView.setAdapter(commentAdapter);
                        commentAdapter.notifyDataSetChanged();
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
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

        EditText newComment = rootView.findViewById(R.id.new_comment);
        Button addComment = rootView.findViewById(R.id.add_comment_button);

        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = newComment.getText().toString();
                if(content.isEmpty()){
                    newComment.setHint(R.string.missing_content);
                } else {
                    CommentRequest request = new CommentRequest(postId, content);
                    String credentials = sharedPref.getString("auth", "");
                    commentOkHttpHelper.createComment(request, credentials, new CommentOkHttpHelper.CallbackResponseString() {
                        @Override
                        public void onSuccess(String response) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast toast = Toast.makeText(getContext(), R.string.success, Toast.LENGTH_LONG);
                                    toast.show();
                                    getActivity().getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.fragment_container, new PostFragment(postId))
                                            .commit();
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