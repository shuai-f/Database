package dao;

import entity.*;
import entity.Class;
import relation.Elect;
import relation.Manage;

import java.util.List;

/**
 * Mybatis mapper
 * 用户的持久层接口
 */
public interface IUserDao {
    /**
     * annotation
     *  @ Action(SQL)k
     */
    //student
    List<Student> findAllStudent();

    void insertStudent(Student student);

    void updateStudent(Student student);

    void deleteStudent(int stuID);

    Student findStudentbyID(int stuID);
    //"%吴%"
    Student findStudentbyName(String name);

    //teacher
    Teacher findTeacherbyID(int teacherID);

    List<Teacher> findAllTeacher();

    void insertTeacher(Teacher teacher);

    void updateTeacher(Teacher teacher);

    void deleteTeacher(int teacherID);

    //department
    Department findDepartmentbyID(int departID);

    //manage
    Manage findManagebyTeacherID(int teacherID);

    //class
    Class findClassbyID(int classID);

    List<Elect> findElectbyStuID(int stuID);

    Course findCoursebyID(int courseID);

    Classroom findClassroombyclassroomID(int classroomID);

    Teacher findTeacherbyName(String name);

    List<Student> findStudentbyClassID(int classID);

    //某学生某们课程成绩
    Elect findElectbyStuIDAndCourseID(int stuID, int courseID);

    List<Elect> findAllElect();
    //某学生已选课程id
    List<Integer> lookupElectedCourseID(int stuID);

    List<Course> findAllCourse();
    //获取所有课程id
    List<Integer> findAllCourseID();

    void insertElect(Elect elect);

    //查学院名
    String findCollegeNamebyCollegeID(int collegeID);

    List<Course> findCoursebyTeacherID(int teacherID);
}
