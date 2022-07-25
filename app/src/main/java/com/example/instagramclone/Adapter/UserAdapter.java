package com.example.instagramclone.Adapter;

import android.content.Context;
import android.media.Image;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramclone.Model.User;
import com.example.instagramclone.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{


    private Context context;
    private List<User> usersList;
    private boolean isFragment;
    private ParseUser parseUser;
    private ArrayList<String> namesFollowing;

    public UserAdapter(Context context, List<User> usersList, boolean isFragment) {
        this.context = context;
        this.usersList = usersList;
        this.isFragment = isFragment;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        namesFollowing = (ArrayList<String>)ParseUser.getCurrentUser().get("isFollowed");
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        parseUser = ParseUser.getCurrentUser();
        User user = usersList.get(position);
        holder.followBtn.setVisibility(View.VISIBLE);
        holder.username.setText(user.getUsername());
        holder.fullname.setText( user.getFullname());

        Picasso.get().load(user.getProfileImage()).placeholder(R.mipmap.ic_launcher).into(holder.imageProfile);

        isFollowed(user.getUsername(), holder.followBtn,namesFollowing);

        if(user.getId().equals(ParseUser.getCurrentUser().getObjectId())){
            holder.followBtn.setVisibility(View.GONE);
        }

        holder.followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.followBtn.getText().toString().equals("Follow")){
                    ParseUser.getCurrentUser().add("isFollowed",user.getUsername());
                    holder.followBtn.setText("Following");
                }else{
                    ParseUser.getCurrentUser().getList("isFollowed").remove(user.getUsername());
                    List tempUsers = ParseUser.getCurrentUser().getList("isFollowed");
                    ParseUser.getCurrentUser().remove("isFollowed");
                    ParseUser.getCurrentUser().put("isFollowed",tempUsers);
                    holder.followBtn.setText("Follow");
                }
                ParseUser.getCurrentUser().saveInBackground();
            }
        });

    }

    private void isFollowed(final String username, Button followBtn, List<String> usernames) {
        followBtn.setText("Follow");
        for(int i = 0; i < usernames.size(); i++){
            if(usernames.get(i).equals(username)){
                followBtn.setText("Following");
            }
        }
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public  class  ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView imageProfile;
        public TextView username;
        public TextView fullname;
        public Button followBtn;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.imageProfile);
            username = itemView.findViewById(R.id.username);
            fullname = itemView.findViewById(R.id.fullname);
            followBtn = itemView.findViewById(R.id.followBtn);

        }
    }

}
