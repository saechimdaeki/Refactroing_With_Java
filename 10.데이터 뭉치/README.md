## 냄새 10. 데이터 뭉치 (Data Clumps)
- 항상 뭉쳐 다니는 데이터는 한 곳으로 모아두는 것이 좋다
  - 여러 클래스에 존재하는 비슷한 필드 목록
  - 여러 함수에 전달하는 매개변수 목록
- 관련 리팩토링 기술
  - `클래스 추출하기(Extract Class)`를 사용해 여러 필드를 하나의 객체나 클래스로 모을 수 있다
  - `매개변수 객체 만들기(Introduce Parameter Obejct)` 또는 `객체 통째로 넘기기(Preserve Whole Object)`
  - 를 사용해 메소드매개변수를 개선할 수 있다.

### 데이터 뭉치 개선 전 코드
```java
public class Office {

    private String location;

    private String officeAreCode;

    private String officeNumber;

    public Office(String location, String officeAreCode, String officeNumber) {
        this.location = location;
        this.officeAreCode = officeAreCode;
        this.officeNumber = officeNumber;
    }

    public String officePhoneNumber() {
        return officeAreCode + "-" + officeNumber;
    }

    public String getOfficeAreCode() {
        return officeAreCode;
    }

    public void setOfficeAreCode(String officeAreCode) {
        this.officeAreCode = officeAreCode;
    }

    public String getOfficeNumber() {
        return officeNumber;
    }

    public void setOfficeNumber(String officeNumber) {
        this.officeNumber = officeNumber;
    }
}
public class Employee {

    private String name;

    private String personalAreaCode;

    private String personalNumber;

    public Employee(String name, String personalAreaCode, String personalNumber) {
        this.name = name;
        this.personalAreaCode = personalAreaCode;
        this.personalNumber = personalNumber;
    }

    public String personalPhoneNumber() {
        return personalAreaCode + "-" + personalNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPersonalAreaCode() {
        return personalAreaCode;
    }

    public void setPersonalAreaCode(String personalAreaCode) {
        this.personalAreaCode = personalAreaCode;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }
}
```

### 데이터 뭉치 개선 후 코드
```java
public class TelephoneNumber {
    private String personalAreaCode;

    private String personalNumber;

    public TelephoneNumber(String personalAreaCode, String personalNumber) {
        this.personalAreaCode = personalAreaCode;
        this.personalNumber = personalNumber;
    }

    public String getPersonalAreaCode() {
        return personalAreaCode;
    }

    public void setPersonalAreaCode(String personalAreaCode) {
        this.personalAreaCode = personalAreaCode;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }

    @Override
    public String toString() {
        return this.personalAreaCode+"-"+this.personalNumber;
    }
}

public class Employee {

    private String name;

    private TelephoneNumber personalPhoneNumber;

    public Employee(String name, TelephoneNumber personalPhoneNumber) {
        this.name = name;
        this.personalPhoneNumber = personalPhoneNumber;
    }

    public String personalPhoneNumber(){
        return this.personalPhoneNumber.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TelephoneNumber getPersonalPhoneNumber() {
        return personalPhoneNumber;
    }
}

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

```