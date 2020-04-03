package ui;

import com.sun.org.apache.xerces.internal.util.EntityResolverWrapper;
import complex.Conn;
import dao.IUserDao;
import entity.*;

import entity.Class;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import relation.Elect;
import relation.Manage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;
import java.util.Map;

/**
 * @Classname AdminUI
 * @Description TODO
 * @Date 2020/4/2 21:26
 * @Created by 冯帅
 */
public class AdminUI {
    private InputStream in ;
    private SqlSessionFactory factory;
    private SqlSession session;
    private Connection connection;
    private Savepoint savepoint = null;//保存点
    private IUserDao userDao;
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public AdminUI() {
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
            session = factory.openSession();
            connection = session.getConnection();
            //5.使用 SqlSession 创建 dao 接口的代理对象
            userDao = session.getMapper(IUserDao.class);
        } catch (IOException e) {
            System.out.println("配置文件读取异常");
        }

    }

    /**
     * 提交事务
     */
    public void commit() {
        try {
            //设置保存点
            savepoint = connection.setSavepoint();
            //6.事务提交
            session.commit();
        } catch (Exception e) {
            try {
                //回滚
                connection.rollback(savepoint);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
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

    public void adminPage() {
        JFrame frame = new JFrame("管理页");
        frame.setSize(800,800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation( (screenSize.width - frame.getWidth()) / 2,
                (screenSize.height - frame.getHeight()) / 2);

        initGlobalFontSetting(24);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        JButton insertButton = new JButton("插入");
        JButton selectButton = new JButton("查询");
        JButton updateButton = new JButton("更新");
        JButton deleteButton = new JButton("删除");
        int x=300,y=150,width=200,height=100;
        panel.add(insertButton);insertButton.setBounds(x,y,width,height);
        panel.add(selectButton);selectButton.setBounds(x,y+100,width,height);
//        panel.add(updateButton);updateButton.setBounds(x,y+200,width,height);
        panel.add(deleteButton);deleteButton.setBounds(x,y+200,width,height);
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectPage();
            }
        });
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertPage();
            }
        });
//        updateButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                updatePage();
//            }
//        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletePage();
            }
        });
        frame.add(panel);
        frame.setVisible(true);

    }

    private void deletePage() {
        JFrame frame = new JFrame("Delete页");
        frame.setSize(1000,800);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocation( (screenSize.width - frame.getWidth()) / 2,
                (screenSize.height - frame.getHeight()) / 2);

        initGlobalFontSetting(16);
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(1,5,5));
        JButton DormitoryButton = new JButton("dormitory");
        JButton TeacherButton = new JButton("teacher");
        JButton ClassButton = new JButton("class");
        JButton ClassroomButton = new JButton("classroom");
        JButton CollegeButton = new JButton("college");
        JButton DepartmentButton = new JButton("department");
        JButton CourseButton = new JButton("course");
        JButton ElectButton = new JButton("elect");
        JButton ManageButton = new JButton("manage");
        JButton StudentButton = new JButton("student");

        panel.add(StudentButton);panel.add(TeacherButton);panel.add(CollegeButton);panel.add(DepartmentButton);
        panel.add(ClassButton);panel.add(ClassroomButton);panel.add(CourseButton);panel.add(DormitoryButton);
        panel.add(ElectButton);
        panel.add(ManageButton);
        frame.getContentPane().add(BorderLayout.SOUTH,panel);
        JLabel label = new JLabel("请先输入要删除表项的id，然后选择删除表：");
        JTextField textFiled = new JTextField(20);
        JPanel jPanel = new JPanel(new FlowLayout(1,5,5));
        jPanel.add(label);
        jPanel.add(textFiled);
        frame.getContentPane().add(BorderLayout.CENTER,jPanel);
        StudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textFiled.getText().matches("^[1-9]\\d*$")){
                    int id = Integer.parseInt(textFiled.getText());
                    Student student = userDao.findStudentbyID(id);
                    System.out.println(student);
                    if (student!=null){
                        userDao.deleteElectbyStuID(id); //要删除学生的话需要先删除选课表中的信息（外键约束）
                        userDao.deleteStudent(id);
                        commit();
                        JOptionPane.showMessageDialog(null,"删除成功！","提示",JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                JOptionPane.showMessageDialog(null,"无效的操作！","提示",JOptionPane.WARNING_MESSAGE);
            }
        });
        frame.setVisible(true);
    }

    private void updatePage() {
        JFrame frame = new JFrame("Update页");
        frame.setSize(1000,800);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocation( (screenSize.width - frame.getWidth()) / 2,
                (screenSize.height - frame.getHeight()) / 2);

        initGlobalFontSetting(16);
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(1,5,5));
        JButton DormitoryButton = new JButton("dormitory");
        JButton TeacherButton = new JButton("teacher");
        JButton ClassButton = new JButton("class");
        JButton ClassroomButton = new JButton("classroom");
        JButton CollegeButton = new JButton("college");
        JButton DepartmentButton = new JButton("department");
        JButton CourseButton = new JButton("course");
        JButton ElectButton = new JButton("elect");
        JButton ManageButton = new JButton("manage");
        JButton StudentButton = new JButton("student");

        panel.add(StudentButton);panel.add(TeacherButton);panel.add(CollegeButton);panel.add(DepartmentButton);
        panel.add(ClassButton);panel.add(ClassroomButton);panel.add(CourseButton);panel.add(DormitoryButton);
        panel.add(ElectButton);
        panel.add(ManageButton);
        frame.getContentPane().add(BorderLayout.CENTER,panel);
        JLabel label = new JLabel("请先输入要删除表项的id，然后选择删除表：");
        JTextField textFiled = new JTextField(20);
        JPanel jPanel = new JPanel(new FlowLayout(1,5,5));
        jPanel.add(label);
        jPanel.add(textFiled);
        frame.getContentPane().add(BorderLayout.NORTH,jPanel);
        StudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textFiled.getText().matches("^[1-9]\\d*$")){
                    int id = Integer.parseInt(textFiled.getText());
                    Student student = userDao.findStudentbyID(id);
                    System.out.println(student);
                    if (student!=null){
                        userDao.deleteStudent(id);
                        userDao.deleteElectbyStuID(id);
                        updateStudent(student);

                    } else {
                        JOptionPane.showMessageDialog(null,"！","提示",JOptionPane.WARNING_MESSAGE);
                    }
                }
                JOptionPane.showMessageDialog(null,"无效的操作！","提示",JOptionPane.WARNING_MESSAGE);
            }
        });
        frame.setVisible(true);
    }

    private void updateStudent(Student student) {
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
        JLabel deptLabel = new JLabel("所属系号：");
        JLabel collegeLabel = new JLabel("所属学院号：");
        JLabel classLabel = new JLabel("班级号：");
        JLabel dormitoryLabel = new JLabel("寝室号：");

        int columns = 25;
        JTextField idTextField = new JTextField(columns);
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
        panel.add(idLabel);idLabel.setBounds(xLabel,y,width,height);panel.add(idTextField);idTextField.setBounds(xText,y,width,height);
        panel.add(nameLabel);nameLabel.setBounds(xLabel,y+alice,width,height);panel.add(nameTextField);nameTextField.setBounds(xText,y+alice,width,height);
        panel.add(emailLabel);emailLabel.setBounds(xLabel,y+alice*2,width,height);panel.add(emailTextField);emailTextField.setBounds(xText,y+alice*2,width,height);
        panel.add(communicationLabel);communicationLabel.setBounds(xLabel,y+alice*3,width,height);panel.add(communicationTextField);communicationTextField.setBounds(xText,y+alice*3,width,height);
        panel.add(birthdayLabel);birthdayLabel.setBounds(xLabel,y+alice*4,width,height);panel.add(birthdayTextField);birthdayTextField.setBounds(xText,y+alice*4,width,height);
        panel.add(deptLabel);deptLabel.setBounds(xLabel,y+alice*5,width,height);panel.add(deptTextField);deptTextField.setBounds(xText,y+alice*5,width,height);
        panel.add(collegeLabel);collegeLabel.setBounds(xLabel,y+alice*6,width,height);panel.add(collegeTextField);collegeTextField.setBounds(xText,y+alice*6,width,height);

        //deal
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

        JButton button = new JButton("保存");
        panel.add(button);button.setBounds(100,y+alice*9,width,height);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameTextField.getText();
                String birthday = birthdayTextField.getText();
                String email = emailTextField.getText();
                if(name.equals(student.getName())
                        && birthday.equals(student.getBirthday())
                        && email.equals(student.getEmail())) {
                    JOptionPane.showMessageDialog(panel, "信息未改变", "提示", JOptionPane.WARNING_MESSAGE);
                }else if (name==null || birthday==null || email==null ){
                    JOptionPane.showMessageDialog(panel,"字段值不能为空","提示",JOptionPane.WARNING_MESSAGE);
                } else {
                    //update
                    student.setName(name);student.setBirthday(birthday);student.setEmail(email);
                    userDao.updateStudent(student);
                    JOptionPane.showMessageDialog(panel,"信息已更新","提示",JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }


    private void insertPage() {
        JFrame frame = new JFrame("Insert页");
        frame.setSize(1000,800);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocation( (screenSize.width - frame.getWidth()) / 2,
                (screenSize.height - frame.getHeight()) / 2);

        initGlobalFontSetting(16);
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(1,5,5));
        JButton DormitoryButton = new JButton("dormitory");
        JButton TeacherButton = new JButton("teacher");
        JButton ClassButton = new JButton("class");
        JButton ClassroomButton = new JButton("classroom");
        JButton CollegeButton = new JButton("college");
        JButton DepartmentButton = new JButton("department");
        JButton CourseButton = new JButton("course");
        JButton ElectButton = new JButton("elect");
        JButton ManageButton = new JButton("manage");
        JButton StudentButton = new JButton("student");

        panel.add(StudentButton);panel.add(TeacherButton);panel.add(CollegeButton);panel.add(DepartmentButton);
        panel.add(ClassButton);panel.add(ClassroomButton);panel.add(CourseButton);panel.add(DormitoryButton);
        panel.add(ElectButton);
        panel.add(ManageButton);
        frame.getContentPane().add(BorderLayout.CENTER,panel);
        JLabel label = new JLabel("请选择要插入的表：");
        JPanel jPanel = new JPanel(new FlowLayout(1,5,5));
        jPanel.add(label);
        frame.getContentPane().add(BorderLayout.NORTH,jPanel);
        StudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                insertStudent();
            }
        });
        frame.setVisible(true);
    }

    private void insertStudent() {
        JFrame frame = new JFrame("InsertStudent页");
        frame.setSize(800,800);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
        panel.add(classLabel);classLabel.setBounds(xLabel,y+alice*7,width,height);panel.add(classTextField);classTextField.setBounds(xText,y+alice*7,width,height);
        panel.add(dormitoryLabel);dormitoryLabel.setBounds(xLabel,y+alice*8,width,height);panel.add(dormitoryTextField);dormitoryTextField.setBounds(xText,y+alice*8,width,height);

        JButton button = new JButton("保存");
        panel.add(button);button.setBounds(100,y+alice*9,width,height);
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
                    if (myClass == null) {
                        JOptionPane.showMessageDialog(panel, "班号不存在，请重新确认！", "提示", JOptionPane.WARNING_MESSAGE);
                    } else if (department == null) {
                        JOptionPane.showMessageDialog(panel, "系号不存在，请重新确认！", "提示", JOptionPane.WARNING_MESSAGE);
                    } else if (userDao.findCollegeNamebyCollegeID(collegeID) == null) {
                        JOptionPane.showMessageDialog(panel, "学院号不存在，请重新确认！", "提示", JOptionPane.WARNING_MESSAGE);
                    } else if (userDao.findDormitorybyID(dormitoryID) == null) {
                        JOptionPane.showMessageDialog(panel, "寝室号不存在，请重新确认！", "提示", JOptionPane.WARNING_MESSAGE);
                    } else if (deptID != myClass.getDeptID()) {
                        JOptionPane.showMessageDialog(panel, "班号和系号不匹配，请重新确认！", "提示", JOptionPane.WARNING_MESSAGE);
                    } else if (collegeID != department.getCollegeID()) {
                        JOptionPane.showMessageDialog(panel, "系号和学院号不匹配，请重新确认！", "提示", JOptionPane.WARNING_MESSAGE);
                    } else {
                        Student newStudent = new Student(name, birthday, communication, email, password, classID, dormitoryID);
                        Student Student = userDao.findStudentbyName(name);
                        if (newStudent.equals(Student)) {
                            JOptionPane.showMessageDialog(panel, "个人信息重复，请重新确认！", "提示", JOptionPane.WARNING_MESSAGE);
                        } else {
                            userDao.insertStudent(newStudent);
                            commit();
                            JOptionPane.showMessageDialog(panel, "插入成功！工号为：" + newStudent.getStuID(), "提示", JOptionPane.WARNING_MESSAGE);
                            frame.dispose();
                        }
                    }

                }
        });
        frame.add(panel);
        frame.setVisible(true);
    }

    private void selectPage() {
        JFrame frame = new JFrame("Select页");
        frame.setSize(1000,800);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocation( (screenSize.width - frame.getWidth()) / 2,
                (screenSize.height - frame.getHeight()) / 2);

        initGlobalFontSetting(24);
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(1,5,5));
        JButton DormitoryButton = new JButton("dormitory");
        JButton TeacherButton = new JButton("teacher");
        JButton ClassButton = new JButton("class");
        JButton ClassroomButton = new JButton("classroom");
        JButton CollegeButton = new JButton("college");
        JButton DepartmentButton = new JButton("department");
        JButton CourseButton = new JButton("course");
        JButton ElectButton = new JButton("elect");
        JButton ManageButton = new JButton("manage");
        JButton StudentButton = new JButton("student");

        panel.add(StudentButton);panel.add(TeacherButton);panel.add(CollegeButton);panel.add(DepartmentButton);
        panel.add(ClassButton);panel.add(ClassroomButton);panel.add(CourseButton);panel.add(DormitoryButton);
        panel.add(ElectButton);
        panel.add(ManageButton);

        //
        JButton groupButton = new JButton("groupQuery");
        JButton havingButton = new JButton("havingQuery");
        JButton nestingButton = new JButton("nestingQuery");
        JButton connectButton = new JButton("connectQuery");
        panel.add(groupButton);panel.add(havingButton);panel.add(nestingButton);panel.add(connectButton);

        JTextArea textArea = new JTextArea(30,45);
        textArea.setLineWrap(true);
        panel.add(textArea);
        groupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText("分组查询：查询各个课程号对应的选课人数\n");
                for (Map map : userDao.findStusGroupbyCourseID()){
                    textArea.append(String.valueOf(map) + "\n");
                }
            }
        });
        havingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText("查询选课表中所有门课程平均成绩超过90的学生学号\n");
                textArea.append("StuID=\n");
                for (int i : userDao.havingAndGroupQuery()){
                    textArea.append(i+"\n");
                }
            }
        });
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText("连接查询测试:查询Class表对应的Department表\n");
                List<Conn> conns = userDao.conn();
                for (Conn name : conns){
                    textArea.append(name.toString()+"\n");
                }
            }
        });
        nestingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText("嵌套查询测试:查询选修了二号课程的学生姓名\nStuName=\n");
                for (String name : userDao.nestingQuery()){
                    textArea.append(name+'\n');
                }
            }
        });
        StudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
                for (Student object : userDao.findAllStudent()){
                    textArea.append(object.toString()+"\n");
                }
            }
        });
        TeacherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
                for (Teacher object : userDao.findAllTeacher()){
                    textArea.append(object.toString()+"\n");
                }
            }
        });
        DepartmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
                for (Department object : userDao.findAllDepartment()){
                    textArea.append(object.toString()+"\n");
                }
            }
        });
        CollegeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
                for (College object : userDao.findAllCollege()){
                    textArea.append(object.toString()+"\n");
                }
            }
        });
        ClassButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
                for (Class object : userDao.findAllClass()){
                    textArea.append(object.toString()+"\n");
                }
            }
        });
        ClassroomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
                for (Classroom object : userDao.findAllClassroom()){
                    textArea.append(object.toString()+"\n");
                }
            }
        });
        CourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
                for (Course object : userDao.findAllCourse()){
                    textArea.append(object.toString()+"\n");
                }
            }
        });
        DormitoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
                for (Dormitory object : userDao.findAllDormitory()){
                    textArea.append(object.toString()+"\n");
                }
            }
        });
        ElectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
                for (Elect object : userDao.findAllElect()){
                    textArea.append(object.toString()+"\n");
                }
            }
        });
        ManageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
                for (Manage object : userDao.findAllManage()){
                    textArea.append(object.toString()+"\n");
                }
            }
        });


        frame.add(panel);
        frame.setVisible(true);
    }

}
