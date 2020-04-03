package entity;

public class Course {
    private int courseID;
    private String courseName;
    private float credit;
    private int classroomID;// A course is given by a classroom, a classroom can give one or more course in different time period
    private int teacherID;// A course is given by a teacher

    public Course(int courseID, String courseName, float credit, int classroomID, int teacherID) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.credit = credit;
        this.classroomID = classroomID;
        this.teacherID = teacherID;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseID=" + courseID +
                ", courseName='" + courseName + '\'' +
                ", credit=" + credit +
                ", classroomID=" + classroomID +
                ", teacherID=" + teacherID +
                '}';
    }

    public Course(String courseName, float credit, int classroomID, int teacherID) {
        this.courseName = courseName;
        this.credit = credit;
        this.classroomID = classroomID;
        this.teacherID = teacherID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public float getCredit() {
        return credit;
    }

    public void setCredit(float credit) {
        this.credit = credit;
    }

    public int getClassroomID() {
        return classroomID;
    }

    public void setClassroomID(int classroomID) {
        this.classroomID = classroomID;
    }

    public int getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(int teacherID) {
        this.teacherID = teacherID;
    }
}
