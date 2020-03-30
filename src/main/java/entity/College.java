package entity;

/**
 * 学院
 */
public class College {
    private int collegeID;
    private String name;
    private String timeOfestablish;

    public College(int collegeID, String name, String timeOfestablish) {
        this.collegeID = collegeID;
        this.name = name;
        this.timeOfestablish = timeOfestablish;
    }
}
