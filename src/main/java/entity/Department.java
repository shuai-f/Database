package entity;

/**
 * 系
 */
public class Department {
    private int departID;
    private String name;
    private String timeOfestablish;
    private int collegeID; // A college set one or more dept , a dept only belongs to one college

    public Department(int departID, String name, String timeOfestablish, int collegeID) {
        this.departID = departID;
        this.name = name;
        this.timeOfestablish = timeOfestablish;
        this.collegeID = collegeID;
    }
}
