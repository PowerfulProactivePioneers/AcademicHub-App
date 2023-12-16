package com.example.academichub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.academichub.ClassRoomFragment.ClassRoomHome;
import com.example.academichub.ClassRoomFragment.PeopleClassRoom;
import com.example.academichub.responsePackage.AttendanceList;
import com.example.academichub.responsePackage.ClassRoomDB;
import com.example.academichub.responsePackage.StudentMark;
import com.example.academichub.responsePackage.UserIDType;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClassRoomPage extends AppCompatActivity {

    TextView code,name,dept;
    Dialog myDialog;
    Button markButton,attendanceButton, people,home;
    AutoCompleteTextView chooseExam;
    ArrayAdapter<String> adapter;

    DatePickerDialog datePickerDialog;

    TextView date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_room_page);

        SharedPreferences sh = getSharedPreferences("userData", Context.MODE_PRIVATE);

        people = findViewById(R.id.people);
        home = findViewById(R.id.home);
        myDialog = new Dialog(this);
        markButton = findViewById(R.id.marks); //Mark Button
        attendanceButton = findViewById(R.id.attendance); //Attendance Button

        markButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sh.getString("type",null).equals("Faculty"))
                    showDialog();
                else{
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://academichub-restapi.onrender.com/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    RetrofitAPI api = retrofit.create(RetrofitAPI.class);
                    Call<List<AttendanceList>> call = api.getClassWiseAttendance(new StudentMark(sh.getString("id",null),getIntent().getStringExtra("id")));
                    call.enqueue(new Callback<List<AttendanceList>>() {
                        @Override
                        public void onResponse(Call<List<AttendanceList>> call, Response<List<AttendanceList>> response) {
                            Intent i = new Intent(ClassRoomPage.this,DetailedAttendance.class);
                            i.putExtra("data",(Serializable) response.body());
                            startActivity(i);
                        }

                        @Override
                        public void onFailure(Call<List<AttendanceList>> call, Throwable t) {

                        }
                    });
                }
            }
        });

        attendanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sh.getString("type",null).equals("Faculty"))
                    attendanceShowDialog();
                else{
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://academichub-restapi.onrender.com/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    RetrofitAPI api = retrofit.create(RetrofitAPI.class);
                    Call<List<AttendanceList>> call = api.getClassWiseAttendance(new StudentMark(getIntent().getStringExtra("id"),sh.getString("id",null)));
                    call.enqueue(new Callback<List<AttendanceList>>() {
                        @Override
                        public void onResponse(Call<List<AttendanceList>> call, Response<List<AttendanceList>> response) {
                            Intent i = new Intent(ClassRoomPage.this,DetailedAttendance.class);
                            i.putExtra("data",(Serializable) response.body());
                            i.putExtra("cid",getIntent().getStringExtra("id"));
                            startActivity(i);
                        }

                        @Override
                        public void onFailure(Call<List<AttendanceList>> call, Throwable t) {

                        }
                    });
                }
            }
        });

        // People Button
        people.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.fragment_section, PeopleClassRoom.class,null)
                        .setReorderingAllowed(true)
                        .commit();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.fragment_section, ClassRoomHome.class,null)
                        .setReorderingAllowed(true)
                        .commit();
            }
        });
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.pop_up_window, null);


        // Find the AutoCompleteTextView in the dialog view
        AutoCompleteTextView autoCompleteTextView = dialogView.findViewById(R.id.Exam);

        // Define your string items dynamically in Java
        String[] items = {"CAT 1","Assignment 1","CAT2","Assignment 2", "CAT3","Assignment 3"};

        // Create an ArrayAdapter for the AutoCompleteTextView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, items);

        autoCompleteTextView.setAdapter(adapter);

        Button JoinButton = dialogView.findViewById(R.id.Submit); // Replace with your button ID
        JoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toTableActivity = new Intent(getApplicationContext(), MarkTableActivity.class);
                toTableActivity.putExtra("name",autoCompleteTextView.getText().toString());
                toTableActivity.putExtra("id",getIntent().getStringExtra("id"));
                startActivity(toTableActivity);
            }
        });
        builder.setView(dialogView)
                .show();
    }

    private void attendanceShowDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.attendance_pop_up_window, null);

        date = dialogView.findViewById(R.id.Date);
        datepicker();
        Button JoinButton = dialogView.findViewById(R.id.Submit); // Replace with your button ID
        JoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toTableActivity = new Intent(getApplicationContext(), AttendanceTableActivity.class);
                toTableActivity.putExtra("id",getIntent().getStringExtra("id"));
                toTableActivity.putExtra("date",date.getText());
                startActivity(toTableActivity);
            }
        });
        builder.setView(dialogView)
                .show();
    }
    private void datepicker() {
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(ClassRoomPage.this,
                        new DatePickerDialog.OnDateSetListener() {
                            String res;
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                res = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                date.setText(res);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
    }
}