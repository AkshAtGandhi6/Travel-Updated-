package com.example.finallogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.shrikanthravi.customnavigationdrawer2.data.MenuItem;
import com.shrikanthravi.customnavigationdrawer2.widget.SNavigationDrawer;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
Button login,register;
Animation frombottom;
    Button signout;
    TextView Name,id;
    String uid,name;
    GoogleSignInClient mGoogleSignInClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        login=findViewById(R.id.log_btn);
        register=findViewById(R.id.register_btn);

        frombottom= AnimationUtils.loadAnimation(this,R.anim.frombottom);
        login.setAnimation(frombottom);
        register.setAnimation(frombottom);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_activity();

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_activity();
            }
        });


    }
    public void login_activity(){
        Intent intent = new Intent(this,Login.class);
        startActivity(intent);
    }
    public void register_activity(){
        Intent intent = new Intent(this,register.class);
        startActivity(intent);
    }
}
