package com.example.instagramclone.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramclone.Model.Comment;
import com.example.instagramclone.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private Context context;
    private List<Comment> commentsList;
    private ParseUser currentuser;
    private ArrayList<String> commentsDetails;
    private String target;
    private String replacement;

    public CommentAdapter(Context context, List<Comment> commentsList) {
        this.context = context;
        this.commentsList = commentsList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false);
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        target = "http://127.0.0.1:1337";
        replacement = "http://52.14.228.32";

        currentuser = ParseUser.getCurrentUser();

        Comment comment = commentsList.get(position);

        holder.commentTextView.setText(comment.getComment());
        ParseQuery<ParseUser> publisher = ParseUser.getQuery();
        publisher.whereEqualTo("username",comment.getPublisher());
        publisher.setLimit(1);
        publisher.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e == null && objects.size() >0){
                    Log.i("!@#$%^&*(","in here");
                    for (ParseUser user:objects){
                        holder.publisherUsername.setText(user.getUsername());
                        ParseFile file = (ParseFile) user.get("Profile_Pic");
                        String temp = file.getUrl().toString();
                        String profile_url = temp.replace(target, replacement);
                        Log.i("Publisher image Url",profile_url);
                        Picasso.get().load(profile_url).into(holder.imageProfileCV);
                    }
                }else{
                    Log.i("!@#$%^&*(","Problem");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView imageProfileCV;
        public TextView publisherUsername;
        public TextView commentTextView;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);


            imageProfileCV = itemView.findViewById(R.id.imageProfileCV);
            publisherUsername = itemView.findViewById(R.id.publisherUsername);
            commentTextView = itemView.findViewById(R.id.commentTextView);

        }
    }

}
