package com.georgeg10499.parsetagram;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.georgeg10499.parsetagram.Model.ImagePost;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

public class CameraFragment extends Fragment{
    private static final int SELECT_PICTURE = 1;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888;
    public final String APP_TAG = "MyCustomApp";

    public String photoFileName = "photo.jpg";
    File photoFile;

    private ImageView ivImage;
    private EditText etDescription;
    private Button btnSelect;
    private Button btnCreate;
    private ImageView ivLogout;
    //private ParseFile parseFile;
    public Activity activity;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_camera, container, false);

        etDescription = (EditText) rootView.findViewById(R.id.etDescription);
        btnCreate = (Button) rootView.findViewById(R.id.btnCreate);
        btnSelect = (Button) rootView.findViewById(R.id.btnSelect);
        ivImage = (ImageView) rootView.findViewById(R.id.ivImage);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(activity, "Clicked Post", Toast.LENGTH_SHORT).show();
                final String description = etDescription.getText().toString();
                final ParseUser currUser = ParseUser.getCurrentUser();
                final File image = photoFile;

                final ParseFile parseFile = new ParseFile(image);

                createPost(description, parseFile, currUser);
            }
        });

        //loadTopPosts();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();

        //Toast.makeText(activity, "Enter Camera", Toast.LENGTH_SHORT).show();

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create Intent to take a picture and return control to the calling application

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                Toast.makeText(activity, "Enter Camera", Toast.LENGTH_SHORT).show();
                // Create a File reference to access to future access
                photoFile = getPhotoFileUri(photoFileName);

                // wrap File object into a content provider
                // required for API >= 24
                // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
                Uri fileProvider = FileProvider.getUriForFile(activity, "com.codepath.fileprovider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

                // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
                // So as long as the result is not null, it's safe to use the intent.
                if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                    // Start the image capture intent to take photo
                    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(activity, "Picture was taken!", Toast.LENGTH_SHORT).show();
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // by this point we have the camera photo on disk
                Toast.makeText(activity, "Set up image", Toast.LENGTH_SHORT).show();
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
//                // RESIZE BITMAP
//                Uri takenPhotoUri = getPhotoFileUri(photoFileName);
//                // by this point we have the camera photo on disk
//                Bitmap rawTakenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
//                // See BitmapScaler.java: https://gist.github.com/nesquena/3885707fd3773c09f1bb
//                Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(rawTakenImage, SOME_WIDTH);


                ivImage.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(activity, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
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
        // this is our fallback here
        return uri.getPath();
    }


    private void createPost(final String description, final ParseFile imageFile, final ParseUser user) {
        imageFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("Image", "Image saved");

                    final ImagePost newPost = new ImagePost();
                    newPost.setDescription(description);
                    newPost.setImage(imageFile);
                    newPost.setUser(user);

                    newPost.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.d("HomeActivity", "Create Post Success");
                            } else {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    e.printStackTrace();
                }
            }
        });

    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

}


