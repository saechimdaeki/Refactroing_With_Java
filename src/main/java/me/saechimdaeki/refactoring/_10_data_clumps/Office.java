package me.saechimdaeki.refactoring._10_data_clumps;

public class Office {

    private String location;


    private TelephoneNumber officePhoneNumber;

    public Office(String location, TelephoneNumber officePhoneNumber) {
        this.location = location;
        this.officePhoneNumber = officePhoneNumber;
    }

    public String officePhoneNumber(){
        return this.officePhoneNumber.toString();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public TelephoneNumber getOfficePhoneNumber() {
        return officePhoneNumber;
    }
}