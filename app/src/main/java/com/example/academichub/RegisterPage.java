package com.example.academichub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.academichub.responsePackage.Status;
import com.example.academichub.responsePackage.StudentFacultyDB;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterPage extends AppCompatActivity {

    TextInputEditText regno,name,type;
    AutoCompleteTextView dept,section;
    TextInputLayout section_layout;
    FirebaseUser user;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);
        user = FirebaseAuth.getInstance().getCurrentUser();
        regno = findViewById(R.id.registerNo);
        name = findViewById(R.id.username);
        dept = findViewById(R.id.department);
        type = findViewById(R.id.Role);
        section = findViewById(R.id.register_section);
        section_layout = findViewById(R.id.section_layout);
        btn = findViewById(R.id.submit);

//      Creating String arrays for dropdowns
        String[] departments = new String[]{"CSE", "ECE", "EEE"};
        String[] sections = new String[]{"A", "B", "C"};

//        Creating Adapter instances
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.dropdown_item, departments);
        dept.setAdapter(adapter1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, R.layout.dropdown_item, sections);
        section.setAdapter(adapter2);

//        Setting OnItemClickListener for both the dropdowns
        dept.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        section.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        String accType = getIntent().getStringExtra("type");
        ArrayList<String> uname = new ArrayList<String>(Arrays.asList( user.getDisplayName().split(" ")));
        String username = "";

        try{
            Integer.parseInt((String) uname.get(0));
            uname.remove(0);
        }
        catch(NumberFormatException e){
        }
        finally {
            username = String.join(" ",uname);
        }

        name.setText(username);
        type.setText(accType);

        if(type.getText().toString().equals("Faculty")){
            section_layout.setVisibility(View.GONE);
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(regno.getText().toString().trim().length() == 0){
                    Toast.makeText(RegisterPage.this, "Enter your Register No.", Toast.LENGTH_SHORT).show();
                }
                else if(name.getText().toString().trim().length() == 0){
                    Toast.makeText(RegisterPage.this, "Enter your Name", Toast.LENGTH_SHORT).show();
                }
                else if(dept.getText().toString().trim().length() == 0){
                    Toast.makeText(RegisterPage.this, "Choose your Department", Toast.LENGTH_SHORT).show();
                }
                else if(accType.equals("Student") && section.getText().toString().trim().length() == 0){
                    Toast.makeText(RegisterPage.this, "Choose your section", Toast.LENGTH_SHORT).show();
                }
                else {
                    sendData(regno.getText().toString(), name.getText().toString(), user.getUid(), dept.getText().toString(), accType, user.getEmail(),
                            accType.equals("Faculty") ? ' ' : section.getText().toString().charAt(0)
                    );
                }
            }
        });

    }


    public void sendData(String id,String name,String uid,String dept,String type,String email,char section){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://academichub-restapi.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI api = retrofit.create(RetrofitAPI.class);
        StudentFacultyDB data = new StudentFacultyDB(id,name,uid,dept,type,email,section);
        Log.d("data",data.toString());
        Call<Status> call = api.createUser(data);

        call.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Log.d("RegisterAPI","Initial");
                Status st = response.body();
                Toast.makeText(RegisterPage.this, st.msg, Toast.LENGTH_SHORT).show();
                if(st.msg.equals("success")){
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
                    Intent i = new Intent(RegisterPage.this,HomePage.class);
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Toast.makeText(RegisterPage.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("RegisterAPIError",t.getMessage());
            }
        });
    }
}