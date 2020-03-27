package com.example.finallogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home_normal extends AppCompatActivity {
    Button signout;
    TextView Name,id;
    String uid,name;
    GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_normal);
        Name=findViewById(R.id.textView_normal);
        id=findViewById(R.id.textView2_normal);
        signout=findViewById(R.id.sign_out_normal);


        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()){
                    case R.id.sign_out_normal:
                        signOut();
                        break;
                }

            }
        });

               FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
             name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
             uid = user.getUid();


        }
        Name.setText(name);
        id.setText(uid);
    }
    private void signOut() {
      FirebaseAuth.getInstance()
                .signOut();

       Toast.makeText(Home_normal.this, "Signed out successfully", Toast.LENGTH_LONG).show();
        finish();

    }
}
