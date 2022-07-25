package com.example.instagramclone.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.instagramclone.Adapter.UserAdapter;
import com.example.instagramclone.Model.User;
import com.example.instagramclone.R;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private SocialAutoCompleteTextView searchBar;
    private List<User> usersList;
    private UserAdapter userAdapter;
    private String target;
    private String replacement;

    private ArrayList<String> usersId;
    private ArrayAdapter arrayAdapter;

    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        target = "http://127.0.0.1:1337";
        replacement = "http://52.14.228.32";

        recyclerView = view.findViewById(R.id.recycleViewUsers);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        usersList = new ArrayList<>();
        userAdapter = new UserAdapter(getContext(), usersList, true);
        recyclerView.setAdapter(userAdapter);
        usersId = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_expandable_list_item_1, usersId);

        searchBar = view.findViewById(R.id.searchBar);
        usersList.clear();
        readUsers();

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String letter = s.toString();
                usersList.clear();
                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
                query.whereStartsWith("username", letter);
                query.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        if (e == null) {
                            usersList.clear();
                            for (ParseUser selectedUser : objects) {
                                String userId = selectedUser.getObjectId();
                                String username = selectedUser.get("username").toString();
                                String fullname = selectedUser.get("Fullname").toString();
                                String email = "temp";
                                String bio = selectedUser.get("bio").toString();

                                ParseFile file = (ParseFile) selectedUser.get("Profile_Pic");
                                String temp = file.getUrl().toString();
                                String profile_url = temp.replace(target, replacement);

                                User user = new User(userId, username, fullname, email, bio, profile_url);
                                usersList.add(user);
                            }
                            userAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return view;
    }

    private void readUsers() {
        Log.i("readUsers", "In here");
        usersList.clear();
        ParseQuery<ParseUser> userParseQuery = ParseUser.getQuery();
        userParseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        userParseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (TextUtils.isEmpty(searchBar.getText().toString())) {
                    usersList.clear();
                    if (e == null && objects.size() > 0) {
                        Log.i("if", "In here");
                        Log.i("if_2", "In here");
                        usersList.clear();
                        for (ParseUser userParse : objects) {
                            String userId = userParse.getObjectId();
                            String username = userParse.get("username").toString();
                            String fullname = userParse.get("Fullname").toString();
                            String email = "temp";//userParse.get("email").toString();
                            String bio = userParse.get("bio").toString();
                            ParseFile file = (ParseFile) userParse.get("Profile_Pic");
                            String temp = file.getUrl().toString();
                            String profile_url = temp.replace(target, replacement);
                            User user = new User(userId, username, fullname, email, bio, profile_url);
                            usersList.add(user);
                        }
                        userAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

}