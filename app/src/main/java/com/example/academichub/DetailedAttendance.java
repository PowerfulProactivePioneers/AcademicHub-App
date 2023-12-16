package com.example.academichub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.academichub.responsePackage.AttendanceList;

import java.io.Serializable;
import java.util.List;
public class DetailedAttendance extends AppCompatActivity {

    LinearLayout layout;

    public int dpToPx(int val){
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float) val * density);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_attendance);
        layout = findViewById(R.id.attendance_details);

        List<AttendanceList> lst = (List<AttendanceList>) getIntent().getSerializableExtra("data");
        float total = 0,present=0,absent=0;
        float percent = 0;
        for (AttendanceList item1:lst) {
            total = total + 1;
            if(item1.getAttendance())
                present = present + 1;
            else
                absent = absent + 1;
        }
        percent = total <= 0 ? 0 : ((present / total) * 100);
        CardView card10 = new CardView(getApplication());
        LinearLayout.LayoutParams params10 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,dpToPx(130));
        params10.setMargins(0,0,0,dpToPx(10));
        card10.setLayoutParams(params10);
        card10.setRadius(40.0f);
        card10.setElevation(40.0f);
        card10.setCardBackgroundColor(getColor(R.color.white));

        LinearLayout child_layout10 = new LinearLayout(getApplicationContext());
        LinearLayout.LayoutParams params110 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        child_layout10.setLayoutParams(params110);
        child_layout10.setOrientation(LinearLayout.HORIZONTAL);
        child_layout10.setWeightSum(1.0f);

        LinearLayout graph_layout = new LinearLayout(getApplicationContext());
        graph_layout.setOrientation(LinearLayout.HORIZONTAL);
        graph_layout.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams params210 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,0.35f);
        params210.setMargins(dpToPx(10),dpToPx(10),dpToPx(10),dpToPx(10));
        graph_layout.setLayoutParams(params210);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        if(Math.round(percent) < 75)
            drawable.setStroke(dpToPx(10), Color.RED);
        else
            drawable.setStroke(dpToPx(10),getResources().getColor(R.color.primary));
        drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        graph_layout.setBackground(drawable);
        TextView txt10 = new TextView(getApplicationContext());
        txt10.setText(Math.round(percent) + "%");
        txt10.setTextColor(getColor(R.color.primary));
        txt10.setTextSize(25.0f);
        graph_layout.addView(txt10);
        child_layout10.addView(graph_layout);

        LinearLayout content_layout = new LinearLayout(getApplicationContext());
        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,0.65f);
        params3.setMargins(dpToPx(10),0,0,0);
        content_layout.setLayoutParams(params3);
        content_layout.setOrientation(LinearLayout.VERTICAL);

        TextView txt110 = new TextView(getApplicationContext());
        txt110.setText(getIntent().getStringExtra("cid").toString().split("_")[0]);
        txt110.setPadding(dpToPx(5),dpToPx(5),dpToPx(5),dpToPx(5));
        txt110.setTextSize(25.0f);
        txt110.setBackgroundColor(getColor(R.color.primary));
        txt110.setTextColor(getColor(R.color.white));
        content_layout.addView(txt110);

        TextView txt210 = new TextView(getApplicationContext());
        txt210.setText("Present : "+Math.round(present));
        txt210.setTextSize(15.0f);
        txt210.setTextColor(getColor(R.color.black));
        txt210.setPadding(dpToPx(10),dpToPx(10),dpToPx(10),dpToPx(10));
        content_layout.addView(txt210);

        TextView txt310 = new TextView(getApplicationContext());
        txt310.setText("Absent : "+Math.round(absent));
        txt310.setTextSize(15.0f);
        txt310.setTextColor(getColor(R.color.black));
        txt310.setPadding(dpToPx(10),0,dpToPx(10),dpToPx(10));
        content_layout.addView(txt310);

        child_layout10.addView(content_layout);
        card10.addView(child_layout10);

        layout.addView(card10);

        int i=-1;
        AttendanceList item = null;
        for (i=0;i<=lst.size();i++) {
            if (i > 0)
                item = lst.get(i-1);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,1);
            CardView card = new CardView(getApplicationContext());
            card.setCardBackgroundColor(i==0 ? getColor(R.color.primary) : Color.WHITE);
            card.setLayoutParams(params);
            LinearLayout child_layout = new LinearLayout(getApplicationContext());
//            if(i>0)
//                child_layout.setBackground(getDrawable(R.drawable.attendance_content));
            child_layout.setLayoutParams(params);
            child_layout.setPadding(0,20,0,20);

            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,1);
            params1.weight = 0.3f;

            TextView txt = new TextView(getApplicationContext());
            txt.setText(i==0 ? "SNO" : Integer.toString(i));
            txt.setLayoutParams(params1);
            txt.setTextSize(15);
            txt.setGravity(Gravity.CENTER);
            txt.setTextColor(getColor(i==0 ? R.color.white : R.color.black));
            txt.setBackgroundColor(getColor(i==0 ? R.color.primary : R.color.white));
//            txt.setBackground(getDrawable(R.drawable.attendance_content));
            child_layout.addView(txt);

            TextView txt1 = new TextView(getApplicationContext());
            txt1.setText(i==0 ? "DATE" : item.getDate());
            txt1.setTextSize(15);
            txt1.setLayoutParams(params1);
            txt1.setGravity(Gravity.CENTER);
            txt1.setTextColor(getColor(i==0 ? R.color.white : R.color.black));
            txt1.setBackgroundColor(getColor(i==0 ? R.color.primary : R.color.white));
//            txt1.setBackground(getDrawable(R.drawable.attendance_content));
            child_layout.addView(txt1);

            TextView txt2 = new TextView(getApplicationContext());
            txt2.setText(i ==0 ? "STATUS" : item.getAttendance() ? "Present" : "Absent");
            txt2.setTextSize(15);
            if(i==0)
                txt2.setTextColor(getColor(R.color.white));
            else
                txt2.setTextColor((item.getAttendance() ? Color.GREEN : Color.RED));
            txt2.setLayoutParams(params1);
            txt2.setGravity(Gravity.CENTER);
            txt2.setBackgroundColor(getColor(i==0 ? R.color.primary : R.color.white));

//            txt2.setBackground(getDrawable(R.drawable.attendance_content));

            child_layout.addView(txt2);

            card.addView(child_layout);
            layout.addView(card);
        }
    }
}