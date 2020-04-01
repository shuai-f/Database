package entity;
/**
 * 班级
 */
public class Class {
    private int classID;
    private int deptID;//A class belongs to one dept, one dept has one or more class
    private int teacherID;//A class has a teacher

    public void setClassID(int classID) {
        this.classID = classID;
    }

    public void setDeptID(int deptID) {
        this.deptID = deptID;
    }

    public void setTeacherID(int teacherID) {
        this.teacherID = teacherID;
    }

    public Class( int deptID, int teacherID) {
        this.deptID = deptID;
        this.teacherID = teacherID;
    }
}
