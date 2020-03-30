package entity;

/**
 * 教师
 */
public class Teacher {
    private int teacherID;
    private String name;
    private int age;
    private String birthday;
    private int deptID;//A teacher belongs to one dept, one dept has one or more teacher
    private int classID;//A teacher manage one class
    private String password;
    public Teacher(int teacherID, String name, int age, String birthday, int deptID, int classID, String password) {
        this.teacherID = teacherID;
        this.name = name;
        this.age = age;
        this.birthday = birthday;
        this.deptID = deptID;
        this.classID = classID;
        this.password = password;
    }
}
