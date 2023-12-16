package com.example.academichub;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.example.academichub.responsePackage.Post;
import com.example.academichub.responsePackage.Status;
import com.example.academichub.responsePackage.StudentFacultyDB;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostPage extends AppCompatActivity {

    Button btn,post_btn;
    Switch switch_btn;
    String cid;

    List urls = new ArrayList<String>();

    TextInputEditText title,desc,due_date;
    TextInputLayout due_date_layout;
    LinearLayout filelayout;

    StorageReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_page);

        cid = getIntent().getStringExtra("cid");
        filelayout = findViewById(R.id.post_files);
        btn = findViewById(R.id.post_upload);
        switch_btn = findViewById(R.id.post_assignment);
        title = findViewById(R.id.post_title);
        desc = findViewById(R.id.post_desc);
        due_date = findViewById(R.id.due_date_input);
        due_date_layout = findViewById(R.id.due_date_layout);
        due_date_layout.setVisibility(View.GONE);
        post_btn = findViewById(R.id.post);

        ref = FirebaseStorage.getInstance().getReference();

        switch_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    due_date_layout.setVisibility(View.VISIBLE);
                }
                else{
                    due_date_layout.setVisibility(View.GONE);
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i,"Select your file"),200);
            }
        });

        post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tit = title.getText().toString();
                String des = desc.getText().toString();
                Boolean assign = switch_btn.isChecked();
                String due = due_date.getText().toString();

                if(tit.trim().length() == 0){
                    Toast.makeText(getApplicationContext(),"Enter the title",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(des.trim().length() == 0){
                    Toast.makeText(getApplicationContext(),"Enter the description",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(assign && due.trim().length() == 0){
                    Toast.makeText(getApplicationContext(),"Enter the Due date",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!assign){
                    due = "N/A";
                }
                Post post = new Post(tit,des,String.join(",",urls),cid,due,assign);
                Log.d("FileDate",post.toString());
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.0.107:8080/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                RetrofitAPI api = retrofit.create(RetrofitAPI.class);
                Call<Status> call = api.createPost(post);

                call.enqueue(new Callback<Status>() {
                    @Override
                    public void onResponse(Call<Status> call, Response<Status> response) {
                        Status st = response.body();
                        if(st.msg.equals("success"))
                            startActivity(new Intent(getApplicationContext(), ClassRoomPage.class));
                        else
                            Toast.makeText(getApplicationContext(),st.msg,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Status> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("FileData","Start");
        ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Uploading...");
        progress.show();
        if(requestCode == 200 && resultCode == RESULT_OK){
            Uri uri = data.getData();
            Cursor returnCursor = null;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                returnCursor = getContentResolver().query(uri,null,null,null);
            }
            int name = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            returnCursor.moveToFirst();
            String file_name = returnCursor.getString(name);
            StorageReference sref = ref.child(cid+"/"+returnCursor.getString(name));
            sref.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriResult = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriResult.isComplete());
                            //Firebase Url
                            Uri url = uriResult.getResult();
                            urls.add(url.toString());
                            // Close Progress Dialog
                            progress.cancel();

                            LinearLayout llayout = new LinearLayout(getApplicationContext());
                            llayout.setOrientation(LinearLayout.HORIZONTAL);

                            Button btn = new Button(getApplicationContext());
                            Drawable draw = getApplicationContext().getResources().getDrawable(R.drawable.pdf);
                            btn.setCompoundDrawablesWithIntrinsicBounds(draw,null,null,null);
                            btn.setText(file_name);
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Log.d("FileData","Click");
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setDataAndType(url,"*/*");
                                    startActivity(i);
                                }
                            });
                            llayout.addView(btn);

                            filelayout.addView(llayout);
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double temp = (100.0*snapshot.getBytesTransferred())/ snapshot.getTotalByteCount();
                            progress.setMessage("Uploaded: "+(int)temp+"%");
                        }
                    });
        }
    }
}