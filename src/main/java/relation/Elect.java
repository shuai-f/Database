package relation;

/**
 * 选修，多对多
 */
public class Elect {
    private int electID;
    private int courseID;
    private int stuID;
    private int achievement;

    @Override
    public String toString() {
        return "Elect{" +
                "electID=" + electID +
                ", courseID=" + courseID +
                ", stuID=" + stuID +
                ", achievement=" + achievement +
                '}';
    }

    public Elect() {
    }

    public int getElectID() {
        return electID;
    }

    public void setElectID(int electID) {
        this.electID = electID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public int getStuID() {
        return stuID;
    }

    public void setStuID(int stuID) {
        this.stuID = stuID;
    }

    public int getAchievement() {
        return achievement;
    }

    public void setAchievement(int achievement) {
        this.achievement = achievement;
    }

    public Elect(int electID, int courseID, int stuID, int achievement) {
        this.electID = electID;
        this.courseID = courseID;
        this.stuID = stuID;
        this.achievement = achievement;
    }

    public Elect(int courseID, int stuID, int achievement) {
        this.courseID = courseID;
        this.stuID = stuID;
        this.achievement = achievement;
    }


}
