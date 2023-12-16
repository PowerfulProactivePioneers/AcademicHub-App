package com.example.academichub;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncAdapterType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.academichub.responsePackage.Status;
import com.example.academichub.responsePackage.StudentClassRoomDB;
import com.example.academichub.responsePackage.StudentFacultyDB;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PopUpClass {
    public void showPopupWindow(final View view) {

//        Create a View object through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null, false);

//        Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

//        Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

//        Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

//        Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

//        Handler for clicking on the inactive zone of the window
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
//                        Close the window when clicked
                        popupWindow.dismiss();
                        break;
                    case MotionEvent.ACTION_UP:
                        v.performClick();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        Button btn = popupView.findViewById(R.id.join);
        TextInputEditText ip = popupView.findViewById(R.id.classroomId);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ip.getText().toString().trim().length() == 0){
                    Toast.makeText(view.getContext(), "Enter Class ID",Toast.LENGTH_SHORT).show();
                }
                else{
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://academichub-restapi.onrender.com/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    RetrofitAPI api = retrofit.create(RetrofitAPI.class);
                    SharedPreferences sh = popupView.getContext().getSharedPreferences("userData",Context.MODE_PRIVATE);
                    Call<Status> call = api.joinClass(new StudentClassRoomDB(sh.getString("id",null),ip.getText().toString()));

                    call.enqueue(new Callback<Status>() {
                        @Override
                        public void onResponse(Call<Status> call, Response<Status> response) {
                            Status st = response.body();
                            if(st.msg.equals("success")){
                                popupWindow.dismiss();
                                Toast.makeText(view.getContext(), "Joined", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(popupView.getContext(), st.msg,Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Status> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }

}
