package com.example.instagramclone.Fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.instagramclone.MainActivity;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private CircleImageView imageProfile;
    private ImageView options;
    private TextView followers;
    private TextView following;
    private TextView usersPosts;
    private TextView username;
    private TextView fullname;
    private TextView bio;
    private String target;
    private String replacement;

    private ParseUser loggedInUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        loggedInUser = ParseUser.getCurrentUser();
        imageProfile = view.findViewById(R.id.image_profile);
        options = view.findViewById(R.id.logoutOption);
        followers = view.findViewById(R.id.followers);
        following = view.findViewById(R.id.following);
        usersPosts = view.findViewById(R.id.postsByUser);
        username = view.findViewById(R.id.username);
        fullname = view.findViewById(R.id.fullname);
        bio = view.findViewById(R.id.bio);
        target = "http://127.0.0.1:1337";
        replacement = "http://52.14.228.32";
        getUserInfo();
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });
        return view;
    }

    private void getUserInfo() {

        Log.i("Followers!@#$%^&*", Integer.toString(loggedInUser.getInt("Followers")));

        followers.setText(String.valueOf(loggedInUser.getInt("Followers")));
        ArrayList<String> followingCount = (ArrayList<String>) loggedInUser.get("isFollowed");
        following.setText(String.valueOf(followingCount.size()));
        ParseFile file = (ParseFile) loggedInUser.get("Profile_Pic");
        String temp = file.getUrl().toString();
        String profile_url = temp.replace(target, replacement);
        Picasso.get().load(profile_url).into(imageProfile);
        username.setText(loggedInUser.getUsername());
        fullname.setText(loggedInUser.get("Fullname").toString());
        bio.setText(loggedInUser.get("bio").toString());
        ParseQuery<ParseObject> postsOfLoggeduser = ParseQuery.getQuery("Image");
        postsOfLoggeduser.whereEqualTo("username",loggedInUser.getUsername());
        postsOfLoggeduser.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null &&  objects.size() > 0){
                    usersPosts.setText(String.valueOf(objects.size()));
                }
            }
        });
    }
}