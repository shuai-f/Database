package test;

import complex.Conn;
import dao.IUserDao;
import entity.Student;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.interfaces.PBEKey;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class mybatisTest {
    private InputStream in ;
    private SqlSessionFactory factory;
    private SqlSession session;
    private IUserDao userDao;

    @Before
    public void init() throws Exception{
        //1.读取配置文件
        in = Resources.getResourceAsStream("MybatisConfig.xml");
        //2.创建 SqlSessionFactory 的构建者对象
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        //3.使用构建者创建工厂对象 SqlSessionFactory
        factory = builder.build(in);
        //4.使用 SqlSessionFactory 生产 SqlSession 对象
        session = factory.openSession();
        //5.使用 SqlSession 创建 dao 接口的代理对象
        userDao = session.getMapper(IUserDao.class);
    }

    @After // 在测试方法之哦户执行资源释放
    public void destroy() throws Exception{
        session.commit();
        //释放资源
        session.close();
        in.close();
    }

    @Test
    public void testFindAllStudent(){
        List<Student> students = userDao.findAllStudent();
        for (Student student : students){
            System.out.println(student);
        }
    }

    @Test
    public void testInsertStudent(){
        Student student = new Student("李四","1999.xx.xx","12313454566","1142166063@qq.com","123456",1,1);
        userDao.insertStudent(student);
    }

    @Test
    public void testFindTeacherbyID(){
        int id = 1;
        System.out.println(userDao.findTeacherbyID(id));
    }
    @Test
    public void testFindStudentbyID(){
        int id = 1;
        System.out.println(userDao.findStudentbyID(id));
    }

    @Test
    public void  testfindStusGroupbyCourseID(){
        List<String> maps = userDao.connQuery();
        for (String map : maps) {
            System.out.println(map);
        }
    }
}
