package com.example.academichub;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.academichub.responsePackage.StudentFacultyDB;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class LoginPage extends AppCompatActivity {

    Button btn;
    GoogleSignInClient gsc;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        btn = findViewById(R.id.login_btn);
        auth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1095703267421-onu1fv8ii4trrjvupc93tc0l0n0s7vtm.apps.googleusercontent.com")
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this,gso);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = gsc.getSignInIntent();
                startActivityForResult(i,100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount acc = task.getResult(ApiException.class);
                AuthCredential authcr = GoogleAuthProvider.getCredential(acc.getIdToken(),null);
                auth.signInWithCredential(authcr)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    if(user.getEmail().endsWith("@svce.ac.in")) {
                                        checkAccount(user);
                                    }
                                    else{
                                        gsc.signOut();
                                        user.delete()
                                                .addOnCompleteListener(task1 -> {
                                                    if(task1.isSuccessful()){
                                                        Toast.makeText(LoginPage.this, "Sign In with svce email id", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            catch (Exception e){
                Log.d("UserError",e.getMessage());
            }
        }
    }

    public void checkAccount(FirebaseUser user) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://academichub-restapi.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI api = retrofit.create(RetrofitAPI.class);
        Call<StudentFacultyDB> call = api.getUser(user.getEmail());

        String res = "";
        Log.d("ResultAPI","Intial");
        Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();
        call.enqueue(new Callback<StudentFacultyDB>() {
            @Override
            public void onResponse(Call<StudentFacultyDB> call, Response<StudentFacultyDB> response) {
                Log.d("ResultAPI","start");
                StudentFacultyDB data = response.body();
                Log.d("ResultAPI",data.toString());
                if(data.getId() != null){
                    Log.d("ResultAPI","Inside");
                    Log.d("New acc","false");
                    try {
                        SharedPreferences saveData = getSharedPreferences("userData", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = saveData.edit();
                        editor.putString("id",data.getId());
                        editor.putString("name",data.getName());
                        editor.putString("dept",data.getDept());
                        editor.putString("uid",data.getUid());
                        editor.putString("email",data.getEmail());
                        editor.putString("type",data.getType());
                        editor.putString("section",Character.toString(data.getSection()));
                        editor.commit();
                        Intent i = new Intent(LoginPage.this, HomePage.class);
                        startActivity(i);
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Log.d("New acc","true");
                    try {
                        Intent i = new Intent(LoginPage.this, RegisterPage.class);
                        try{
                            Integer.parseInt(user.getEmail().substring(0,4));
                            i.putExtra("type","Student");
                        }
                        catch (Exception e){
                            i.putExtra("type","Faculty");
                        }
                        startActivity(i);
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<StudentFacultyDB> call, Throwable t) {
                Log.d("ErrorAPI", t.getMessage());
            }
        });
    }

    public String typeOfAcc(String email){
        try {
            Integer.parseInt(email.substring(0, 4));
            return "Student";
        } catch (NumberFormatException e) {
            return "Faculty";
        }
    }
}