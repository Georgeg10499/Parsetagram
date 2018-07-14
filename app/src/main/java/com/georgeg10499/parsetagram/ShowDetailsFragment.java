package com.georgeg10499.parsetagram;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ShowDetailsFragment extends Fragment {

    ImageView image;
    ImageView avatar;
    TextView username;
    TextView likeCount;
    TextView description;
    TextView timeSince;
    ImageView likeButton;
    ImageView commentButton;
    ImageView messageButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_show_details, container, false);
        ImagePost post= getArguments().getParcelable("Post");

        image = (ImageView) rootView.findViewById(R.id.post_image_iv);
        avatar = (ImageView) rootView.findViewById(R.id.user_avatar_iv);
        username = (TextView) rootView.findViewById(R.id.username_tv);
        likeCount = (TextView) rootView.findViewById(R.id.likes_count_tv);
        description = (TextView) rootView.findViewById(R.id.description_tv);
        timeSince = (TextView) rootView.findViewById(R.id.time_since_tv);
        likeButton = (ImageView) rootView.findViewById(R.id.like_iv);
        commentButton = (ImageView) rootView.findViewById(R.id.comment_iv);
        messageButton = (ImageView) rootView.findViewById(R.id.message_iv);

//        final ImagePost imagePost = imagePosts.get(position);
//        // populate the view according to the data

        username.setText(post.getUser().getUsername());
        description.setText(post.getDescription());
        timeSince.setText(getRelativeTimeAgo(post.getCreatedAt().toString()));

        loadPostImage(post.getImage(), image);
        loadAvatarImage(post.getAvatar(), avatar);

        return rootView;
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

    public String getRelativeTimeAgo(String rawDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
