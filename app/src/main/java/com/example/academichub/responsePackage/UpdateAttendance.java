package com.example.academichub.responsePackage;

public class UpdateAttendance {
    private String cid,date,present,absent;


    public UpdateAttendance(String cid, String date, String present, String absent) {
        this.cid = cid;
        this.date = date;
        this.present = present;
        this.absent = absent;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPresent() {
        return present;
    }

    public void setPresent(String present) {
        this.present = present;
    }

    public String getAbsent() {
        return absent;
    }

    public void setAbsent(String absent) {
        this.absent = absent;
    }

    @Override
    public String toString() {
        return "UpdateAttendance{" +
                "date='" + date + '\'' +
                ", present='" + present + '\'' +
                ", absent='" + absent + '\'' +
                '}';
    }
}
