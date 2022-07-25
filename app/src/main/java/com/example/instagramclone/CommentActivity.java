package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instagramclone.Adapter.CommentAdapter;
import com.example.instagramclone.Model.Comment;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentActivity extends AppCompatActivity {

    private EditText addComment;
    private CircleImageView profileImage;
    private TextView post;

    private String postId;
    private String authorId;

    ParseUser currentUser;
    private String target;
    private String replacement;

    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    private ArrayList<String> commentsDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        target = "http://127.0.0.1:1337";
        replacement = "http://52.14.228.32";

        addComment = findViewById(R.id.addComment);
        profileImage = findViewById(R.id.profileImage);
        post = findViewById(R.id.post);

        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");
        authorId = intent.getStringExtra("authorId");
        currentUser = ParseUser.getCurrentUser();

        getUsersProfileImage();

        recyclerView = findViewById(R.id.commentsRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(this,commentList);
        recyclerView.setAdapter(commentAdapter);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(addComment.getText().toString())){

                }else{
                    putComment();
                    addComment.setText("");
                }
            }
        });
        getComments();
    }

    private void getComments() {
//        commentList.clear();
        ParseQuery<ParseObject> image = ParseQuery.getQuery("Image");
        image.getInBackground(postId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null && object != null){

                    commentsDetails = (ArrayList<String>)object.get("Comments");
                    for(int i = 0; i < commentsDetails.size(); i++){
                        String[] temp = commentsDetails.get(i).split("-&-");
                        String publisher = temp[0];
                        Log.i("Publisher name", publisher);
                        String comment = temp[1];
                        Log.i("Publisher comment", comment);
                        Comment tempComment = new Comment(postId,comment,publisher);
                        commentList.add(tempComment);
                    }
                    commentAdapter.notifyDataSetChanged();
                }
            }
        });


    }

    private void putComment() {

        String commentDetails = currentUser.getUsername() + "-&-" + addComment.getText().toString();


        ParseQuery<ParseObject> imagePost = ParseQuery.getQuery("Image");
        imagePost.getInBackground(postId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null && object != null){
                    object.add("Comments",commentDetails);
                }else{
                    Toast.makeText(CommentActivity.this, "There was a problem, try again",Toast.LENGTH_SHORT).show();
                }
                object.saveInBackground();
            }
        });
    }

    private void getUsersProfileImage() {
        ParseFile file = (ParseFile) currentUser.get("Profile_Pic");
        String temp = file.getUrl().toString();
        String profile_url = temp.replace(target, replacement);
        Picasso.get().load(profile_url).into(profileImage);
    }
}