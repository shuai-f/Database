package entity;

/**
 * @Classname Administrator
 * @Description TODO
 * @Date 2020/4/2 21:22
 * @Created by 冯帅
 */
public class Administrator {
    private String admin;
    private String password;

    public Administrator(String admin, String password) {
        this.admin = admin;
        this.password = password;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
