package entity;

import java.util.Objects;

public class Student {
    private int stuID;
    private String name;
    private String birthday;
    private String communication;
    private String email;
    private String password;
    private int classID; // A stu belongs to a class, a class has one or more students
    private int dormitoryID;// A student belongs to a domitory, a domitory has one or more students

    public void setStuID(int stuID) {
        this.stuID = stuID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setCommunication(String communication) {
        this.communication = communication;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setClassID(int classID) {
        this.classID = classID;
    }

    public void setDormitoryID(int dormitoryID) {
        this.dormitoryID = dormitoryID;
    }

    public int getStuID() {
        return stuID;
    }

    public String getName() {
        return name;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getCommunication() {
        return communication;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getClassID() {
        return classID;
    }

    public int getDormitoryID() {
        return dormitoryID;
    }

    public Student(int stuID, String name, String birthday, String communication, String email, String password, int classID, int dormitoryID) {
        this.stuID = stuID;
        this.name = name;
        this.birthday = birthday;
        this.communication = communication;
        this.email = email;
        this.password = password;
        this.classID = classID;
        this.dormitoryID = dormitoryID;
    }

    public Student() {
    }

    public Student(String name, String birthday, String communication, String email, String password, int classID, int dormitoryID) {
        this.name = name;
        this.birthday = birthday;
        this.communication = communication;
        this.email = email;
        this.password = password;
        this.classID = classID;
        this.dormitoryID = dormitoryID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return getClassID() == student.getClassID() &&
                getDormitoryID() == student.getDormitoryID() &&
                getName().equals(student.getName()) &&
                getBirthday().equals(student.getBirthday()) &&
                getCommunication().equals(student.getCommunication()) &&
                getEmail().equals(student.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getBirthday(), getCommunication(), getEmail(), getClassID(), getDormitoryID());
    }

    @Override
    public String toString() {
        return "Student{" +
                "stuID=" + stuID +
                ", name='" + name + '\'' +
                ", birthday='" + birthday + '\'' +
                ", communication='" + communication + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", classID=" + classID +
                ", dormitoryID=" + dormitoryID +
                '}';
    }
}
