package com.georgeg10499.parsetagram;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.georgeg10499.parsetagram.Model.Post;
import com.loopj.android.http.AsyncHttpClient;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;


public class FeedFragment extends Fragment {

    AsyncHttpClient client;
    PostAdapter postAdapter;
    ArrayList<Post> posts;
    RecyclerView rvPosts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        client = new AsyncHttpClient();
        posts = new ArrayList<>();
        postAdapter = new PostAdapter(posts);

        rvPosts = (RecyclerView) rootView.findViewById(R.id.rvPost);

        rvPosts.addItemDecoration(new SimpleDividerDecorator(getActivity()));

        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPosts.setAdapter(postAdapter);

        loadTopPosts();

        return rootView;
    }

    private void loadTopPosts() {
        final Post.Query postsQuery = new Post.Query();
        postsQuery.getTop().withUser();
        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null){
                    for(int i = 0; i < objects.size(); i++) {
                        Log.d("HomeActivity", "Post[" + i + "] = " + objects.get(i).getDescription()
                                + "\nusername = " + objects.get(i).getUser().getUsername());
                        posts.addAll(objects);
                        postAdapter.notifyDataSetChanged();
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }



}
