package com.example.academichub.responsePackage;

public class Attendance {
    String id, attendance;

    public String getId() {
        return id;
    }

    public String getAttendance() {
        return attendance;
    }

    public Attendance(String id, String attendance) {
        this.id = id;
        this.attendance = attendance;
    }
}
