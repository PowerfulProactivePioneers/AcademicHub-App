package com.example.academichub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.academichub.responsePackage.ClassRoomDB;
import com.example.academichub.responsePackage.Status;
import com.example.academichub.responsePackage.StudentFacultyDB;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Calendar;
import androidx.appcompat.app.AppCompatActivity;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateClassroom extends AppCompatActivity {

    Button btn;

    AutoCompleteTextView autoCompleteTextView,autoCompleteTextView1,deptbtn;
    ArrayAdapter<String> adapterItems,adapt;
    String dept_items[]={"CSE","ECE","EEE","IT","ALL"};
    String sec_items[]={"A","B","C","N"};

    public void sendData(String id,String code,String name,String dept,String section,String fac_id){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://academichub-restapi.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI api = retrofit.create(RetrofitAPI.class);
        Call<Status> call = api.createClass(new ClassRoomDB(id,name,code,fac_id,dept,section.charAt(0)));

        call.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status st = response.body();
                Log.d("create-class",st.msg);
                if(st.msg.equals("success") || st.msg.contains("trailing junk")){
                    startActivity(new Intent(getApplicationContext(),HomePage.class));
                }
                else{
                    Toast.makeText(getApplicationContext(),"Already Created",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_classroom);

        TextInputLayout subject_code = findViewById(R.id.Createsubjectcode);
        TextInputEditText editTextcode = (TextInputEditText) subject_code.getEditText();
        TextInputLayout subject_name = findViewById(R.id.Createsubjectname);
        TextInputEditText editTextname = (TextInputEditText) subject_name.getEditText();
        TextInputLayout department=findViewById(R.id.dept);
        AutoCompleteTextView departmentEditText= (AutoCompleteTextView) department.getEditText();
        TextInputLayout section=findViewById(R.id.sections);
        AutoCompleteTextView sectionEditText= (AutoCompleteTextView) section.getEditText();



        autoCompleteTextView=findViewById(R.id.auto_complete_txt);
        adapterItems =new ArrayAdapter<String>(this,R.layout.dropdown_item,dept_items);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView1=findViewById(R.id.auto_complete_txt1);
        adapt =new ArrayAdapter<String>(this,R.layout.dropdown_item,sec_items);
        autoCompleteTextView1.setAdapter(adapt);

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String depts = autoCompleteTextView.getText().toString();
                if (depts.equals("ALL")) {
                    section.setVisibility(View.GONE);
                    autoCompleteTextView1.setVisibility(View.GONE);
                } else {
                    section.setVisibility(View.VISIBLE);
                    autoCompleteTextView1.setVisibility(View.VISIBLE);
                }
            }
        });



        btn=findViewById(R.id.Create);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String subcode = editTextcode.getText().toString().trim();
                String name =editTextname.getText().toString().trim();
                String depts=departmentEditText.getText().toString();
                String section=sectionEditText.getText().toString();
                Boolean flag=true;
                if(!subcode.equals("") && !name.equals(""))
                {
                    flag=true;
                }
                else if(subcode.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please Enter the subject code",Toast.LENGTH_SHORT).show();
                    flag=false;
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please Enter the subject name",Toast.LENGTH_SHORT).show();
                    flag=false;
                }

                if(flag)
                {
                    String id="";
                    String currentYear = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
                    subcode = subcode.toUpperCase();
                    if(!depts.equals("ALL") && !section.equals("N"))
                    {
                        id=subcode+"_"+section+"_"+currentYear;
                    }
                    else if(depts.equals("ALL") || section.equals("N"))
                    {
                        id=subcode+"_"+currentYear;
                    }

                    SharedPreferences userData = getSharedPreferences("userData", Context.MODE_PRIVATE);
                    sendData(id,subcode,name,depts,section,userData.getString("id",null));
                }
            }
        });
    }
}
