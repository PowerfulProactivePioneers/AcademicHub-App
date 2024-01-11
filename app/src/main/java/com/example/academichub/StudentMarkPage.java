package com.example.academichub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.example.academichub.responsePackage.MarkSchema;
import com.example.academichub.responsePackage.StudentFacultyDB;
import com.example.academichub.responsePackage.StudentMark;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StudentMarkPage extends AppCompatActivity {

    // variable for our bar chart
    BarChart barChart;

    // variable for our bar data.
    BarData barData;

    // variable for our bar data set.
    BarDataSet barDataSet;

    // array list for storing entries.
    ArrayList barEntriesArrayList;

    List<MarkSchema> marks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_mark_page);

        fetchDetails();

        // initializing variable for bar chart.
        barChart = findViewById(R.id.idBarChart);

        // calling method to get bar entries.
        getBarEntries();

        // creating a new bar data set.
        barDataSet = new BarDataSet(barEntriesArrayList,null);

        // creating a new bar data and
        // passing our bar data set.
        barData = new BarData(barDataSet);
        barData.setBarWidth(0.5f);

        // below line is to set data
        // to our bar chart.
        barChart.setData(barData);

        // adding color to our bar data set.
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        // setting text color.
        barDataSet.setValueTextColor(Color.BLACK);

        // setting text size
        List<String> categories = new ArrayList<>();
        categories.add("");
        categories.add("Mark");
        categories.add("Average");
        categories.add("Top Mark");
        barDataSet.setValueTextSize(16f);
        barChart.getDescription().setEnabled(false);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(categories));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    }

    private void getBarEntries() {
        // creating a new array list
        barEntriesArrayList = new ArrayList<>();
        // adding new entry to our array list with bar
        // entry and passing x and y axis value to it.
        barEntriesArrayList.add(new BarEntry(1f, marks.get(0).getCat1()));
        barEntriesArrayList.add(new BarEntry(2f, marks.get(1).getCat1()));
        barEntriesArrayList.add(new BarEntry(3f, marks.get(2).getCat1()));
    }

    private void fetchDetails(){
        String rno = getSharedPreferences("userData", Context.MODE_PRIVATE).getString("id",null);
        String cid = getIntent().getStringExtra("cid");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://academichub-restapi.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI api = retrofit.create(RetrofitAPI.class);
        StudentMark obj = new StudentMark(rno,cid);
        Call<List<MarkSchema>> call = api.getStudentMarks(obj);

        call.enqueue(new Callback<List<MarkSchema>>() {
            @Override
            public void onResponse(Call<List<MarkSchema>> call, Response<List<MarkSchema>> response) {
                marks = response.body();
            }

            @Override
            public void onFailure(Call<List<MarkSchema>> call, Throwable t) {
                Log.d("ErrorAPI",t.getMessage());
            }
        });


    }
}