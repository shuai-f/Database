package entity;

public class Course {
    private int courseID;
    private String courseName;
    private int credit;
    private int classroomID;// A course is given by a classroom, a classroom can give one or more course in different time period
    private int teacherID;// A course is given by a teacher
}
