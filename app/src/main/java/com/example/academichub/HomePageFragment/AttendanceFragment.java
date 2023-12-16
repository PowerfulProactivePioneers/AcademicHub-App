package com.example.academichub.HomePageFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.academichub.DetailedAttendance;
import com.example.academichub.R;
import com.example.academichub.RetrofitAPI;
import com.example.academichub.responsePackage.AttendanceList;
import com.example.academichub.responsePackage.AttendanceReport;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AttendanceFragment extends Fragment {
    
    View view;
    LinearLayout parent_layout;

    public int dpToPx(int val){
        float density = getContext().getResources().getDisplayMetrics().density;
        return Math.round((float) val * density);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_attendance, container, false);

        parent_layout = view.findViewById(R.id.attendance_cards);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://academichub-restapi.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI api = retrofit.create(RetrofitAPI.class);
        SharedPreferences sh = getActivity().getSharedPreferences("userData", Context.MODE_PRIVATE);
        Log.d("userData",sh.getString("id",null));
        Call<List<AttendanceReport>> call = api.getAllAttendace(sh.getString("id",null));

        call.enqueue(new Callback<List<AttendanceReport>>() {
            @Override
            public void onResponse(Call<List<AttendanceReport>> call, Response<List<AttendanceReport>> response) {
                Log.d("userData",response.body().toString());
                float total = 0,present=0,absent=0;
                float percent = 0;
                for (AttendanceReport item: response.body()) {
                    total = 0;
                    present = 0;
                    absent = 0;
                    percent = 0;
                    List<AttendanceList> lst = item.getLst();
                    for (AttendanceList item1:lst) {
                        total = total + 1;
                        if(item1.getAttendance())
                            present = present + 1;
                        else
                            absent = absent + 1;
                    }
                    percent = total <= 0 ? 0 : ((present / total) * 100);
                    CardView card = new CardView(getContext());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,dpToPx(130));
                    params.setMargins(dpToPx(10),dpToPx(10),dpToPx(10),dpToPx(10));
                    card.setLayoutParams(params);
                    card.setRadius(40.0f);
                    card.setElevation(40.0f);
                    card.setCardBackgroundColor(getActivity().getColor(R.color.white));

                    LinearLayout child_layout = new LinearLayout(getContext());
                    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    child_layout.setLayoutParams(params1);
                    child_layout.setOrientation(LinearLayout.HORIZONTAL);
                    child_layout.setWeightSum(1.0f);

                    LinearLayout graph_layout = new LinearLayout(getContext());
                    graph_layout.setOrientation(LinearLayout.HORIZONTAL);
                    graph_layout.setGravity(Gravity.CENTER);
                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,0.35f);
                    params2.setMargins(dpToPx(10),dpToPx(10),dpToPx(10),dpToPx(10));
                    graph_layout.setLayoutParams(params2);
                    GradientDrawable drawable = new GradientDrawable();
                    drawable.setShape(GradientDrawable.OVAL);
                    if(Math.round(percent) < 75)
                        drawable.setStroke(dpToPx(10), Color.RED);
                    else
                        drawable.setStroke(dpToPx(10),getResources().getColor(R.color.primary));
                    drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
                    graph_layout.setBackground(drawable);
                    TextView txt = new TextView(getContext());
                    txt.setText(Math.round(percent) + "%");
                    txt.setTextColor(getActivity().getColor(R.color.primary));
                    txt.setTextSize(25.0f);
                    graph_layout.addView(txt);
                    child_layout.addView(graph_layout);

                    LinearLayout content_layout = new LinearLayout(getContext());
                    LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,0.65f);
                    params3.setMargins(dpToPx(10),0,0,0);
                    content_layout.setLayoutParams(params3);
                    content_layout.setOrientation(LinearLayout.VERTICAL);

                    TextView txt1 = new TextView(getContext());
                    txt1.setText(item.getCid().split("_")[0]);
                    txt1.setPadding(dpToPx(5),dpToPx(5),dpToPx(5),dpToPx(5));
                    txt1.setTextSize(25.0f);
                    txt1.setBackgroundColor(getActivity().getColor(R.color.primary));
                    txt1.setTextColor(getActivity().getColor(R.color.white));
                    content_layout.addView(txt1);

                    TextView txt2 = new TextView(getContext());
                    txt2.setText("Present : "+Math.round(present));
                    txt2.setTextSize(15.0f);
                    txt2.setTextColor(getActivity().getColor(R.color.black));
                    txt2.setPadding(dpToPx(10),dpToPx(10),dpToPx(10),dpToPx(10));
                    content_layout.addView(txt2);

                    TextView txt3 = new TextView(getContext());
                    txt3.setText("Absent : "+Math.round(absent));
                    txt3.setTextSize(15.0f);
                    txt3.setTextColor(getActivity().getColor(R.color.black));
                    txt3.setPadding(dpToPx(10),0,dpToPx(10),dpToPx(10));
                    content_layout.addView(txt3);

                    child_layout.addView(content_layout);
                    card.addView(child_layout);

                    card.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(getContext(), DetailedAttendance.class);
                            i.putExtra("data",(Serializable) item.getLst());
                            i.putExtra("cid",item.getCid().split("_")[0]);
                            startActivity(i);
                        }
                    });
                    parent_layout.addView(card);
                }
            }

            @Override
            public void onFailure(Call<List<AttendanceReport>> call, Throwable t) {
                Log.d("userData",t.getMessage());
            }
        });
        
        
        return view;
    }
}