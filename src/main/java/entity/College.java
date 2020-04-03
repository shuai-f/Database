package entity;

/**
 * 学院
 */
public class College {
    private int collegeID;
    private String name;
    private String address;
    private String postcode;
    private String communication;
    private String timeOfestablish;

    public College(int collegeID, String name, String address, String postcode, String communication, String timeOfestablish) {
        this.collegeID = collegeID;
        this.name = name;
        this.address = address;
        this.postcode = postcode;
        this.communication = communication;
        this.timeOfestablish = timeOfestablish;
    }

    public College(String name, String address, String postcode, String communication, String timeOfestablish) {
        this.name = name;
        this.address = address;
        this.postcode = postcode;
        this.communication = communication;
        this.timeOfestablish = timeOfestablish;
    }

    @Override
    public String toString() {
        return "College{" +
                "collegeID=" + collegeID +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", postcode='" + postcode + '\'' +
                ", communication='" + communication + '\'' +
                ", timeOfestablish='" + timeOfestablish + '\'' +
                '}';
    }

    public int getCollegeID() {
        return collegeID;
    }

    public void setCollegeID(int collegeID) {
        this.collegeID = collegeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCommunication() {
        return communication;
    }

    public void setCommunication(String communication) {
        this.communication = communication;
    }

    public String getTimeOfestablish() {
        return timeOfestablish;
    }

    public void setTimeOfestablish(String timeOfestablish) {
        this.timeOfestablish = timeOfestablish;
    }
}
