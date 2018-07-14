package com.georgeg10499.parsetagram;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.georgeg10499.parsetagram.Model.ImagePost;
import com.loopj.android.http.AsyncHttpClient;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;


public class FeedFragment extends Fragment {
    private InteractionListener mListener;

    public FeedFragment() {
        // Required empty public constructor
    }


    private SwipeRefreshLayout swipeContainer;

    //private HomeFragmentListener
    AsyncHttpClient client;
    PostAdapter postAdapter;
    ArrayList<ImagePost> posts;
    RecyclerView rvPosts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        client = new AsyncHttpClient();
        posts = new ArrayList<>();
        postAdapter = new PostAdapter(posts);

        rvPosts = (RecyclerView) rootView.findViewById(R.id.rvPost);

        rvPosts.addItemDecoration(new SimpleDividerDecorator(getActivity()));

        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPosts.setAdapter(postAdapter);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        loadTopPosts();
    }

    private void loadTopPosts() {
        Toast.makeText(getActivity(), "Loading posts!", Toast.LENGTH_SHORT).show();
        final ImagePost.Query postsQuery = new ImagePost.Query();
        postsQuery.limit20().withUser();
        postsQuery.orderByDescending("createdAt");
        postsQuery.findInBackground(new FindCallback<ImagePost>() {
            @Override
            public void done(List<ImagePost> objects, ParseException e) {
                if (e == null){
                    //Toast.makeText(getActivity(), "Add Posts", Toast.LENGTH_SHORT).show();
                    Log.d("HomeActivity", Integer.toString(objects.size()));
                    for(int i = 0; i < objects.size(); i++) {
//                        Log.d("HomeActivity", "Post[" + i + "] = " + objects.get(i).getDescription()
//                                + "\nusername = " + objects.get(i).getUser().getUsername());
//                        posts.addAll(objects);
//                        postAdapter.notifyDataSetChanged();
                    }
                    postAdapter.clear();
                    postAdapter.addAll(objects);
                    postAdapter.notifyDataSetChanged();
                } else {
                    //Toast.makeText(getActivity(), "null?", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    public void fetchTimelineAsync(int page) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.
        postAdapter.clear();
        posts.clear();
        // ...the data has come back, add new items to your adapter...
        loadTopPosts();
        postAdapter.addAll(posts);
        // Now we call setRefreshing(false) to signal refresh has finished
        swipeContainer.setRefreshing(false);
    }

    public interface MainActivityListener {
        void sendPostToMainActivity(ImagePost post);
    }


}
