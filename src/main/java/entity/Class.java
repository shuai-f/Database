package entity;
/**
 * 班级
 */
public class Class {
    private int classID;
    private int deptID;//A class belongs to one dept, one dept has one or more class
    private int teacherID;//A class has a teacher

    public int getClassID() {
        return classID;
    }

    public int getDeptID() {
        return deptID;
    }

    public int getTeacherID() {
        return teacherID;
    }

    public void setClassID(int classID) {
        this.classID = classID;
    }

    public void setDeptID(int deptID) {
        this.deptID = deptID;
    }

    public void setTeacherID(int teacherID) {
        this.teacherID = teacherID;
    }

    @Override
    public String toString() {
        return "Class{" +
                "classID=" + classID +
                ", deptID=" + deptID +
                ", teacherID=" + teacherID +
                '}';
    }

    public Class(int classID, int deptID, int teacherID) {
        this.classID = classID;
        this.deptID = deptID;
        this.teacherID = teacherID;
    }

    public Class(int deptID, int teacherID) {
        this.deptID = deptID;
        this.teacherID = teacherID;
    }
}
