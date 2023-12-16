package com.example.academichub;

import com.example.academichub.responsePackage.AttendanceList;
import com.example.academichub.responsePackage.AttendanceReport;
import com.example.academichub.responsePackage.ClassRoomDB;
import com.example.academichub.responsePackage.Post;
import com.example.academichub.responsePackage.Status;
import com.example.academichub.responsePackage.StudentClassRoomDB;
import com.example.academichub.responsePackage.StudentFacultyDB;
import com.example.academichub.responsePackage.StudentMark;
import com.example.academichub.responsePackage.UpdateAttendance;
import com.example.academichub.responsePackage.UpdateMark;
import com.example.academichub.responsePackage.UserIDType;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface RetrofitAPI {

    @Headers("Content-Type:application/json")
    @POST("/getuser")
    Call<StudentFacultyDB> getUser(@Body String email);

    @Headers("Content-Type: application/json")
    @POST("/create-user")
    Call<Status> createUser(@Body StudentFacultyDB data);

    @Headers("Content-Type: application/json")
    @POST("/create-classroom")
    Call<Status> createClass(@Body ClassRoomDB data);

    @Headers("Content-Type: application/json")
    @POST("/join-classroom")
    Call<Status> joinClass(@Body StudentClassRoomDB data);

    @Headers("Content-Type: application/json")
    @POST("/get-user-classrooms")
    Call<List<ClassRoomDB>> findClassforUser(@Body UserIDType data);

    @Headers("Content-Type: application/json")
    @POST("/create-post")
    Call<Status> createPost(@Body Post data);

    @Headers("Content-Type: application/json")
    @POST("/get-post")
    Call<List<Post>> getPost(@Body String data);

    @Headers("Content-Type: application/json")
    @POST("/get-students-list")
    Call<List<String>> getStudentList(@Body String data);

    @Headers("Content-Type: application/json")
    @POST("/update-marks")
    Call<Status> updateMarks(@Body UpdateMark data);

    @Headers("Content-Type: application/json")
    @POST("/update-attendance")
    Call<Status> updateAttendance(@Body UpdateAttendance data);

    @Headers("Content-Type: application/json")
    @POST("/get-people")
    Call<List<String>> getPeople(@Body String data);

    @Headers("Content-Type: application/json")
    @POST("/student-attendance")
    Call<List<AttendanceList>> getClassWiseAttendance(@Body StudentMark data);

    @Headers("Content-Type: application/json")
    @POST("/student-all-attendance")
    Call<List<AttendanceReport>> getAllAttendace(@Body String data);

}
