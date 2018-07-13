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
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.georgeg10499.parsetagram.Model.ImagePost;
import com.parse.ParseFile;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    List<ImagePost> imagePosts;
    Context context;

    public PostAdapter(List<ImagePost> posts) {
        this.imagePosts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);



        View v = inflater.inflate(R.layout.item_image_post, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return imagePosts.size();
    }

    // bind the values based on the position of the element

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get the data according to position
        final ImagePost imagePost = imagePosts.get(position);
        // populate the view according to the data

        holder.username.setText(imagePost.getUser().getUsername());
        holder.description.setText(imagePost.getDescription());

        loadPostImage(imagePost.getImage(), holder.image);
        loadAvatarImage(imagePost.getAvatar(), holder.avatar);
    }

    //create ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public ImageView avatar;
        public TextView username;
        public TextView likeCount;
        public TextView description;
        public TextView timeSince;
        public ImageView likeButton;
        public ImageView commentButton;
        public ImageView messageButton;


        public ViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.post_image_iv);
            avatar = (ImageView) itemView.findViewById(R.id.user_avatar_iv);
            username = (TextView) itemView.findViewById(R.id.username_tv);
            likeCount = (TextView) itemView.findViewById(R.id.likes_count_tv);
            description = (TextView) itemView.findViewById(R.id.description_tv);
            timeSince = (TextView) itemView.findViewById(R.id.time_since_tv);
            likeButton = (ImageView) itemView.findViewById(R.id.like_iv);
            commentButton = (ImageView) itemView.findViewById(R.id.comment_iv);
            messageButton = (ImageView)  itemView.findViewById(R.id.message_iv);
            //item

        }

    }

    // Clean all elements of the recycler
    public void clear() {
        imagePosts.clear();
        notifyDataSetChanged();
    }


    // Add a list of items -- change to type used
    public void addAll(List<ImagePost> list) {
        imagePosts.addAll(list);
        notifyDataSetChanged();
    }

    private void loadPostImage(final ParseFile imageFile, final ImageView imageView) {
        if (imageFile == null) {
            imageView.setImageResource(R.color.grey_5);
        } else {
            Glide.with(imageView)
                    .asBitmap()
                    .load(imageFile.getUrl())
                    .apply(
                            RequestOptions.centerCropTransform()
                                    .placeholder(R.color.grey_5)
                                    .error(R.color.grey_5)
                    )
                    .transition(
                            BitmapTransitionOptions.withCrossFade()
                    )
                    .into(imageView);
        }
    }

    private void loadAvatarImage(final ParseFile avatarFile, final ImageView avatarView) {
        if (avatarFile == null) {
            avatarView.setImageResource(R.drawable.ic_placeholder_circle);
        } else {
            Glide.with(avatarView)
                    .asBitmap()
                    .load(avatarFile.getUrl())
                    .apply(
                            RequestOptions.circleCropTransform()
                                    .placeholder(R.drawable.ic_placeholder_circle)
                                    .error(R.drawable.ic_placeholder_circle)
                    )
                    .transition(BitmapTransitionOptions.withCrossFade())
                    .into(avatarView);

        }
    }

}
