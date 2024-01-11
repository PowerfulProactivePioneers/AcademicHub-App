package com.example.academichub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.academichub.HomePageFragment.AttendanceFragment;
import com.example.academichub.HomePageFragment.HomeFragment;
import com.example.academichub.HomePageFragment.ProfilePage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomePage extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    Button img1,img2,img3,img4;
    Button btn;
    FragmentManager fm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        auth = FirebaseAuth.getInstance();
        img1 = findViewById(R.id.home);
        img2 = findViewById(R.id.profile);
        img3 = findViewById(R.id.create_join);
        img4 = findViewById(R.id.attendance);
        fm = getSupportFragmentManager();

        if(auth.getCurrentUser()!=null){
            user = auth.getCurrentUser();
        }
        else{
            Intent i = new Intent(this,LoginPage.class);
            startActivity(i);
            finish();
        }

        if(getSharedPreferences("userData", Context.MODE_PRIVATE).getString("type",null).equals("Faculty")){
            img4.setVisibility(View.GONE);
        }

        img3.setText(getSharedPreferences("userData", Context.MODE_PRIVATE).getString("type",null).equals("Student") ? "JOIN" : "CREATE");

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm.beginTransaction()
                        .replace(R.id.fragment_section, HomeFragment.class,null)
                        .setReorderingAllowed(true)
                        .commit();
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                fm.beginTransaction()
                        .replace(R.id.fragment_section, ProfilePage.class,null)
                        .setReorderingAllowed(true)
                        .commit();
            }
        });

        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(img3.getText().equals("JOIN")){
                    PopUpClass popUpClass = new PopUpClass();
                    popUpClass.showPopupWindow(view);
                }
                else {
                    Intent i = new Intent(getApplicationContext(), CreateClassroomPage.class);
                    startActivity(i);
                }
            }
        });

        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm.beginTransaction()
                        .replace(R.id.fragment_section, AttendanceFragment.class,null)
                        .setReorderingAllowed(true)
                        .commit();
            }
        });

    }
}