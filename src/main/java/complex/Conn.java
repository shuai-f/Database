package complex;

import entity.Department;

/**
 * @Classname Conn
 * @Description TODO
 * @Date 2020/4/3 2:15
 * @Created by 冯帅
 */
public class Conn {
    private int deptID;
    private int classID;
    private int teacherID;

    private Department department;

    @Override
    public String toString() {
        return "Conn{" +
                "deptID=" + deptID +
                ", classID=" + classID +
                ", teacherID=" + teacherID +
                ", department=" + department.toString() +
                '}';
    }

    public int getDeptID() {
        return deptID;
    }

    public void setDeptID(int deptID) {
        this.deptID = deptID;
    }

    public int getClassID() {
        return classID;
    }

    public void setClassID(int classID) {
        this.classID = classID;
    }

    public int getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(int teacherID) {
        this.teacherID = teacherID;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
