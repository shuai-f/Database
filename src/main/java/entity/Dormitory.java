package entity;

/**
 * 寝室
 */
public class Dormitory {
    private int dormitoryID;
    private String apartmentName;

    public Dormitory(int dormitoryID, String apartmentName) {
        this.dormitoryID = dormitoryID;
        this.apartmentName = apartmentName;
    }

    @Override
    public String toString() {
        return "Dormitory{" +
                "dormitoryID=" + dormitoryID +
                ", apartmentName='" + apartmentName + '\'' +
                '}';
    }

    public int getDormitoryID() {
        return dormitoryID;
    }

    public void setDormitoryID(int dormitoryID) {
        this.dormitoryID = dormitoryID;
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }
}
