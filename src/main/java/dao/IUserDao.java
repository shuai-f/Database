package dao;

import entity.Student;
import org.apache.ibatis.annotations.Select;

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
    @Select("select * from student")
    List<Student> findAllStudent();
}
