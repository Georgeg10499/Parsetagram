package com.georgeg10499.parsetagram;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.georgeg10499.parsetagram.Model.Post;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    List<Post> posts = new ArrayList<>();
    Context context;

    public PostAdapter(List<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_post, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return  viewHolder;
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    // bind the values based on the position of the element

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get the data according to position
        Post post = posts.get(position);
        // populate the view according to the data
        holder.tvUser.setText(post.getUser().getUsername());
        holder.tvDescription.setText(post.getDescription());

        int radius = 30; // corner radius, higher value = more rounded
        int margin = 10; // crop margin, set to 0 for corners with no crop
        Glide.with(context)
                .load(post.getImage().getUrl())
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(radius, margin)))
                .into(holder.ivImg);
    }

    //create ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivImg;
        public TextView tvUser;
        public TextView tvDescription;

        public ViewHolder(View itemView) {
            super(itemView);

            // perform findViewById lookups
            ivImg = (ImageView) itemView.findViewById(R.id.ivImg);
            tvUser = (TextView) itemView.findViewById(R.id.tvUser);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            // add this as the itemView's OnClickListener
            //itemView.setOnClickListener(this);
        }
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

}
