package com.example.instagramclone.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramclone.CommentActivity;
import com.example.instagramclone.Model.Post;
import com.example.instagramclone.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private Context context;
    private List<Post> postsList;
    private ParseUser parseUser;
    private String target;
    private String replacement;
    private ArrayList<String> likedbyuser;
    private ArrayList<String> commentsByUsers;


    public PostAdapter(Context context, List<Post> postsList) {
        this.context = context;
        this.postsList = postsList;
        parseUser = ParseUser.getCurrentUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_item, parent, false);
        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        target = "http://127.0.0.1:1337";
        replacement = "http://52.14.228.32";


        Post post = postsList.get(position);
        Picasso.get().load(post.getImageURL()).into(holder.postImageView);
        holder.descriptionTextView.setText(post.getDescription());


        ParseQuery<ParseUser> user = ParseUser.getQuery();
        user.whereEqualTo("username", post.getPoster());
        user.setLimit(1);
        user.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    for (ParseUser tempuser : objects) {
                        String tempusername = tempuser.getUsername();
                        ParseFile file = (ParseFile) tempuser.get("Profile_Pic");
                        String temp = file.getUrl().toString();
                        String profile_url = temp.replace(target, replacement);
                        Picasso.get().load(profile_url).into(holder.profileImageView);
                        holder.usernameTextView.setText(tempuser.getUsername());
                        holder.authorTextView.setText(tempuser.get("Fullname").toString());
                    }
                }
            }
        });

        countComments(post.getPostId(),holder.noOfCommentsTextView);

        ParseQuery<ParseObject> image = ParseQuery.getQuery("Image");
        image.getInBackground(post.getPostId(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null && object != null) {
                    likedbyuser = (ArrayList<String>)object.get("Liked_By");
                    String noOfLikes = String.valueOf(likedbyuser.size());
                    holder.noOfLikesTextView.setText(noOfLikes);
                    for(int i = 0; i < likedbyuser.size(); i++){
                        if(likedbyuser.get(i).equals(parseUser.getUsername())){
                            holder.likeImageView.setImageResource(R.drawable.ic_liked);
                            holder.likeImageView.setTag("Liked");
                        }else{
                            holder.likeImageView.setImageResource(R.drawable.ic_like);
                            holder.likeImageView.setTag("Like");
                        }
                    }
                }
            }
        });



        holder.likeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseQuery<ParseObject> image = ParseQuery.getQuery("Image");
                image.getInBackground(post.getPostId(), new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if (e == null && object != null) {

                            if (holder.likeImageView.getTag().equals("Like")) {
                                object.add("Liked_By", parseUser.getUsername());
                                holder.likeImageView.setImageResource(R.drawable.ic_liked);
                                holder.likeImageView.setTag("Liked");
                                int noOfLikes = Integer.parseInt(holder.noOfLikesTextView.getText().toString());
                                noOfLikes++;
                                holder.noOfLikesTextView.setText(Integer.toString(noOfLikes));
                            } else {
                                object.getList("Liked_By").remove(parseUser.getUsername());
                                ParseUser.getCurrentUser().getList("isFollowed").remove(parseUser.getUsername());
                                List tempImage = object.getList("Liked_By");
                                object.remove("Liked_By");
                                object.put("Liked_By", tempImage);
                                holder.likeImageView.setImageResource(R.drawable.ic_like);
                                holder.likeImageView.setTag("Like");
                                int noOfLikes = Integer.parseInt(holder.noOfLikesTextView.getText().toString());
                                noOfLikes--;
                                holder.noOfLikesTextView.setText(Integer.toString(noOfLikes));
                            }
                            object.saveInBackground();
                        }
                    }
                });
            }
        });


        holder.commentImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("postId",post.getPostId());
                intent.putExtra("authorName",post.getPoster());
                context.startActivity(intent);
            }
        });

        holder.noOfCommentsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("postId",post.getPostId());
                intent.putExtra("authorName",post.getPoster());
                context.startActivity(intent);
            }
        });

    }


    private  void countComments(String postId, TextView view){
        ParseQuery<ParseObject> image = ParseQuery.getQuery("Image");
        image.getInBackground(postId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                commentsByUsers = (ArrayList<String>)object.get("Comments");
                int count = commentsByUsers.size();
                view.setText("See All " + Integer.toString(count) + " Comments");
            }
        });
    }


    @Override
    public int getItemCount() {

        return postsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView profileImageView;
        public ImageView postImageView;
        public ImageView likeImageView;
        public ImageView commentImageView;
        public TextView usernameTextView;
        public TextView noOfLikesTextView;
        public TextView authorTextView;
        public TextView noOfCommentsTextView;
        public TextView descriptionTextView;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            profileImageView = itemView.findViewById(R.id.profilePic);
            postImageView = itemView.findViewById(R.id.postImage);
            likeImageView = itemView.findViewById(R.id.like);
            commentImageView = itemView.findViewById(R.id.comment);

            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            noOfLikesTextView = itemView.findViewById(R.id.noOfLikes);
            authorTextView = itemView.findViewById(R.id.author);
            noOfCommentsTextView = itemView.findViewById(R.id.noOfComments);
            descriptionTextView = itemView.findViewById(R.id.bioTextField);

        }
    }

}
