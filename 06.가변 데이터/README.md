## 냄새 6. 가변 데이터 (Mutable Data)
- 데이터를 변경하다보면 예상치 못했던 결과나 해결하기 어려운 버그가 발생하기도 한다.
- 함수형 프로그래밍 언어는 데이터를 변경하지 않고 복사본을 전달한다. 하지만 그밖의 프로그래밍 언어는 데이터 변경을 허용
- 하고 있다. 따라서 변경되는 데이터 사용 시 발생할 수 있는 리스크를 관리할 수 있는 방법을 적용하는게 좋다
- 관련 리팩토링
  - `변수 캡슐화하기(Encapsulate Variable)`를 적용해 데이터를 변경할 수 있는 메소드를 제한하고 관리할 수 있다
  - `변수 쪼개기(Split Variable)` 을 사용해 여러 데이터를 저장하는 변수를 나눌 수 있다.
  - `코드 정리하기(Slide Statements)`를 사용해 데이터를 변경하는 코드를 분리하고 피할 수 있다.
  - `함수 추출하기(Extract Function)`으로 데이터를 변경하는 코드로부터 사이드 이펙트가 없는 코드를 분리할 수 있다.
  - `질의 함수와 변경 함수 분리하기(Separate Query from Modifier)`를 적용해서 클라이언트가 원하는 경우에만
  - 사이드 이펙트가 있는 함수를 호출하도록 API를 개선할 수 있다
  - 가능하다면 `세터 제거하기(Remove Setting Method)` 를 적용한다
  - 계산해서 알아낼 수 있는 값에는 `파생 변수를 질의 함수로 바꾸기(Replace Derived Variable with Query)`를 적용할 수 있다
  - 변수가 사용되는 범위를 제한하려면 `여러 함수를 클래스로 묶기(Combine Functions into Class)`또는
  - `여러 함수를 변환 함수로 변환(Combine Functions into Transform)` 을 적용할 수 있다.
  - `참조를 값으로 바꾸기(Change Reference to Value)`를 적용해서 데이터 일부를 변경하기 보다는 데이터 전체를 교체할 수 있다

### 리팩토링 18. 변수 쪼개기 (Split Variable)
- 어떤 변수가 여러번 재할당 되어도 적절한 경우
  - 반복문에서 순회하는데 사용하는 변수 또는 인덱스
  - 값을 축적시키는데 사용하는 변수
- 그밖에 경우에 재할당 되는 변수가 있다면 해당 변수는 여러 용도로 사용되는 것이며 분리해야 더 이해하기 좋은 코드를 만들 수 있다.
  - 변수 하나 당 하나의 책임(Responsibility)을 지도록 만든다
  - 상수를 활용하자 (자바스크립트의 const, 자바의 final)

### 변수 쪼개기 전 코드
```java
public class Haggis {

    private double primaryForce;
    private double secondaryForce;
    private double mass;
    private int delay;

    public Haggis(double primaryForce, double secondaryForce, double mass, int delay) {
        this.primaryForce = primaryForce;
        this.secondaryForce = secondaryForce;
        this.mass = mass;
        this.delay = delay;
    }

    public double distanceTravelled(int time) {
        double result;
        double acc = primaryForce / mass;
        int primaryTime = Math.min(time, delay);
        result = 0.5 * acc * primaryTime * primaryTime;

        int secondaryTime = time - delay;
        if (secondaryTime > 0) {
            double primaryVelocity = acc * delay;
            acc = (primaryForce + secondaryForce) / mass;
            result += primaryVelocity * secondaryTime + 0.5 * acc * secondaryTime + secondaryTime;
        }

        return result;
    }
}

public class Order {

    public double discount(double inputValue, int quantity) {
        if (inputValue > 50) inputValue = inputValue - 2;
        if (quantity > 100) inputValue = inputValue - 1;
        return inputValue;
    }
}

public class Rectangle {

    private double perimeter;
    private double area;

    public void updateGeometry(double height, double width) {
        double temp = 2 * (height + width);
        System.out.println("Perimeter: " + temp);
        perimeter = temp;

        temp = height * width;
        System.out.println("Area: " + temp);
        area = temp;
    }

    public double getPerimeter() {
        return perimeter;
    }

    public double getArea() {
        return area;
    }
}

```

### 변수 쪼개기 이후 코드
```java
public class Haggis {

    private double primaryForce;
    private double secondaryForce;
    private double mass;
    private int delay;

    public Haggis(double primaryForce, double secondaryForce, double mass, int delay) {
        this.primaryForce = primaryForce;
        this.secondaryForce = secondaryForce;
        this.mass = mass;
        this.delay = delay;
    }

    public double distanceTravelled(int time) {
        double result;
        final double primaryAcceleration = primaryForce / mass;
        int primaryTime = Math.min(time, delay);
        result = 0.5 * primaryAcceleration * primaryTime * primaryTime;

        int secondaryTime = time - delay;
        if (secondaryTime > 0) {
            final double primaryVelocity = primaryAcceleration * delay;
            final double secondaryAcceleration = (primaryForce + secondaryForce) / mass;
            result += primaryVelocity * secondaryTime + 0.5 * secondaryAcceleration * secondaryTime + secondaryTime;
        }
        return result;
    }
}

public class Order {

    public double discount(double inputValue, int quantity) {
        double result = inputValue;

        if (inputValue > 50) result-=2;
        if (quantity > 100) result-=1;

        return result;
    }
}

public class Rectangle {

    private double perimeter;
    private double area;

    public void updateGeometry(double height, double width) {
        final double perimeter = 2 * (height + width);
        System.out.println("Perimeter: " + perimeter);
        this.perimeter = perimeter;

        final double area = height * width;
        System.out.println("Area: " + area);
        this.area = area;
    }

    public double getPerimeter() {
        return perimeter;
    }

    public double getArea() {
        return area;
    }
}
```

### 리팩토링 19. 질의 함수와 변경 함수 분리하기(Separate Query from Modifier)
- `눈에 띌만한` 사이드 이펙트 없이 값을 조회할 수 있는 메소드는 테스트하기도 쉽고, 메소드를 이동하기도 편하다
- 명령- 조회 분리(command-query separation) 규칙:
  - 어떤 값을 리턴하는 함수는 사이드 이펙트가 없어야 한다
- `눈에 띌만한(observable)사이드 이펙트`
  - 가령, 캐시는 중요한 객체 상태 변화는 아니다.
  - 따라서 어떤 메소드 호출로 인해, 캐시 데이터를 변경하더라도 분리할 필요는 없다.

### 질의 함수와 변경 함수 분리하기 전 코드
```java
public class Criminal {

    public String alertForMiscreant(List<Person> people) {
        for (Person p : people) {
            if (p.getName().equals("Don")) {
                setOffAlarms();
                return "Don";
            }

            if (p.getName().equals("John")) {
                setOffAlarms();
                return "John";
            }
        }

        return "";
    }

    private void setOffAlarms() {
        System.out.println("set off alarm");
    }
}

public class Billing {

    private Customer customer;

    private EmailGateway emailGateway;

    public Billing(Customer customer, EmailGateway emailGateway) {
        this.customer = customer;
        this.emailGateway = emailGateway;
    }

    public double getTotalOutstandingAndSendBill() {
        double result = customer.getInvoices().stream()
                .map(Invoice::getAmount)
                .reduce((double) 0, Double::sum);
        sendBill();
        return result;
    }

    private void sendBill() {
        emailGateway.send(formatBill(customer));
    }

    private String formatBill(Customer customer) {
        return "sending bill for " + customer.getName();
    }
}
```

### 질의 함수와 변경 함수 분리하기 후 코드
```java

public class Billing {

    private Customer customer;

    private EmailGateway emailGateway;

    public Billing(Customer customer, EmailGateway emailGateway) {
        this.customer = customer;
        this.emailGateway = emailGateway;
    }

    public double totalOutstanding() {
        return customer.getInvoices().stream()
                .map(Invoice::getAmount)
                .reduce((double) 0, Double::sum);

    }

    public void sendBill() {
        emailGateway.send(formatBill(customer));
    }

    private String formatBill(Customer customer) {
        return "sending bill for " + customer.getName();
    }
}

public class Criminal {

    public void alertForMiscreant(List<Person> people) {
        if(!findMiscreant(people).isBlank())
            setOffAlarms();
    }


    public String findMiscreant(List<Person> people) {
        for (Person p : people) {
            if (p.getName().equals("Don")) {
                setOffAlarms();
                return "Don";
            }

            if (p.getName().equals("John")) {
                setOffAlarms();
                return "John";
            }
        }
        return "";
    }

    private void setOffAlarms() {
        System.out.println("set off alarm");
    }
}
```

### 리팩토링 20. 세터 제거하기 (Remove Setting Method)
- 세터를 제공한다는 것은 해당 필드가 변경될 수 있다는 것을 뜻한다.
- 객체 생성시 처음 설정된 값이 변경될 필요가 없다면 해당 값을 설정할 수 있는 
- 생성자를 만들고 세터를 제거해서 변경될 수 있는 가능성을 제거해야 한다.

### 세터 제거하기 전 코드
```java
public class Person {

    private String name;

    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

class PersonTest {

    @Test
    void person() {
        Person person = new Person();
        person.setId(10);
        person.setName("keesun");
        assertEquals(10, person.getId());
        assertEquals("keesun", person.getName());
        person.setName("whiteship");
        assertEquals("whiteship", person.getName());
    }

}
```

### 세터 제거하기 후 코드

```java
public class Person {

    private String name;

    private int id;

    public Person(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

}

class PersonTest {

    @Test
    void person() {
        Person person = new Person(10);
        person.setName("keesun");
        assertEquals(10, person.getId());
        assertEquals("keesun", person.getName());
        person.setName("whiteship");
        assertEquals("whiteship", person.getName());
    }

}
```

### 리팩토링 21. 파생 변수를 질의 함수로 바꾸기(Replace Derived Variable with Query)
- 변경할 수 있는 데이터를 최대한 줄이도록 노력해야 한다.
- 계산해서 알아낼 수 있는 변수는 제거할 수 있다.
  - 계산 자체가 데이터의 의미를 잘 표현하는 경우도 있다.
  - 해당 변수가 어디선가 잘못된 값으로 수정될 수 있는 가능성을 제거할 수 있다.
- 계산에 필요한 데이터가 변하지 않는 값이라면, 계산의 결과에 해당하는
- 데이터 역시 불변 데이터기 때문에 해당 변수는 그대로 유지할 수 있다.

### 파생 변수를 질의 함수로 바꾸기 전 코드
```java
public class Discount {

    private double discountedTotal;
    private double discount;

    private double baseTotal;

    public Discount(double baseTotal) {
        this.baseTotal = baseTotal;
    }

    public double getDiscountedTotal() {
        return this.discountedTotal;
    }

    public void setDiscount(double number) {
        this.discount = number;
        this.discountedTotal = this.baseTotal - this.discount;
    }
}

public class ProductionPlan {

    private double production;
    private List<Double> adjustments = new ArrayList<>();

    public void applyAdjustment(double adjustment) {
        this.adjustments.add(adjustment);
        this.production += adjustment;
    }

    public double getProduction() {
        return this.production;
    }
}
```

### 파생 변수를 질의 함수로 바꾸기 후 코드
```java
public class Discount {

    private double discount;

    private double baseTotal;

    public Discount(double baseTotal) {
        this.baseTotal = baseTotal;
    }

    public double getDiscountedTotal() {
        return this.baseTotal- this.discount;
    }

    public void setDiscount(double number) {
        this.discount = number;
    }
}

public class ProductionPlan {
    private List<Double> adjustments = new ArrayList<>();

    public void applyAdjustment(double adjustment) {
        this.adjustments.add(adjustment);
//        this.production += adjustment;
    }

    public double getProduction() {
        return this.adjustments.stream().mapToDouble(Double::valueOf).sum();
    }

}
```

### 리팩토링 22. 여러 함수를 변환 함수로 묶기 (Combine Functions into Transform)
- 관련있는 여러 파생 변수를 만들어내는 함수가 여러곳에서 만들어지고 사용된다면
- 그러한 파생 변수를 `변환 함수(transform function)`를 통해 한곳으로 모아둘 수 있다.
- 소스 데이터가 변경될 수 있는 경우에는 `여러 함수를 클래스로 묶기(Combine Functions into Class)` 를 사용하는 것이 적절하다
- 소스 데이터가 변경되지 않는 경우에는 두 가지 방법을 모두 사용할 수 있지만, 변환함수를 사용해서
- 불변 데이터의 필드로 생성해두고 재사용할 수도 있다.

### 여러 함수를 변환 함수로 묶기 전 코드
```java
public record Reading(String customer, double quantity, Month month, Year year) {
}

public class Client1 {

    double baseCharge;

    public Client1(Reading reading) {
        this.baseCharge = baseRate(reading.month(), reading.year()) * reading.quantity();
    }

    private double baseRate(Month month, Year year) {
        return 10;
    }

    public double getBaseCharge() {
        return baseCharge;
    }
}

public class Client2 {

    private double base;
    private double taxableCharge;

    public Client2(Reading reading) {
        this.base = baseRate(reading.month(), reading.year()) * reading.quantity();
        this.taxableCharge = Math.max(0, this.base - taxThreshold(reading.year()));
    }

    private double taxThreshold(Year year) {
        return 5;
    }

    private double baseRate(Month month, Year year) {
        return 10;
    }

    public double getBase() {
        return base;
    }

    public double getTaxableCharge() {
        return taxableCharge;
    }
}

public class Client3 {

    private double basicChargeAmount;

    public Client3(Reading reading) {
        this.basicChargeAmount = calculateBaseCharge(reading);
    }

    private double calculateBaseCharge(Reading reading) {
        return baseRate(reading.month(), reading.year()) * reading.quantity();
    }

    private double baseRate(Month month, Year year) {
        return 10;
    }

    public double getBasicChargeAmount() {
        return basicChargeAmount;
    }
}

```

### 여러 함수를 변환 함수로 묶기 후 코드
```java
public class ReadingClient {

    protected double taxThreshold(Year year){
        return 5;
    }

    protected double baseRate(Month month, Year year){
        return 10;
    }

    protected EnrichReading enrichReading(Reading reading){
        return new EnrichReading(reading, baseCharge(reading),taxableCharge(reading));
    }

    private double taxableCharge(Reading reading) {
        return Math.max(0,baseCharge(reading)-taxThreshold(reading.year()));
    }

    private double baseCharge(Reading reading){
        return baseRate(reading.month(),reading.year())*reading.quantity();
    }
}

public record Reading(String customer, double quantity, Month month, Year year) {
}

public record EnrichReading(Reading reading, double baseCharge,double taxableCharge) {
}

public class Client3 extends ReadingClient{

    private double basicChargeAmount;

    public Client3(Reading reading) {
        this.basicChargeAmount = enrichReading(reading).baseCharge();
    }

    public double getBasicChargeAmount() {
        return basicChargeAmount;
    }
}


public class Client2 extends ReadingClient {

    private double base;
    private double taxableCharge;

    public Client2(Reading reading) {
        EnrichReading enrichReading=enrichReading(reading);
        this.base=enrichReading(reading).baseCharge();
        this.taxableCharge=enrichReading.taxableCharge();
    }

    public double getBase() {
        return base;
    }

    public double getTaxableCharge() {
        return taxableCharge;
    }
}

public class Client1 {

    double baseCharge;

    public Client1(Reading reading) {
        this.baseCharge = baseRate(reading.month(), reading.year()) * reading.quantity();
    }

    private double baseRate(Month month, Year year) {
        return 10;
    }

    public double getBaseCharge() {
        return baseCharge;
    }
}
```

### 리팩토링 23. 참조를 값으로 바꾸기 (Change Reference to Value)
- 레퍼런스(Reference) 객체 vs 값(Value) 객체
  - https://martinfowler.com/bliki/ValueObject.html
  - `Objects that are equal due to the value of their properties, in this case their x and y coordinates, are called value objects`
  - 값 객체는 객체가 가진 필드의 값으로 동일성을 확인한다
  - 값 객체는 변하지 않는다
  - 어떤 객체의 변경 내역을 다른 곳으로 전파시키고 싶다면 레퍼런스, 아니라면 값 객체를 사용한다. 

### 참조를 값으로 바꾸기 전 코드
```java
public class TelephoneNumber {

    private String areaCode;

    private String number;

    public String areaCode() {
        return areaCode;
    }

    public void areaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String number() {
        return number;
    }

    public void number(String number) {
        this.number = number;
    }
}

public class Person {

    private TelephoneNumber officeTelephoneNumber;

    public String officeAreaCode() {
        return this.officeTelephoneNumber.areaCode();
    }

    public void officeAreaCode(String areaCode) {
        this.officeTelephoneNumber.areaCode(areaCode);
    }

    public String officeNumber() {
        return this.officeTelephoneNumber.number();
    }

    public void officeNumber(String number) {
        this.officeTelephoneNumber.number(number);
    }

}
```

### 참조를 값으로 바꾸기 후 코드
```java

public class Person {

    private TelephoneNumber officeTelephoneNumber;

    public String officeAreaCode() {
        return this.officeTelephoneNumber.areaCode();
    }

    public void officeAreaCode(String areaCode) {
        this.officeTelephoneNumber = new TelephoneNumber(areaCode,this.officeNumber());
    }

    public String officeNumber() {
        return this.officeTelephoneNumber.number();
    }

    public void officeNumber(String number) {
        this.officeTelephoneNumber = new TelephoneNumber(this.officeAreaCode(),number);
    }

}

public class TelephoneNumber {

    private final String areaCode;

    private final String number;

    public TelephoneNumber(String areaCode, String number) {
        this.areaCode = areaCode;
        this.number = number;
    }

    public String areaCode() {
        return areaCode;
    }


    public String number() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TelephoneNumber that = (TelephoneNumber) o;
        return Objects.equals(areaCode, that.areaCode) && Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(areaCode, number);
    }
}
```