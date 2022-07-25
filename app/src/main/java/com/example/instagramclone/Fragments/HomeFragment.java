package com.example.instagramclone.Fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.instagramclone.Adapter.PostAdapter;
import com.example.instagramclone.Model.Post;
import com.example.instagramclone.PostActivity;
import com.example.instagramclone.R;
import com.example.instagramclone.UserActivity;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewPosts;
    private PostAdapter postAdapter;
    private List<Post> postsList;
    private ArrayList<String> namesFollowing;
    private String currentUserUsername;
    private String target;
    private String replacement;
    public ImageView homeProfilePic;
    private ImageView post_btn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        target = "http://127.0.0.1:1337";
        replacement = "http://52.14.228.32";

        post_btn = view.findViewById(R.id.post_btn);
        post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhoto();
            }
        });
        homeProfilePic = view.findViewById(R.id.homeProfilePic);
        if(ParseUser.getCurrentUser().get("Profile_Pic") == null){
            Picasso.get().load("drawable/default_profile_pic.png").into(homeProfilePic);
        }else{
            ParseFile file = (ParseFile) ParseUser.getCurrentUser().get("Profile_Pic");
            String temp = file.getUrl().toString();
            String profile_url = temp.replace(target, replacement);
            Picasso.get().load(profile_url).into(homeProfilePic);
        }


        target = "http://127.0.0.1:1337";
        replacement = "http://52.14.228.32";

        currentUserUsername = ParseUser.getCurrentUser().getUsername();

        namesFollowing = (ArrayList<String>) ParseUser.getCurrentUser().get("isFollowed");

        recyclerViewPosts = view.findViewById(R.id.recycleViewPosts);
        recyclerViewPosts.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerViewPosts.setLayoutManager(linearLayoutManager);


        postsList = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(),postsList);
        recyclerViewPosts.setAdapter(postAdapter);

        checkFollowingUsers(namesFollowing);
        return view;
    }




    private void checkFollowingUsers(ArrayList<String> namesFollowing) {

//        postsList.clear();
        ParseQuery<ParseUser> usersQuery = ParseUser.getQuery();
        usersQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e == null && objects.size() > 0){

                    for(ParseUser selecteduser: objects){
                        for(int i = 0; i < namesFollowing.size(); i++){
                            if(namesFollowing.get(i).equals(selecteduser.getUsername())){

                                ParseQuery<ParseObject> postsQuery = new ParseQuery("Image");
                                postsQuery.whereEqualTo("username",selecteduser.getUsername());
                                postsQuery.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> objects, ParseException e) {
                                        if(e == null && objects.size() > 0){
                                            for(ParseObject currentpost : objects){
                                                String description;
                                                if(currentpost.get("description") == null){
                                                    description = "No description";
                                                }else{
                                                    description = currentpost.get("description").toString();
                                                }
                                                ParseFile file = (ParseFile) currentpost.get("image");
                                                String temp = file.getUrl().toString();
                                                String imageURL = temp.replace(target, replacement);
                                                String postId = currentpost.getObjectId();
                                                String poster = currentpost.get("username").toString();
                                                Post post = new Post( description, imageURL, postId, poster);
                                                postsList.add(post);
                                            }
                                            postAdapter.notifyDataSetChanged();
                                        }
                                    }
                                });
                            }
                        }
                    }

                }
            }
        });


    }


    public void getPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri selectedImage = data.getData();

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(), selectedImage);

                Log.i("Image Selected", "Good work");

                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                byte[] byteArray = stream.toByteArray();

                ParseFile file = new ParseFile("image.png", byteArray);

                ParseObject object = new ParseObject("Image");

                object.put("image", file);

                object.put("username", ParseUser.getCurrentUser().getUsername());

                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(com.parse.ParseException e) {
                        if (e == null) {
                            Toast.makeText(getContext(), "Image has been shared!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "There has been an issue uploading the image :(", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPhoto();
            }
        }
    }

}