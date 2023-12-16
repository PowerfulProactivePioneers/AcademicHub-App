package com.example.academichub.ClassRoomFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.academichub.R;
import com.example.academichub.RetrofitAPI;
import com.example.academichub.responsePackage.Status;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PeopleClassRoom extends Fragment {

    View view;
    ListView simpleList,teacherlist;

//    String StudentsName[] = {"Praveen", "Logesh", "Pratheek", "Mathivanan", "Prasanna", "Varathan"};
    List<String> names;
    List<String> teachersName;
    List<Character> profile;
//    Character profile[]=new Character[StudentsName.length];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.people_class_room, container, false);
        String cls_id = getActivity().getIntent().getStringExtra("id");

        profile = new ArrayList<Character>();
        teachersName = new ArrayList<String>();
        simpleList = view.findViewById(R.id.StudentList);
        teacherlist= view.findViewById(R.id.Teacherlist);

        // CALL API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://academichub-restapi.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI api = retrofit.create(RetrofitAPI.class);
        Call<List<String>> call = api.getPeople(cls_id);

        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                names = response.body();

                for(int i=0;i<names.size();i++)
                {
                    profile.add(names.get(i).charAt(0));
                }

                Log.d("namelist",names.toString());

                String teacherName = names.get(names.size()-1);
                Log.d("namelist",names.get(names.size()-1));
                teachersName.add(names.get(names.size()-1));
                names.remove(names.size()-1);

                ArrayAdapter<String> array=new ArrayAdapter<String >(getContext(),R.layout.activity_listview,R.id.StudentListName,teachersName) {
                    @Override
                    public View getView(int positions, View convertViews, ViewGroup parents) {
                        View view = super.getView(positions, convertViews, parents);
                        TextView profileLetterTextView = view.findViewById(R.id.StudentProfile);
                        profileLetterTextView.setText(String.valueOf(teacherName.charAt(0)));
                        return view;

                    }
                };

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.activity_listview, R.id.StudentListName, names) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView profileLetterTextView = view.findViewById(R.id.StudentProfile);
                        profileLetterTextView.setText(String.valueOf(profile.get(position)));
                        return view;
                    }
                };
                teacherlist.setAdapter(array);
                simpleList.setAdapter(arrayAdapter);
                simpleList.setDivider(null);
                teacherlist.setDivider(null);

            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });


        return view;
    }
}