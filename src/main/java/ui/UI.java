package ui;

import dao.IUserDao;
import entity.College;
import entity.Department;
import entity.Student;
import entity.Teacher;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import relation.Manage;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

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
            session = factory.openSession();
            //5.使用 SqlSession 创建 dao 接口的代理对象
            userDao = session.getMapper(IUserDao.class);
        } catch (IOException e) {
            System.out.println("配置文件读取异常");
        }

    }

    public void  mainFrame(){
        JFrame frame = new JFrame("主界面");
        frame.setSize(600,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation( (screenSize.width - frame.getWidth()) / 2,
                (screenSize.height - frame.getHeight()) / 2);
        // 创建面板
        JPanel panel = new JPanel();
        panel.setLayout(null);    // 面板布局

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
        int x,y,width,height;
        x = 150;
        y = 100;
        userLabel.setBounds(x, y+20, 80, 25);
        passwordLabel.setBounds(x, y+50, 80, 25);
        identityLabel.setBounds(x, y+80, 80, 25);

        userNameText.setBounds(x+40, y+20, 165, 25);
        userPasswordText.setBounds(x+40, y+50, 165, 25);
        //identityText.setBounds(x+40, y+80, 165, 25);

        loginButton.setBounds(220, y+110, 80, 25);
        registerButton.setBounds(220, y+140, 80, 25);

        JComboBox comBox = new JComboBox();//下拉列表
        comBox.addItem("学生");
        comBox.addItem("老师");
        comBox.setBounds(x+40, y+80, 165, 25);
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
                // 检测身份
                int userID = Integer.parseInt(userNameText.getText());
                String password = String.valueOf(userPasswordText.getPassword());
                System.out.println(password);
                if (comBox.getSelectedItem().toString().equals("老师")) {
                    Teacher teacher = userDao.findTeacherbyID(userID);
                    System.out.println(teacher);
                    // 查找该用户
                    if (teacher.getPassword().equals(password)){
                        JOptionPane.showMessageDialog(panel, "登陆成功", "提示",JOptionPane.WARNING_MESSAGE);
                        frame.dispose();
                        individualPage("teacher",teacher);
                    } else {
                        JOptionPane.showMessageDialog(panel, "学工号或密码错误", "提示",JOptionPane.WARNING_MESSAGE);
                    }
                } else if (comBox.getSelectedItem().toString().equals("学生")) {
                    // 查找该用户
                    Student student = userDao.findStudentbyID(userID);
                    System.out.println(student);
                    if (student.getPassword().equals(password)){
                        JOptionPane.showMessageDialog(panel, "登录成功", "提示",JOptionPane.WARNING_MESSAGE);
                        frame.dispose();
                        individualPage("student",student);
                    } else {
                        JOptionPane.showMessageDialog(panel, "学工号或密码错误", "提示",JOptionPane.WARNING_MESSAGE);
                    }
                }

            }
        });

        // 注册按钮的监听事件
        registerButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                registerPage();
            }
        });

        frame.setVisible(true);
    }

    /**
     * 修改全局字体
     */
    public void initGlobalFontSetting(){
        //Font
        Font font = new Font("Dialog", Font.PLAIN, 24);
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, font);
            }
        }
    }
    private void individualPage(String userType, Object object) {
        JFrame frame = new JFrame("个人信息页");
        frame.setSize(800,800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation( (screenSize.width - frame.getWidth()) / 2,
                (screenSize.height - frame.getHeight()) / 2);

        initGlobalFontSetting();

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

        int columns = 20;
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
        JButton button = new JButton("修改个人信息");

        if (userType.equals("teacher")){
            Teacher teacher = (Teacher)object;
            idTextField.setText(teacher.getTeacherID()+"");
            nameTextField.setText(teacher.getName());
            emailTextField.setText(teacher.getEmail());
            communicationTextField.setText(teacher.getCommunication());
            birthdayTextField.setText(teacher.getBirthday());
            //学院号需要多表查询，
            deptTextField.setText(teacher.getDeptID()+"");
            Department department = userDao.findDepartmentbyID(teacher.getDeptID());
            collegeTextField.setText(department.getCollegeID()+"");
            Manage manage = userDao.findManagebyTeacherID(teacher.getTeacherID());
            if(manage.getManageID()!=0){
                JLabel manageClassLabel = new JLabel("管理班：");
                JTextField manageClassTextField = new JTextField();
                manageClassTextField.setText(manage.getClassID()+"");
                panel.add(manageClassLabel);manageClassLabel.setBounds(xLabel,y+alice*7,width,height);panel.add(manageClassTextField);manageClassTextField.setBounds(xText,y+alice*7,width,height);
            }
        } else {
            Student student = (Student)object;
            idTextField.setText(student.getStuID()+"");
            nameTextField.setText(student.getName());
            emailTextField.setText(student.getEmail());
            communicationTextField.setText(student.getCommunication());
            birthdayTextField.setText(student.getBirthday());
            //系号，学院号需要多表查询，
            classTextField.setText(student.getClassID()+"");
            dormitoryTextField.setText(student.getDormitoryID()+"");
            panel.add(classLabel);classLabel.setBounds(xLabel,y+alice*7,width,height);panel.add(classTextField);classTextField.setBounds(xText,y+alice*7,width,height);
            panel.add(dormitoryLabel);dormitoryLabel.setBounds(xLabel,y+alice*8,width,height);panel.add(dormitoryTextField);dormitoryTextField.setBounds(xText,y+alice*8,width,height);

            JButton viewCourse = new JButton("查看选课表"); //查看任课教师信息
            JButton viewClass = new JButton("查看所在班级人员分布");
            panel.add(viewCourse);viewCourse.setBounds(xLabel,y+alice*9,width,height);panel.add(viewClass);viewClass.setBounds(xText,y+alice*9,width,height);

        }
        frame.add(panel);
        frame.setVisible(true);
    }

    private void registerPage() {
    }

    public static void main(String[] args) {
        UI ui = new UI();
        ui.mainFrame();
//        Student student = new Student();
//        ui.individualPage("student",student);
    }
}
