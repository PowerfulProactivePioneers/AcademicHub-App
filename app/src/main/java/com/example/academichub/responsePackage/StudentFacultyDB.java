package com.example.academichub.responsePackage;

public class StudentFacultyDB {
    String id,name,uid,email,type,dept;
    char section;

    @Override
    public String toString() {
        return "StudentFacultyDB{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", uid='" + uid + '\'' +
                ", email='" + email + '\'' +
                ", type='" + type + '\'' +
                ", dept='" + dept + '\'' +
                ", section=" + section +
                '}';
    }

    public StudentFacultyDB(String id, String name, String uid, String dept, String type, String email, char section) {
        this.id = id;
        this.name = name;
        this.uid = uid;
        this.email = email;
        this.type = type;
        this.dept = dept;
        this.section = section;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public char getSection() {
        return section;
    }

    public void setSection(char section) {
        this.section = section;
    }
}
