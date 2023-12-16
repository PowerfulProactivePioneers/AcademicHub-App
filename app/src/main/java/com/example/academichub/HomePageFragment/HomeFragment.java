package com.example.academichub.HomePageFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.academichub.ClassRoomPage;
import com.example.academichub.R;
import com.example.academichub.RetrofitAPI;
import com.example.academichub.responsePackage.ClassRoomDB;
import com.example.academichub.responsePackage.StudentFacultyDB;
import com.example.academichub.responsePackage.UserIDType;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    View view;
    LinearLayout layout;

    public int dpToPx(int val){
        float density = getContext().getResources().getDisplayMetrics().density;
        return Math.round((float) val * density);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        layout = view.findViewById(R.id.classroom_cards);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://academichub-restapi.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI api = retrofit.create(RetrofitAPI.class);
        SharedPreferences sh = getActivity().getSharedPreferences("userData", Context.MODE_PRIVATE);
        Call<List<ClassRoomDB>> call = api.findClassforUser(new UserIDType(sh.getString("id",null),sh.getString("type",null)));
        call.enqueue(new Callback<List<ClassRoomDB>>() {
            @Override
            public void onResponse(Call<List<ClassRoomDB>> call, Response<List<ClassRoomDB>> response) {
                List<ClassRoomDB> result = response.body();
                if(result.isEmpty()){
                    TextView txt = new TextView(getActivity());
                    txt.setText("No Classrooms");
                    layout.addView(txt);
                }else{
                    int i = 1;
                    for (ClassRoomDB data: result) {
                        CardView card = new CardView(getActivity());
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,dpToPx(140));
                        params.setMargins(dpToPx(10),dpToPx(10),dpToPx(10),0);
                        card.setLayoutParams(params);
                        card.setRadius(getResources().getDimension(R.dimen.card_radius));
                        LinearLayout child_layout = new LinearLayout(getActivity());
                        if(i == 1) {
                            child_layout.setBackground(getActivity().getDrawable(R.drawable.bg1));
                            i++;
                        }
                        else if(i == 2){
                            child_layout.setBackground(getActivity().getDrawable(R.drawable.bg2));
                            i = 1;
                        }
                        child_layout.setPadding(dpToPx(10),dpToPx(10),dpToPx(10),dpToPx(10));
                        child_layout.setOrientation(LinearLayout.HORIZONTAL);

                        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                        params2.weight = 0.7f;

                        LinearLayout cclayout = new LinearLayout(getActivity());
                        cclayout.setLayoutParams(params2);
                        cclayout.setOrientation(LinearLayout.VERTICAL);
                        cclayout.setPadding(dpToPx(5),dpToPx(5),dpToPx(5),dpToPx(5));
                        TextView txt = new TextView(getActivity());
                        txt.setText(data.getCcode());
                        txt.setTextColor(getActivity().getColor(R.color.white));
                        txt.setTextSize(dpToPx(8));
                        txt.setPadding(dpToPx(2),dpToPx(2),dpToPx(2),dpToPx(2));
                        cclayout.addView(txt);

                        TextView txt1 = new TextView(getActivity());
                        txt1.setText(data.getCname());
                        txt1.setTextColor(getActivity().getColor(R.color.white));
                        txt1.setTextSize(dpToPx(8));
                        txt1.setPadding(dpToPx(2),dpToPx(2),dpToPx(2),dpToPx(2));
                        cclayout.addView(txt1);

                        TextView txt2 = new TextView(getActivity());
                        txt2.setText(data.getAllowed_dept());
                        txt2.setTextColor(getActivity().getColor(R.color.white));
                        txt2.setTextSize(dpToPx(8));
                        txt2.setPadding(dpToPx(2),dpToPx(2),dpToPx(2),dpToPx(2));
                        cclayout.addView(txt2);

                        child_layout.addView(cclayout);

                        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                        params1.weight = 0.3f;
                        LinearLayout cclayout1 = new LinearLayout(getActivity());
                        cclayout1.setLayoutParams(params1);
                        ImageView img = new ImageView(getActivity());
                        img.setBackground(getActivity().getDrawable(R.drawable.stud));
                        cclayout1.addView(img);

                        child_layout.addView(cclayout1);

                        card.addView(child_layout);

                        card.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(getContext(), ClassRoomPage.class);
                                i.putExtra("name",data.getCname());
                                i.putExtra("code",data.getCcode());
                                i.putExtra("dept",data.getAllowed_dept());
                                i.putExtra("section",Character.toString(data.getAllowed_section()));
                                i.putExtra("id",data.getCid());
                                startActivity(i);
                            }
                        });

                        layout.addView(card);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ClassRoomDB>> call, Throwable t) {
                Log.d("Available Class Error",t.getMessage());
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().getFragmentManager().popBackStack();
    }
}