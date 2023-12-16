package com.example.academichub;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.academichub.responsePackage.Marks;
import com.example.academichub.responsePackage.Status;
import com.example.academichub.responsePackage.UpdateMark;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MarkTableActivity extends AppCompatActivity {
//    String regno[] = {"2127210501110", "2127210501111", "2127210501114","2127210501115","21217210501094", "2127210501063"};
    List<String> regno = new ArrayList<String>();
    List<Marks> students_marks= new ArrayList<Marks>();
    TableLayout table;
    Button Marksubmitbtn;
    TextView txt;
    int mark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_mark);
        table = findViewById(R.id.Table);
        txt = findViewById(R.id.mark_title);
        txt.setText(getIntent().getStringExtra("name"));
        txt.setTextColor(getResources().getColor(R.color.white));
        txt.setTextSize(25.0f);

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
                Log.d("markData",response.body().toString());
                for (int i = 0; i < regno.size(); i++) {
                    TableRow trow = new TableRow(getApplicationContext());
                    CardView cview = new CardView(getApplicationContext());
                    cview.setCardBackgroundColor(getResources().getColor(R.color.white));
                    LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                    TextView textView = new TextView(getApplicationContext());
                    EditText editText = new EditText(getApplicationContext());
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

                    LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(
                            0,// Set layout_width to 0 (will be weighted)
                            ViewGroup.LayoutParams.MATCH_PARENT // Set layout_height to match_parent
                    );
                    editParams.weight = 1; // Distribute space evenly
                    editText.setLayoutParams(editParams);

//            Adding id to the marks
                    editText.setId(i);
                    editText.setEms(8);
                    editText.setHint("Mark");
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);

//            Design stuffs for linear & table layouts
                    LinearLayout.LayoutParams linearparams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                    );
                    linearLayout.setLayoutParams(linearparams);
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
//                    linearLayout.setPadding(10,10,10,10);
                    linearLayout.addView(textView);
                    linearLayout.addView(editText);

                    // Set the layout_width and layout_height of the CardView to match_parent
                    LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, // Set layout_width to match_parent
                            LinearLayout.LayoutParams.WRAP_CONTENT // Set layout_height to wrap_content
                    );
//            cardViewParams.setMargins(5, 5, 2, 5); // Set the margin to 2dp

                    cview.setLayoutParams(cardViewParams);
//                    cview.setPadding(2, 2, 0, 2);
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
                Log.d("markData",t.getMessage());
            }
        });

        Log.d("markData","start");

        Marksubmitbtn=findViewById(R.id.markSubmit);
        Marksubmitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i;
                Log.d("regno",regno.toString());
                Marks obj[] = new Marks[regno.size()];
                for(i = 0; i < regno.size(); i++) {
                    EditText edit = findViewById(i);
                    String marks = String.valueOf(edit.getText());
                    Log.d("marks",marks);
                    mark = (marks.equalsIgnoreCase("")) ? 0 : Integer.parseInt(marks);
                    Log.d("marks",Integer.toString(marks.length()));
                    if(!marks.isEmpty())
                    {
                        if ((Integer.parseInt(marks) >= 0 && Integer.parseInt(marks) <= 100)) {
                            obj[i] = new Marks(regno.get(i), Integer.parseInt(marks));
                            Log.d("marks",obj[i].toString());
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Enter valid marks for "+regno.get(i),Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Enter valid marks for "+regno.get(i),Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                if(i == regno.size()) {
                    for (i=0;i<regno.size();i++){
                        students_marks.add(obj[i]);
                    }
                    Log.d("student_mark",students_marks.toString());
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://academichub-restapi.onrender.com/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    RetrofitAPI api = retrofit.create(RetrofitAPI.class);
                    UpdateMark umark = new UpdateMark(getIntent().getStringExtra("id"),getIntent().getStringExtra("name"),students_marks);
                    Log.d("umark",umark.toString());
                    Call<Status> call = api.updateMarks(umark);
                    call.enqueue(new Callback<Status>() {
                        @Override
                        public void onResponse(Call<Status> call, Response<Status> response) {
                            Log.d("updatemarks",response.body().toString());
                        }

                        @Override
                        public void onFailure(Call<Status> call, Throwable t) {
                            Log.d("updatemarks",t.getMessage());
                        }
                    });
                }
            }
        });
    }
}
