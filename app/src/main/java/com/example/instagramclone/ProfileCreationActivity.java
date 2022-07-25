package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.theartofdev.edmodo.cropper.CropImage;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;

public class ProfileCreationActivity extends AppCompatActivity {


    private Uri imageUri;
    private ImageView close;
    private ImageView imageAdded;
    private TextView post;
    SocialAutoCompleteTextView newBio;
    private EditText fullNameTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creation);

        close = findViewById(R.id.close);
        imageAdded = findViewById(R.id.image_added);
        post = findViewById(R.id.post);
        newBio = findViewById(R.id.bioTextField);
        fullNameTextField = findViewById(R.id.fullNameTextField);


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        CropImage.activity().start(ProfileCreationActivity.this);


    }

    private void uploadImage() {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading image");
        pd.show();
        if (imageUri != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                ParseFile file = new ParseFile("image.png", byteArray);
                if (newBio.getText().toString().isEmpty()) {
                    Toast.makeText(ProfileCreationActivity.this, "Enter your bio", Toast.LENGTH_SHORT).show();
                } else if(fullNameTextField.getText().toString().isEmpty()){
                    Toast.makeText(ProfileCreationActivity.this, "Enter your Fullname", Toast.LENGTH_SHORT).show();
                }else{
                    ParseUser currentuser = ParseUser.getCurrentUser();
                    currentuser.put("Profile_Pic", file);
                    currentuser.put("Fullname", fullNameTextField.getText().toString());
                    currentuser.put("bio", newBio.getText().toString());
                    currentuser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(ProfileCreationActivity.this, "All complete", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ProfileCreationActivity.this, UserActivity.class));
                                finish();
                            } else {
                                Toast.makeText(ProfileCreationActivity.this, "The was a problem", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ProfileCreationActivity.this, ProfileCreationActivity.class));
                                finish();
                            }

                        }
                    });
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(ProfileCreationActivity.this, "Select your profile picture", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                uploadImage();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            imageAdded.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "Error! Try again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ProfileCreationActivity.this, UserActivity.class));
            finish();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (imageUri != null) {


        }
    }
}