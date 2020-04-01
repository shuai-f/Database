package dao;

import entity.College;
import entity.Department;
import entity.Student;
import entity.Teacher;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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

    //管理表
    Manage findManagebyTeacherID(int teacherID);
}
