package entity;

import org.junit.Test;

import java.util.Objects;

/**
 * 教师
 */
public class Teacher {
    private int teacherID;
    private String name;
    private String communication;
    private String email;
    private String birthday;
    private int deptID;//A teacher belongs to one dept, one dept has one or more teacher
    private String password;

    public Teacher(String name, String communication, String email, String birthday, int deptID, String password) {
        this.name = name;
        this.communication = communication;
        this.email = email;
        this.birthday = birthday;
        this.deptID = deptID;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return getDeptID() == teacher.getDeptID() &&
                getName().equals(teacher.getName()) &&
                getCommunication().equals(teacher.getCommunication()) &&
                getEmail().equals(teacher.getEmail()) &&
                getBirthday().equals(teacher.getBirthday());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getCommunication(), getEmail(), getBirthday(), getDeptID());
    }

    public Teacher(int teacherID, String name, String communication, int deptID, String email, String birthday, String password) {
        this.teacherID = teacherID;
        this.name = name;
        this.communication = communication;
        this.email = email;
        this.birthday = birthday;
        this.deptID = deptID;
        this.password = password;
    }

    public String getCommunication() {
        return communication;
    }

    public void setCommunication(String communication) {
        this.communication = communication;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public int getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(int teacherID) {
        this.teacherID = teacherID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getDeptID() {
        return deptID;
    }

    public void setDeptID(int deptID) {
        this.deptID = deptID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        return "Teacher{" +
                "teacherID=" + teacherID +
                ", name='" + name + '\'' +
                ", communication='" + communication + '\'' +
                ", email='" + email + '\'' +
                ", birthday='" + birthday + '\'' +
                ", deptID=" + deptID +
                ", password='" + password + '\'' +
                '}';
    }
}
