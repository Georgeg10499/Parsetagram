package com.georgeg10499.parsetagram;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ProfileFragment extends Fragment{

    private static final int PROFILE_GALLERY = 2;
    private Button btnLogout;

    ImageView ivProfile;
    Bitmap photoBitmap;
    private Button btnProfile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        btnLogout = (Button) rootView.findViewById(R.id.btnLogout);
        btnProfile = (Button) rootView.findViewById(R.id.btnSelectProfileImage);
        ivProfile = (ImageView) rootView.findViewById(R.id.ivProfile);

        loadAvatarImage((ParseFile)ParseUser.getCurrentUser().getParseFile("avatar"),ivProfile);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent , PROFILE_GALLERY);
            }
        });

        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Toast.makeText(activity, "Picture was taken!", Toast.LENGTH_SHORT).show();
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PROFILE_GALLERY){
                Uri uri = data.getData();
                String profileImageFilePath = getPath(uri);

                try {
                    final ParseFile parseFile;
                    photoBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                    ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    photoBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
                    parseFile = new ParseFile(bytes.toByteArray());
                    currentUser.put("avatar", parseFile);
                    loadAvatarImage(parseFile,ivProfile);
                    currentUser.saveInBackground();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                final File file = new File(profileImageFilePath);
                final ParseFile parseImageProfile = new ParseFile(file);


            } else { // Result was a failure
                //Toast.makeText(activity, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * helper to retrieve the path of an image URI
     */
    public String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        // this is our fallback here
        return uri.getPath();
    }

    private void logout() {
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
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
