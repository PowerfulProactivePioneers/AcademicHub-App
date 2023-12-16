package com.example.academichub.HomePageFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.academichub.LoginPage;
import com.example.academichub.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ProfilePage extends Fragment {

    View view;
    TextInputEditText txt1;
    TextView txt2,txt3,txt4,txt5;
    LinearLayout lay;
    Button btn;
    FirebaseAuth auth;
    GoogleSignInClient gsc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.profile_page, container, false);
        auth = FirebaseAuth.getInstance();
        txt1 = view.findViewById(R.id.profile_name);
        txt2 = view.findViewById(R.id.profile_dept);
        txt3 = view.findViewById(R.id.profile_section);
        txt4 = view.findViewById(R.id.profile_email);
        txt5 = view.findViewById(R.id.profile_pic);
        lay = view.findViewById(R.id.profile_section_layout);

        btn = view.findViewById(R.id.profie_signout);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1095703267421-onu1fv8ii4trrjvupc93tc0l0n0s7vtm.apps.googleusercontent.com")
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(getActivity(),gso);

        SharedPreferences getData = getContext().getSharedPreferences("userData", Context.MODE_PRIVATE);
        Log.d("userdata",getData.getAll().toString());
        txt1.setText(getData.getString("name",null));
        txt2.setText(getData.getString("dept",null));
        if(getData.getString("type",null).equals("Faculty"))
            lay.setVisibility(View.GONE);
        else
            txt3.setText(getData.getString("section",null));
        txt4.setText(getData.getString("email",null));
        txt5.setText(Character.toString(getData.getString("name",null).charAt(0)));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                gsc.signOut();
                startActivity(new Intent(getContext(), LoginPage.class));
                getActivity().finish();
            }
        });
        return view;
    }

    public void onDestroy() {
        super.onDestroy();

    }
}