package relation;

import entity.Teacher;

/**
 * @Classname Manage
 * @Description 教师管理班级
 * @Date 2020/3/30 22:43
 * @Created by 冯帅
 */
public class Manage {
    private int manageID;
    private int teacherID;
    private int classID;

    public Manage(int manageID, int teacherID, int classID) {
        this.manageID = manageID;
        this.teacherID = teacherID;
        this.classID = classID;
    }

    public Manage(int teacherID, int classID) {
        this.teacherID = teacherID;
        this.classID = classID;
    }

    public int getManageID() {
        return manageID;
    }

    public void setManageID(int manageID) {
        this.manageID = manageID;
    }

    public int getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(int teacherID) {
        this.teacherID = teacherID;
    }

    public int getClassID() {
        return classID;
    }

    public void setClassID(int classID) {
        this.classID = classID;
    }
}
