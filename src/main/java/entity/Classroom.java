package entity;

/**
 * 教室
 */
public class Classroom {
    private int classroomID;
    private String building;
    private String location;

    public Classroom(int classroomID, String building, String location) {
        this.classroomID = classroomID;
        this.building = building;
        this.location = location;
    }

    public Classroom(String building, String location) {
        this.building = building;
        this.location = location;
    }

    public int getClassroomID() {
        return classroomID;
    }

    public void setClassroomID(int classroomID) {
        this.classroomID = classroomID;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
