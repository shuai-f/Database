package entity;

/**
 * 系
 */
public class Department {
    private int departID;
    private String name;
    private String timeOfestablish;
    private int collegeID; // A college set one or more dept , a dept only belongs to one college

    public int getDepartID() {
        return departID;
    }

    public void setDepartID(int departID) {
        this.departID = departID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeOfestablish() {
        return timeOfestablish;
    }

    public void setTimeOfestablish(String timeOfestablish) {
        this.timeOfestablish = timeOfestablish;
    }

    public int getCollegeID() {
        return collegeID;
    }

    public void setCollegeID(int collegeID) {
        this.collegeID = collegeID;
    }

    @Override
    public String toString() {
        return "Department{" +
                "departID=" + departID +
                ", name='" + name + '\'' +
                ", timeOfestablish='" + timeOfestablish + '\'' +
                ", collegeID=" + collegeID +
                '}';
    }
}
