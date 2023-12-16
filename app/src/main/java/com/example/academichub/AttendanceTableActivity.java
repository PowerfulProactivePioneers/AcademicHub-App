package com.example.academichub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.academichub.responsePackage.Attendance;
import com.example.academichub.responsePackage.Status;
import com.example.academichub.responsePackage.UpdateAttendance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AttendanceTableActivity extends AppCompatActivity {
//    String regno[] = {"2127210501110", "2127210501111", "2127210501114","2127210501115","21217210501094", "2127210501063"};
    
    List<String> regno;
    ArrayList<String> present = new ArrayList<>();
    ArrayList<String> absent = new ArrayList<>();
    ArrayList<Attendance> students_attendance = new ArrayList<Attendance>();
    TableLayout table;
    Button Marksubmitbtn;
    TextView txt;

    public int dpToPx(int val){
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float) val * density);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_activity_table);
        table = findViewById(R.id.Table);

        txt = findViewById(R.id.attendance_date);
        txt.setText(getIntent().getStringExtra("date"));


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://academichub-restapi.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI api = retrofit.create(RetrofitAPI.class);
        Call<List<String>> call = api.getStudentList(getIntent().getStringExtra("id"));
        
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                regno = response.body();
                for (int i = 0; i < regno.size(); i++) {
                    TableRow trow = new TableRow(getApplicationContext());
                    CardView cview = new CardView(getApplicationContext());
                    cview.setCardBackgroundColor(getResources().getColor(R.color.white));
                    LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                    TextView textView = new TextView(getApplicationContext());
                    CheckBox checkBox = new CheckBox(getApplicationContext());

//            Design Stuffs for Linear Layout
                    textView.setText(regno.get(i));
                    textView.setGravity(Gravity.CENTER);

                    LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                            0, // Set layout_width to 0 (will be weighted)
                            ViewGroup.LayoutParams.MATCH_PARENT // Set layout_height to match_parent
                    );
                    textParams.weight = 1; // Distribute space evenly

                    textView.setLayoutParams(textParams);

                    LinearLayout.LayoutParams checkParams = new LinearLayout.LayoutParams(
                            0,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    );
                    checkParams.gravity = Gravity.CENTER;
                    checkParams.weight = 1;
                    checkBox.setLayoutParams(checkParams);

                    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params1.setMargins(dpToPx(30),0,dpToPx(30),0);
//            Adding id to the attendance
                    int id1 = Integer.parseInt(regno.get(i).substring(regno.get(i).length()-3, regno.get(i).length()));
                    checkBox.setId(id1);
                    checkBox.setEms(8);
                    checkBox.setGravity(Gravity.CENTER_HORIZONTAL);

//            Design stuffs for linear & table layouts
                    LinearLayout.LayoutParams linearparams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                    );
                    linearLayout.setLayoutParams(linearparams);
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    linearLayout.setPadding(10,10,10,10);
                    linearLayout.addView(textView);
                    linearLayout.addView(checkBox);

                    // Set the layout_width and layout_height of the CardView to match_parent
                    TableRow.LayoutParams cardViewParams = new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT, // Set layout_width to match_parent
                            TableRow.LayoutParams.WRAP_CONTENT // Set layout_height to wrap_content
                    );
                    cardViewParams.setMargins(5, 5, 2, 5); // Set the margin to 2dp

                    cview.setLayoutParams(cardViewParams);
                    cview.setPadding(2, 2, 0, 2);
                    cview.addView(linearLayout);

                    TableRow.LayoutParams trowParams = new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                    );
                    trow.setGravity(Gravity.CENTER);
                    trow.setLayoutParams(trowParams);
                    trow.addView(cview);

                    table.addView(trow);
                }     
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });

        Marksubmitbtn=findViewById(R.id.markSubmit);
//        Check and append the register number of students' attendance in present and absent arrays respectively
        Marksubmitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i;
                Attendance obj[] = new Attendance[regno.size()];
                for(i = 0; i < regno.size(); i++) {
                    int newid = Integer.parseInt(regno.get(i).substring(regno.get(i).length()-3, regno.get(i).length()));

                    CheckBox check = findViewById(newid);
                    boolean isChecked = check.isChecked();
                    String attendance = String.valueOf(isChecked);

                    obj[i] = new Attendance(regno.get(i), attendance);

                    if(isChecked){
                        present.add(regno.get(i));
                        Toast.makeText(getApplicationContext(),"Attendance: " + attendance,Toast.LENGTH_SHORT).show();
                    } else {
                        absent.add(regno.get(i));
                    }
                }

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://academichub-restapi.onrender.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                RetrofitAPI api = retrofit.create(RetrofitAPI.class);
                UpdateAttendance attend = new UpdateAttendance(getIntent().getStringExtra("id"),getIntent().getStringExtra("date"),String.join(",",present),String.join(",",absent));
                Call<Status> call = api.updateAttendance(attend);

                call.enqueue(new Callback<Status>() {
                    @Override
                    public void onResponse(Call<Status> call, Response<Status> response) {
                        if(response.body().msg.equals("success")){
                            Intent i = new Intent(getApplicationContext(), ClassRoomPage.class);
                            startActivity(i);
                            finish();
                        }
                        Log.d("Attendance",response.body().msg);
                    }

                    @Override
                    public void onFailure(Call<Status> call, Throwable t) {
                        Log.d("ErrorAttend",t.getMessage());
                    }
                });
            }
        });
    }
}
