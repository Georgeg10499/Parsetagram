package com.georgeg10499.parsetagram;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.georgeg10499.parsetagram.Model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final int SELECT_PICTURE = 1;
    public static final int RESULT_GALLERY = 0;

    private ImageView ivImage;
    private EditText etDescription;
    private Button btnRefresh;
    private Button btnCreate;

    //20180705_211953.jpg
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        etDescription = (EditText) findViewById(R.id.etDescription);
        btnCreate = (Button) findViewById(R.id.btnCreate);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);
        ivImage = (ImageView) findViewById(R.id.ivImage);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTopPosts();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //final File file = new File(image)
                // in onCreate or any event where your want the user to
                // select a file
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent,
//                        "Select Picture"), SELECT_PICTURE);

//                final String description = etDescription.getText().toString();
//                final ParseUser user = ParseUser.getCurrentUser();
//                final File image = new File(imagePath);
//                if (!image.exists()) {
//                    Toast.makeText(HomeActivity.this, "NOOOOOO", Toast.LENGTH_LONG).show();
//                }
//
//                final ParseFile parseFile = new ParseFile(image);
//
//                createPost(description, parseFile, user);

                Intent galleryIntent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent , RESULT_GALLERY );

            }
        });

        loadTopPosts();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                //imagePath = getPath(selectedImageUri);

                // createPost()
                final String description = etDescription.getText().toString();
                final ParseUser user = ParseUser.getCurrentUser();
                final File image = new File(imagePath);
                final ParseFile parseFile = new ParseFile(image);

                createPost(description, parseFile, user);
            } else if (requestCode == RESULT_GALLERY) {
                Uri selectedImageUri = data.getData();
                imagePath = getPath(selectedImageUri);
                Log.d("Path",imagePath);

                // createPost()
                final String description = etDescription.getText().toString();
                final ParseUser user = ParseUser.getCurrentUser();
                final File image = new File(imagePath);
                final ParseFile parseFile = new ParseFile(image);


                createPost(description, parseFile, user);
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
        Cursor cursor = managedQuery(uri, projection, null, null, null);
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
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }


    private void createPost(final String description, final ParseFile imageFile, final ParseUser user) {
        imageFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("Image", "Image saved");

                    final Post newPost = new Post();
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



}
