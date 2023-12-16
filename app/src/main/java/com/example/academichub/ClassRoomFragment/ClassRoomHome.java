package com.example.academichub.ClassRoomFragment;

import static android.content.Intent.getIntent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.academichub.PostPage;
import com.example.academichub.R;
import com.example.academichub.RetrofitAPI;
import com.example.academichub.responsePackage.Post;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClassRoomHome extends Fragment {

    View view;
    LinearLayout post_layout;
    FloatingActionButton fab;
    TextView code,name,dept;
    
    public ClassRoomHome() {
    }

    public int dpToPx(int val){
        float density = getContext().getResources().getDisplayMetrics().density;
        return Math.round((float) val * density);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_class_room_home, container, false);
        post_layout = view.findViewById(R.id.class_post);
        fab = view.findViewById(R.id.post_page);
        SharedPreferences sh = getContext().getSharedPreferences("userData", Context.MODE_PRIVATE);

        code = view.findViewById(R.id.classroom_page_code);
        name = view.findViewById(R.id.classroom_page_cname);
        dept = view.findViewById(R.id.classroom_page_dept);

        code.setText(getActivity().getIntent().getStringExtra("code"));
        name.setText(getActivity().getIntent().getStringExtra("name"));
        dept.setText(getActivity().getIntent().getStringExtra("dept"));
        if(!getActivity().getIntent().getStringExtra("dept").equals("ALL")){
            if(!getActivity().getIntent().getStringExtra("section").equals("N")){
                dept.setText(getActivity().getIntent().getStringExtra("dept")+" - "+getActivity().getIntent().getStringExtra("section"));
            }
        }


        if(sh.getString("type",null).equals("Student")){
            fab.setVisibility(View.GONE);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), PostPage.class);
                i.putExtra("cid",getActivity().getIntent().getStringExtra("id"));
                startActivity(i);
            }
        });
        
        
        
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://academichub-restapi.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI api = retrofit.create(RetrofitAPI.class);
        Call<List<Post>> call = api.getPost(getActivity().getIntent().getStringExtra("id"));
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                List<Post> post_data = response.body();
                Log.d("class Data", post_data.toString());
                if(post_data.isEmpty()){

                }
                else{
                    for (Post data:post_data) {
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(dpToPx(10),dpToPx(10),dpToPx(10),dpToPx(10));
                        CardView card = new CardView(getContext());
                        card.setCardBackgroundColor(getResources().getColor(R.color.white));
                        card.setRadius(dpToPx(15));
                        card.setLayoutParams(params);

                        // Linear Layout
                        LinearLayout layout = new LinearLayout(getContext());
                        layout.setOrientation(LinearLayout.VERTICAL);

                        // User Profile
                        TextView txt = new TextView(getContext());
                        txt.setText(sh.getString("name",null));
                        Drawable leftDraw = getContext().getResources().getDrawable(R.drawable.user);
                        txt.setCompoundDrawablesWithIntrinsicBounds(leftDraw,null,null,null);
                        txt.setCompoundDrawablePadding(dpToPx(5));
                        txt.setPadding(dpToPx(10),dpToPx(10),dpToPx(10),dpToPx(10));
                        txt.setBackgroundColor(getResources().getColor(R.color.primary));
                        txt.setTextColor(getResources().getColor(R.color.white));

                        //Content
                        TextView txt1 = new TextView(getContext());
                        txt1.setText(data.getTitle());
                        txt1.setTextSize(dpToPx(8));
                        txt1.setPadding(dpToPx(10),dpToPx(10),dpToPx(10),0);
                        txt1.setTextColor(getResources().getColor(R.color.black));

                        TextView txt2 = new TextView(getContext());
                        txt2.setText(data.getDesc());
                        txt2.setTextSize(dpToPx(5));
                        txt2.setPadding(dpToPx(10),dpToPx(10),dpToPx(10),dpToPx(10));
                        txt2.setTextColor(getResources().getColor(R.color.black));


                        layout.addView(txt);
                        layout.addView(txt1);
                        layout.addView(txt2);
                        card.addView(layout);
                        post_layout.addView(card);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.d("Class Post",t.getMessage());
            }
        });
        return view;
    }
}