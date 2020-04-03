package ui;

import com.sun.javafx.sg.prism.web.NGWebView;
import com.sun.org.apache.xerces.internal.util.EntityResolverWrapper;
import com.sun.xml.internal.bind.v2.model.core.ID;
import dao.IUserDao;
import entity.*;
import entity.Class;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import relation.Elect;
import relation.Manage;
import sun.awt.geom.AreaOp;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Savepoint;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import java.util.function.IntConsumer;

/**
 * @Classname UI
 * @Description TODO
 * @Date 2020/3/30 17:52
 * @Created by 冯帅
 */
public class UI {

    private InputStream in ;
    private SqlSessionFactory factory;
    private SqlSession session;
    private IUserDao userDao;
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public UI() {
        init();
    }

    public void init() {
        try {
            //1.读取配置文件
            in = Resources.getResourceAsStream("MybatisConfig.xml");
            //2.创建 SqlSessionFactory 的构建者对象
            SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
            //3.使用构建者创建工厂对象 SqlSessionFactory
            factory = builder.build(in);
            //4.使用 SqlSessionFactory 生产 SqlSession 对象
            session = factory.openSession(true);
            //5.使用 SqlSession 创建 dao 接口的代理对象
            userDao = session.getMapper(IUserDao.class);
        } catch (IOException e) {
            System.out.println("配置文件读取异常");
        }

    }

    /**
     * 主界面
     */
    public void  mainFrame(){
        JFrame frame = new JFrame("主界面");
        frame.setSize(600,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation( (screenSize.width - frame.getWidth()) / 2,
                (screenSize.height - frame.getHeight()) / 2);
        // 创建面板
        JPanel panel = new JPanel();
        panel.setLayout(null);    // 面板布局
        initGlobalFontSetting(24);
        //创建 标签 & 输入框 & 按钮
        JLabel userLabel = new JLabel("工号:");
        JLabel passwordLabel = new JLabel("密码:");
        JLabel identityLabel = new JLabel("身份:");
        JTextField userNameText = new JTextField(20);
        JPasswordField userPasswordText = new JPasswordField(20);
        userPasswordText.setEchoChar('*');
        //JTextField identityText = new JTextField(20);
        JButton loginButton = new JButton("登录");
        JButton registerButton = new JButton("注册");

        // 设置标签的大小和位置
        int x,y,width,height = 30;
        x = 200;
        y = 100;
        int alias = 80;
        userLabel.setBounds(x, y+20, 80, height);
        passwordLabel.setBounds(x, y+50, 80, height);
        identityLabel.setBounds(x, y+80, 80, height);

        userNameText.setBounds(x+alias, y+20, 165, height);
        userPasswordText.setBounds(x+alias, y+50, 165, height);
        //identityText.setBounds(x+alias, y+80, 165, height);

        loginButton.setBounds(240, y+110, 100, height);
        registerButton.setBounds(240, y+140, 100, height);

        JComboBox comBox = new JComboBox();//下拉列表
        comBox.addItem("学生");
        comBox.addItem("老师");
        comBox.addItem("管理员");
        comBox.setBounds(x+alias, y+80, 165, height);
        panel.add(comBox);

        // 设置面板内容
        panel.add(userLabel);
        panel.add(userNameText);
        panel.add(passwordLabel);
        panel.add(userPasswordText);
        panel.add(identityLabel);
        //panel.add(identityText);
        panel.add(loginButton);
        panel.add(registerButton);

        // 将面板加入到窗口中
        frame.add(panel);

        // 按钮的监听事件
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = String.valueOf(userPasswordText.getPassword());
                if (comBox.getSelectedItem().toString().equals("管理员")) {
                    String admin = userNameText.getText();
                    if (admin.equals("root") && password.equals(userDao.findPasswordbyAdmin(admin))) {
                        JOptionPane.showMessageDialog(panel, "登陆成功", "提示", JOptionPane.WARNING_MESSAGE);
                        frame.dispose();
                        AdminUI adminUI = new AdminUI();
                        adminUI.adminPage();
                        return;
                    } else {
                        JOptionPane.showMessageDialog(panel, "学工号或密码错误", "提示", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                if (userNameText.getText().matches("^[1-9]\\d*$")) {
                    // 检测身份
                    int userID = Integer.parseInt(userNameText.getText());
                    System.out.println(password);
                    if (comBox.getSelectedItem().toString().equals("老师")) {
                        Teacher teacher = userDao.findTeacherbyID(userID);
                        System.out.println(teacher);
                        // 查找该用户
                        if (teacher.getPassword().equals(password)) {
                            JOptionPane.showMessageDialog(panel, "登陆成功", "提示", JOptionPane.WARNING_MESSAGE);
                            frame.dispose();
                            individualPage("teacher", teacher);
                        }
                    } else if (comBox.getSelectedItem().toString().equals("学生")) {
                        // 查找该用户
                        Student student = userDao.findStudentbyID(userID);
                        System.out.println(student);
                        if (student.getPassword().equals(password)) {
                            JOptionPane.showMessageDialog(panel, "登录成功", "提示", JOptionPane.WARNING_MESSAGE);
                            frame.dispose();
                            individualPage("student", student);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(panel, "学工号或密码错误", "提示", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // 注册按钮的监听事件
        registerButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                choosePage();
            }
        });

        frame.setVisible(true);
    }

    /**
     * 修改全局字体
     */
    public void initGlobalFontSetting(int size){
        //Font
        Font font = new Font("Dialog", Font.PLAIN, size);
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, font);
            }
        }
    }

    /**
     * 个人主页
     */
    private void individualPage(String userType, Object object) {
        JFrame frame = new JFrame("个人信息页");
        frame.setSize(800,800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation( (screenSize.width - frame.getWidth()) / 2,
                (screenSize.height - frame.getHeight()) / 2);

        initGlobalFontSetting(24);

        JPanel panel = new JPanel();
        JLabel idLabel = new JLabel("工号:");
        JLabel nameLabel = new JLabel("姓名：");
        JLabel emailLabel = new JLabel("邮箱：");
        JLabel communicationLabel = new JLabel("联系方式：");
        JLabel birthdayLabel = new JLabel("出生日期：");
        JLabel deptLabel = new JLabel("所属系：");
        JLabel collegeLabel = new JLabel("所属学院：");
        JLabel classLabel = new JLabel("班级号：");
        JLabel dormitoryLabel = new JLabel("寝室号：");

        int columns = 30;
        JTextField idTextField = new JTextField(columns);
        JTextField nameTextField = new JTextField(columns);
        JTextField emailTextField = new JTextField(columns);
        JTextField communicationTextField = new JTextField(columns);
        JTextField birthdayTextField = new JTextField(columns);
        JTextField deptTextField = new JTextField(columns);
        JTextField collegeTextField = new JTextField(columns);
        JTextField classTextField = new JTextField(columns);
        JTextField dormitoryTextField = new JTextField(columns);

        idTextField.setEnabled(false);
        deptTextField.setEnabled(false);
        collegeTextField.setEnabled(false);
        classTextField.setEnabled(false);
        dormitoryTextField.setEnabled(false);


        panel.setLayout(null);
        int xLabel = 200,y = 100,alice = 60;
        int xText = 400;
        int width = 250,height = 40;
        panel.add(idLabel);idLabel.setBounds(xLabel,y,width,height);panel.add(idTextField);idTextField.setBounds(xText,y,width,height);
        panel.add(nameLabel);nameLabel.setBounds(xLabel,y+alice,width,height);panel.add(nameTextField);nameTextField.setBounds(xText,y+alice,width,height);
        panel.add(emailLabel);emailLabel.setBounds(xLabel,y+alice*2,width,height);panel.add(emailTextField);emailTextField.setBounds(xText,y+alice*2,width,height);
        panel.add(communicationLabel);communicationLabel.setBounds(xLabel,y+alice*3,width,height);panel.add(communicationTextField);communicationTextField.setBounds(xText,y+alice*3,width,height);
        panel.add(birthdayLabel);birthdayLabel.setBounds(xLabel,y+alice*4,width,height);panel.add(birthdayTextField);birthdayTextField.setBounds(xText,y+alice*4,width,height);
        panel.add(deptLabel);deptLabel.setBounds(xLabel,y+alice*5,width,height);panel.add(deptTextField);deptTextField.setBounds(xText,y+alice*5,width,height);
        panel.add(collegeLabel);collegeLabel.setBounds(xLabel,y+alice*6,width,height);panel.add(collegeTextField);collegeTextField.setBounds(xText,y+alice*6,width,height);

        //deal
        if (userType.equals("teacher")){
            Teacher teacher = (Teacher)object;
            idTextField.setText(teacher.getTeacherID()+"");
            nameTextField.setText(teacher.getName());
            emailTextField.setText(teacher.getEmail());
            communicationTextField.setText(teacher.getCommunication());
            birthdayTextField.setText(teacher.getBirthday());
            //学院号需要多表查询，
            Department department = userDao.findDepartmentbyID(teacher.getDeptID());
            deptTextField.setText("["+department.getDepartID()+"] "+department.getName());
            collegeTextField.setText("["+department.getCollegeID()+"] "+userDao.findCollegeNamebyCollegeID(department.getCollegeID()));
            Manage manage = userDao.findManagebyTeacherID(teacher.getTeacherID());
            if(manage!=null){
                JLabel manageClassLabel = new JLabel("管理班：");
                JTextField manageClassTextField = new JTextField();
                manageClassTextField.setText(manage.getClassID()+"");
                panel.add(manageClassLabel);manageClassLabel.setBounds(xLabel,y+alice*7,width,height);panel.add(manageClassTextField);manageClassTextField.setBounds(xText,y+alice*7,width,height);
                JButton viewClass = new JButton("查看班级");
                panel.add(viewClass);viewClass.setBounds(xText,y+alice*10,width,height);

                viewClass.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        viewClass(manage.getClassID());
                    }
                });
            }
            JButton viewTeachCourse = new JButton("查看任课表"); //查看任课信息
            panel.add(viewTeachCourse);viewTeachCourse.setBounds(100,y+alice*10,width,height);
            viewTeachCourse.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    viewTeachCourse(teacher);
                }
            });
            JButton button = new JButton("保存");
            panel.add(button);button.setBounds(100,y+alice*9,width,height);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String name = nameTextField.getText();
                    String birthday = birthdayTextField.getText();
                    String email = emailTextField.getText();
                    if(name.equals(teacher.getName())
                            && birthday.equals(teacher.getBirthday())
                            && email.equals(teacher.getEmail())) {
                        JOptionPane.showMessageDialog(panel, "信息未改变", "提示", JOptionPane.WARNING_MESSAGE);
                    }else if (name==null || birthday==null || email==null){
                        JOptionPane.showMessageDialog(panel,"字段值不能为空","提示",JOptionPane.WARNING_MESSAGE);
                    } else {
                        //update
                        teacher.setName(name);teacher.setBirthday(birthday);teacher.setEmail(email);
                        userDao.updateTeacher(teacher);
                        JOptionPane.showMessageDialog(panel,"信息已更新","提示",JOptionPane.WARNING_MESSAGE);
                    }
                }
            });
        } else {
            Student student = (Student)object;
            idTextField.setText(student.getStuID()+"");
            nameTextField.setText(student.getName());
            emailTextField.setText(student.getEmail());
            communicationTextField.setText(student.getCommunication());
            birthdayTextField.setText(student.getBirthday());
            //系号，学院号需要多表查询，
            Class myClass = userDao.findClassbyID(student.getClassID());
            Department department = userDao.findDepartmentbyID(myClass.getDeptID());
            deptTextField.setText("["+department.getDepartID()+"] "+department.getName());
            collegeTextField.setText("["+department.getCollegeID()+"] "+userDao.findCollegeNamebyCollegeID(department.getCollegeID()));

            classTextField.setText(student.getClassID()+"");
            dormitoryTextField.setText(student.getDormitoryID()+"");
            panel.add(classLabel);classLabel.setBounds(xLabel,y+alice*7,width,height);panel.add(classTextField);classTextField.setBounds(xText,y+alice*7,width,height);
            panel.add(dormitoryLabel);dormitoryLabel.setBounds(xLabel,y+alice*8,width,height);panel.add(dormitoryTextField);dormitoryTextField.setBounds(xText,y+alice*8,width,height);

            JButton saveButton = new JButton("保存");
            JButton electButton = new JButton("选课");
            panel.add(electButton);electButton.setBounds(xText,y+alice*9,width,height);

            panel.add(saveButton);saveButton.setBounds(100,y+alice*9,width,height);
            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String name = nameTextField.getText();
                    String birthday = birthdayTextField.getText();
                    String email = emailTextField.getText();
                    if(name.equals(student.getName())
                            && birthday.equals(student.getBirthday())
                            && email.equals(student.getEmail())) {
                        JOptionPane.showMessageDialog(panel, "信息未改变", "提示", JOptionPane.WARNING_MESSAGE);
                    }else if (name==null || birthday==null || email==null){
                        JOptionPane.showMessageDialog(panel,"字段值不能为空","提示",JOptionPane.WARNING_MESSAGE);
                    } else {
                        //update
                        student.setName(name);student.setBirthday(birthday);student.setEmail(email);
                        userDao.updateStudent(student);
                        JOptionPane.showMessageDialog(panel,"信息已更新","提示",JOptionPane.WARNING_MESSAGE);
                    }
                }
            });
            electButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    elect(student);
                }
            });

            JButton viewCourse = new JButton("查看选课表"); //查看任课教师信息
            JButton viewClass = new JButton("查看班级");
            panel.add(viewCourse);viewCourse.setBounds(100,y+alice*10,width,height);panel.add(viewClass);viewClass.setBounds(xText,y+alice*10,width,height);

            viewCourse.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    viewCourse(student);
                }
            });
            viewClass.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    viewClass(student.getClassID());
                }
            });
        }

        frame.add(panel);
        frame.setVisible(true);
    }

    /**
     * 查看任课表
     * @param teacher
     */
    private void viewTeachCourse(Teacher teacher) {
        JFrame frame = new JFrame("课程表");
        frame.setSize(1000,400);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocation( (screenSize.width - frame.getWidth()) / 2,
                (screenSize.height - frame.getHeight()) / 2);
        JPanel panel = new JPanel(new BorderLayout());
        initGlobalFontSetting(10);
        //从Course表中，查询任课信息，利用teacherID查询任课列表
        List<Course> courses = userDao.findCoursebyTeacherID(teacher.getTeacherID());

        Object[] columns = {"courseID","courseName","credit","classroomID","classroomName","teacherID","teacherName"};
        Object[][] data = new Object[courses.size()][columns.length];
        for (int i=0;i<courses.size();i++) {
            Course course = courses.get(i);
            Classroom classroom = userDao.findClassroombyclassroomID(course.getClassroomID());
            int y = 0;
            data[i][y++] = course.getCourseID();
            data[i][y++] = course.getCourseName();
            data[i][y++] = course.getCredit();
            data[i][y++] = course.getClassroomID();
            data[i][y++] = classroom.getBuilding()+classroom.getLocation();
            data[i][y++] = teacher.getTeacherID();
            data[i][y++] = teacher.getName();
        }
        JTable table = new JTable(data,columns);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setRowHeight(20);
        table.setShowGrid(true);
        panel.add(table.getTableHeader(), BorderLayout.NORTH);
        panel.add(table,BorderLayout.CENTER);
        frame.add(panel);
        frame.setVisible(true);

    }

    /**
     * 选课
     * @param student
     */
    private void elect(Student student) {
        JFrame frame = new JFrame("课程表");
        frame.setSize(800,400);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocation( (screenSize.width - frame.getWidth()) / 2,
                (screenSize.height - frame.getHeight()) / 2);
        JPanel panel = new JPanel(new BorderLayout());
        initGlobalFontSetting(16);
        //从Elect表中，查询选课信息，利用courseID查询选课列表
        List<Course> Courses = userDao.findAllCourse();
        List<Integer> allCourseID = userDao.findAllCourseID();
        List<Integer> electedCourseID = userDao.lookupElectedCourseID(student.getStuID());
        Object[] columns = {"courseID","courseName","credit","classroomID","classroomName","teacherID","teacherName"};
        Object[][] data = new Object[Courses.size()][columns.length];
        for (int i=0;i<Courses.size();i++) {
            if (electedCourseID.contains(Courses.get(i).getCourseID())){ //跳过已选课
                continue;
            }
            Course course = Courses.get(i);
            Teacher teacher = userDao.findTeacherbyID(course.getTeacherID());
            Classroom classroom = userDao.findClassroombyclassroomID(course.getClassroomID());
            int y = 0;
            data[i][y++] = course.getCourseID();
            data[i][y++] = course.getCourseName();
            data[i][y++] = course.getCredit();
            data[i][y++] = course.getClassroomID();
            data[i][y++] = classroom.getBuilding()+classroom.getLocation();
            data[i][y++] = teacher.getTeacherID();
            data[i][y++] = teacher.getName();
        }
        JTable table = new JTable(data,columns);
        table.setShowGrid(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setRowHeight(20);

        panel.add(table.getTableHeader(), BorderLayout.NORTH);
        panel.add(table,BorderLayout.CENTER);
        JTextField numText = new JTextField(10);
        JLabel label = new JLabel("请输入想要选修的课程id");
        JButton confirm = new JButton("确认");
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(1,5,5));
        jPanel.add(label);jPanel.add(numText);jPanel.add(confirm);
        panel.add(jPanel,BorderLayout.SOUTH);
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (numText.getText().matches("^[1-9]\\d*$")){
                    int course = Integer.parseInt(numText.getText());
                    if (allCourseID.contains(course)) {
                        //选课，改选课表，
                        Elect elect = new Elect(course,student.getStuID(),0);
                        userDao.insertElect(elect);
                        JOptionPane.showMessageDialog(panel,"选课成功","提示",JOptionPane.WARNING_MESSAGE);
                        frame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(panel,"无效的操作","提示",JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });
        frame.add(panel);
        frame.setVisible(true);
    }

    /**
     * 查看班级信息
     */
    private void viewClass(int classID) {
        JFrame frame = new JFrame("班级表");
        frame.setSize(800,400);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//        frame.setLocation( (screenSize.width - frame.getWidth()) / 2,
//                (screenSize.height - frame.getHeight()) / 2);
        JPanel panel = new JPanel(new BorderLayout());
        initGlobalFontSetting(16);
        //从Elect表中，查询选课信息，利用courseID查询选课列表
        Object[] columns = {"classID","departmentID","departmentName","teacherID","teacherName","numberOfStudents"};
        Object[][] data = new Object[1][columns.length];
        Class myClass = userDao.findClassbyID(classID);
        Department department = userDao.findDepartmentbyID(myClass.getDeptID());
        Teacher teacher = userDao.findTeacherbyID(myClass.getTeacherID());
        List<Student> students = userDao.findStudentbyClassID(classID);
        int y = 0,i = 0;
        data[i][y++] = myClass.getClassID();
        data[i][y++] = myClass.getDeptID();
        data[i][y++] = department.getName();
        data[i][y++] = teacher.getTeacherID();
        data[i][y++] = teacher.getName();
        data[i][y++] = students.size();

        JTable table = new JTable(data,columns);
        table.setShowGrid(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setRowHeight(20);
        panel.add(table.getTableHeader(), BorderLayout.NORTH);
        panel.add(table,BorderLayout.CENTER);
        frame.add(panel);
        frame.setVisible(true);

    }

    /**
     * 查看选课表
     * @param student
     */
    private void viewCourse(Student student) {
        JFrame frame = new JFrame("课程表");
        frame.setSize(800,400);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocation( (screenSize.width - frame.getWidth()) / 2,
                (screenSize.height - frame.getHeight()) / 2);
        JPanel panel = new JPanel(new BorderLayout());
        initGlobalFontSetting(16);
        //从Elect表中，查询选课信息，利用courseID查询选课列表
        List<Elect> elects = userDao.findElectbyStuID(student.getStuID());
        Object[] columns = {"courseID","courseName","credit","classroomID","classroomName","teacherID","teacherName","achievement"};
        Object[][] data = new Object[elects.size()][columns.length];
        for (int i=0;i<elects.size();i++) {
            Course course = userDao.findCoursebyID(elects.get(i).getCourseID());
            Teacher teacher = userDao.findTeacherbyID(course.getTeacherID());
            Classroom classroom = userDao.findClassroombyclassroomID(course.getClassroomID());
            Elect test = new Elect();
            test.setStuID(student.getStuID());test.setCourseID(course.getCourseID());
            Elect elect = userDao.findElectbyStuIDAndCourseID(test);
            int y = 0;
            data[i][y++] = course.getCourseID();
            data[i][y++] = course.getCourseName();
            data[i][y++] = course.getCredit();
            data[i][y++] = course.getClassroomID();
            data[i][y++] = classroom.getBuilding()+classroom.getLocation();
            data[i][y++] = teacher.getTeacherID();
            data[i][y++] = teacher.getName();
            data[i][y++] = elect.getAchievement();
        }
        JTable table = new JTable(data,columns);
        table.setShowGrid(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setRowHeight(20);
        panel.add(table.getTableHeader(), BorderLayout.NORTH);
        panel.add(table,BorderLayout.CENTER);
        frame.add(panel);
        frame.setVisible(true);

    }

    private void choosePage(){
        JFrame frame = new JFrame("注册界面");
        initGlobalFontSetting(24);
        JButton teacherButton = new JButton("注册教师身份");
        JButton studentButton = new JButton("注册学生身份");
        frame.setSize(800,800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation( (screenSize.width - frame.getWidth()) / 2,
                (screenSize.height - frame.getHeight()) / 2);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        frame.add(panel);
        panel.add(teacherButton);teacherButton.setBounds(300,200,200,100);
        panel.add(studentButton);studentButton.setBounds(300,400,200,100);
        teacherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                registerPage("teacher");
            }
        });
        studentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                registerPage("student");
            }
        });
        frame.setVisible(true);

    }

    /**
     * 注册界面
     * @param choice
     */
    private void registerPage(String choice) {
        JFrame frame = new JFrame("注册界面");
        frame.setSize(800,800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation( (screenSize.width - frame.getWidth()) / 2,
                (screenSize.height - frame.getHeight()) / 2);
        initGlobalFontSetting(24);

        JPanel panel = new JPanel();
        JLabel passwordLabel = new JLabel("密码：");
        JLabel nameLabel = new JLabel("姓名：");
        JLabel emailLabel = new JLabel("邮箱：");
        JLabel communicationLabel = new JLabel("联系方式：");
        JLabel birthdayLabel = new JLabel("出生日期：");
        JLabel deptLabel = new JLabel("所属系：");
        JLabel collegeLabel = new JLabel("所属学院：");
        JLabel classLabel = new JLabel("班级号：");
        JLabel dormitoryLabel = new JLabel("寝室号：");
        int columns = 20;
        JTextField passwordTextField = new JTextField(columns);
        JTextField nameTextField = new JTextField(columns);
        JTextField emailTextField = new JTextField(columns);
        JTextField communicationTextField = new JTextField(columns);
        JTextField birthdayTextField = new JTextField(columns);
        JTextField deptTextField = new JTextField(columns);
        JTextField collegeTextField = new JTextField(columns);
        JTextField classTextField = new JTextField(columns);
        JTextField dormitoryTextField = new JTextField(columns);

        panel.setLayout(null);
        int xLabel = 200,y = 100,alice = 60;
        int xText = 400;
        int width = 250,height = 40;
        panel.add(passwordLabel);passwordLabel.setBounds(xLabel,y,width,height);panel.add(passwordTextField);passwordTextField.setBounds(xText,y,width,height);
        panel.add(nameLabel);nameLabel.setBounds(xLabel,y+alice,width,height);panel.add(nameTextField);nameTextField.setBounds(xText,y+alice,width,height);
        panel.add(emailLabel);emailLabel.setBounds(xLabel,y+alice*2,width,height);panel.add(emailTextField);emailTextField.setBounds(xText,y+alice*2,width,height);
        panel.add(communicationLabel);communicationLabel.setBounds(xLabel,y+alice*3,width,height);panel.add(communicationTextField);communicationTextField.setBounds(xText,y+alice*3,width,height);
        panel.add(birthdayLabel);birthdayLabel.setBounds(xLabel,y+alice*4,width,height);panel.add(birthdayTextField);birthdayTextField.setBounds(xText,y+alice*4,width,height);
        panel.add(deptLabel);deptLabel.setBounds(xLabel,y+alice*5,width,height);panel.add(deptTextField);deptTextField.setBounds(xText,y+alice*5,width,height);
        panel.add(collegeLabel);collegeLabel.setBounds(xLabel,y+alice*6,width,height);panel.add(collegeTextField);collegeTextField.setBounds(xText,y+alice*6,width,height);

        if (choice.equals("student")){
            panel.add(classLabel);classLabel.setBounds(xLabel,y+alice*7,width,height);panel.add(classTextField);classTextField.setBounds(xText,y+alice*7,width,height);
            panel.add(dormitoryLabel);dormitoryLabel.setBounds(xLabel,y+alice*8,width,height);panel.add(dormitoryTextField);dormitoryTextField.setBounds(xText,y+alice*8,width,height);
            JButton button = new JButton("确定");
            panel.add(button);button.setBounds(500,y+alice*9,width,height);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (nameTextField.getText().equals("") || birthdayTextField.getText().equals("") ||
                            emailTextField.getText().equals("") || communicationTextField.getText().equals("") ||
                            deptTextField.getText().equals("") || collegeTextField.getText().equals("") ||
                            classTextField.getText().equals("") || dormitoryTextField.getText().equals("") || passwordTextField.getText().equals("")
                    ) {
                        JOptionPane.showMessageDialog(panel, "个人信息不能为空！", "提示", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    String password = passwordTextField.getText();
                    String name = nameTextField.getText();
                    String birthday = birthdayTextField.getText();
                    String email = emailTextField.getText();
                    String communication = communicationTextField.getText();
                    int deptID = Integer.parseInt(deptTextField.getText());
                    int collegeID = Integer.parseInt(collegeTextField.getText());
                    int classID = Integer.parseInt(classTextField.getText());
                    int dormitoryID = Integer.parseInt(dormitoryTextField.getText());
                    Class myClass = userDao.findClassbyID(classID);
                    Department department = userDao.findDepartmentbyID(deptID);
                    if (myClass==null){
                        JOptionPane.showMessageDialog(panel,"班号不存在，请重新确认！","提示",JOptionPane.WARNING_MESSAGE);
                    } else if (department==null){
                        JOptionPane.showMessageDialog(panel,"系号不存在，请重新确认！","提示",JOptionPane.WARNING_MESSAGE);
                    } else if (userDao.findCollegeNamebyCollegeID(collegeID)==null){
                        JOptionPane.showMessageDialog(panel,"学院号不存在，请重新确认！","提示",JOptionPane.WARNING_MESSAGE);
                    } else if (userDao.findDormitorybyID(dormitoryID)==null){
                        JOptionPane.showMessageDialog(panel,"寝室号不存在，请重新确认！","提示",JOptionPane.WARNING_MESSAGE);
                    } else if (deptID != myClass.getDeptID()){
                        JOptionPane.showMessageDialog(panel,"班号和系号不匹配，请重新确认！","提示",JOptionPane.WARNING_MESSAGE);
                    } else if(collegeID != department.getCollegeID()){
                        JOptionPane.showMessageDialog(panel,"系号和学院号不匹配，请重新确认！","提示",JOptionPane.WARNING_MESSAGE);
                    } else {
                        Student newStudent = new Student(name,birthday,communication,email,password,classID,dormitoryID);
                        Student Student = userDao.findStudentbyName(name);
                        if (newStudent.equals(Student)){
                            JOptionPane.showMessageDialog(panel,"个人信息重复，请重新确认！","提示",JOptionPane.WARNING_MESSAGE);
                        } else {
                            userDao.insertStudent(newStudent);
                            JOptionPane.showMessageDialog(panel,"注册成功！工号为："+newStudent.getStuID(),"提示",JOptionPane.WARNING_MESSAGE);
                            frame.dispose();
                        }
                    }

                }
            });
        } else {
            JButton button = new JButton("确定");
            panel.add(button);button.setBounds(500,y+alice*9,width,height);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(nameTextField.getText().equals("") || birthdayTextField.getText().equals("") ||
                            emailTextField.getText().equals("") || communicationTextField.getText().equals("") ||
                            deptTextField.getText().equals("") || collegeTextField.getText().equals("") ||
                            passwordTextField.getText().equals("")
                    ){
                        JOptionPane.showMessageDialog(panel,"个人信息不能为空！","提示",JOptionPane.WARNING_MESSAGE);
                        return ;
                    }
                    String password = passwordTextField.getText();
                    String name = nameTextField.getText();
                    String birthday = birthdayTextField.getText();
                    String email = emailTextField.getText();
                    String communication = communicationTextField.getText();
                    int deptID = Integer.parseInt(deptTextField.getText());
                    int collegeID = Integer.parseInt(collegeTextField.getText());
                    Department department = userDao.findDepartmentbyID(deptID);
                    if (department==null){
                        JOptionPane.showMessageDialog(panel,"系号不存在，请重新确认！","提示",JOptionPane.WARNING_MESSAGE);
                    } else if (userDao.findCollegeNamebyCollegeID(collegeID)==null){
                        JOptionPane.showMessageDialog(panel,"学院号不存在，请重新确认！","提示",JOptionPane.WARNING_MESSAGE);
                    } else if(collegeID != department.getCollegeID()){
                        JOptionPane.showMessageDialog(panel,"系号和学院号不匹配，请重新确认！","提示",JOptionPane.WARNING_MESSAGE);
                    } else {
                        Teacher newTeacher = new Teacher(name,communication,email,birthday,deptID,password);
                        Teacher teacher = userDao.findTeacherbyName(name);
                        if (newTeacher.equals(teacher)){
                            JOptionPane.showMessageDialog(panel,"个人信息重复，请重新确认！","提示",JOptionPane.WARNING_MESSAGE);
                        } else {
                            userDao.insertTeacher(newTeacher);
                            JOptionPane.showMessageDialog(panel,"注册成功！工号为："+newTeacher.getTeacherID(),"提示",JOptionPane.INFORMATION_MESSAGE);
                            frame.dispose();
                        }
                    }

                }
            });
        }
        frame.add(panel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        UI ui = new UI();
        ui.mainFrame();
        Student student = new Student();
        student.setStuID(1);
//        ui.individualPage("student",student);
//        ui.viewCourse(student);
//        ui.elect(student);
    }
}
