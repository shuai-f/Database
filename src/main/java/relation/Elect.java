package relation;

/**
 * 选修，多对多
 */
public class Elect {
    private int electID;
    private int courseID;
    private int stuID;
    private int achievement;

    public Elect(int courseID, int stuID, int achievement) {
        this.courseID = courseID;
        this.stuID = stuID;
        this.achievement = achievement;
    }


}
