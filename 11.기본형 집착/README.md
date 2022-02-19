## 냄새 11. 기본형 집착
- 애플리케이션이 다루고 있는 도메인에 필요한 기본 타입을 만들지 않고 프로그래밍 언어가 제공하는 기본타입을 사용하는 경우가 많다
  - 예) 전화번호, 좌퓨ㅛ, 돈, 범위 , 수량 등
- 기본형으로는 단위(인치 vs 미터) 또는 표기법을 표현하기 어렵다
- 관련 리팩토링 기술
  - `기본형을 객체로 바꾸기(Replace Primitive with Object)`
  - `타입 코드를 서브클래스로 바꾸기(Replace Type Code with Subclasses)`
  - `조건부 로직을 다형성으로 바꾸기(Replace Conditional with Polymorphism)`
  - `클래스 추출하기(Extract Class)`
  - `매개변수 객체 만들기(Introduce Parameter Object)`

### 리팩토링 30. 기본형을 객체로 바꾸기 (Replace Primitive with Object)
- 개발 초기에는 기본형(숫자 또는 문자열)으로 표현한 데이터가 나중에는 해당 데이터와 관련있는 다양한 기능을 필요로 하는 경우가 발생한다
  - 예) 문자열로 표현하던 전화번호의 지역 코드가 필요하거나 다양한 포맷을 지원하는 경우
  - 예) 숫자로 표현하던 온도의 단위(화씨, 섭씨)를 변환하는 경우
- 기본형을 사용한 데이터를 감싸 줄 클래스를 만들면, 필요한 기능을 추가할 수 있다.

### 기본형을 객체로 바꾸기 전 코드
```java
public class Order {

    private String priority;

    public Order(String priority) {
        this.priority = priority;
    }

    public String getPriority() {
        return priority;
    }
}

public class OrderProcessor {

    public long numberOfHighPriorityOrders(List<Order> orders) {
        return orders.stream()
                .filter(o -> o.getPriority() == "high" || o.getPriority() == "rush")
                .count();
    }
}
```
### 기본형을 객체로 바꾸기 후 코드
```java
public class Priority {
    private String value;

    private List<String> legalValues=List.of("low","normal","high","rush");

    public Priority(String value) {
        if(legalValues.contains(value))
            this.value = value;
        else
            throw new IllegalArgumentException("illegal value for priority "+value);
    }

    @Override
    public String toString() {
        return this.value;
    }

    private int index(){
        return this.legalValues.indexOf(this.value);
    }

    public boolean higherThan(Priority other){
        return this.index() > other.index();
    }
}

public class Order {

    private Priority priority;

    public Order(String priorityValue) {
        this(new Priority(priorityValue));
    }

    public Order(Priority priority){
        this.priority=priority;
    }

    public Priority getPriority() {
        return priority;
    }
}

public class OrderProcessor {

    public long numberOfHighPriorityOrders(List<Order> orders) {
        return orders.stream()
                .filter(o -> o.getPriority().higherThan(new Priority("normal")))
                .count();
    }
}
```

### 리팩토링 31. 타입 코드를 서브클래스로 바꾸기(Replace Type Code with Subclasses)
- 비슷하지만 다른 것들을 표현해야 하는 경우, 문자열(String),열거형(enum),숫자(int) 등으로 표현하기도 한다
  - 예) 주문 타입, "일반주문","빠른주문"
  - 예) 직원 타입, "엔지니어","매니저","세일즈"
- 타입을 서브클래스로 바꾸는 계기
  - 조건문을 다형성으로 표현할 수 있을때, 서브클래스를 만들고 `조건무 로직을 다형성으로 바꾸기`를 적용한다
  - 특정 타입에만 유효한 필드가 있을때, 서브클래스를 만들고 `필드내리기`를 활용한다.

### 타입 코드를 서브클래스로 변경 전 코드 (직접 클래스 상속하는 서브클래스 만들 수 있는 경우)
```java
public class Employee {

    private String name;

    private String type;

    public Employee(String name, String type) {
        this.validate(type);
        this.name = name;
        this.type = type;
    }

    private void validate(String type) {
        List<String> legalTypes = List.of("engineer", "manager", "salesman");
        if (!legalTypes.contains(type)) {
            throw new IllegalArgumentException(type);
        }
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
```
### 타입 코드를 서브클래스로 변경 후 코드 (직접 클래스 상속하는 서브클래스 만들 수 있는 경우)
```java
public abstract class Employee {

    private String name;


    protected Employee(String name) {
        this.name = name;
    }

    public static Employee createEmployee(String name, String type){
        return switch (type) {
            case "engineer" -> new Engineer(name);
            case "manager" -> new Manager(name);
            case "salesman" -> new Salesman(name);
            default -> throw new IllegalArgumentException(type);
        };
    }

    private void validate(String type) {
        List<String> legalTypes = List.of("engineer", "manager", "salesman");
        if (!legalTypes.contains(type)) {
            throw new IllegalArgumentException(type);
        }
    }

    protected abstract String getType();

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", type='" + getType() + '\'' +
                '}';
    }
}

public class Engineer extends Employee{

    public Engineer(String name) {
        super(name);
    }

    @Override
    public String getType() {
        return "engineer";
    }
}
public class Manager extends Employee{

    public Manager(String name) {
        super(name);
    }

    @Override
    public String getType() {
        return "manager";
    }
}
public class Salesman extends Employee{

    public Salesman(String name) {
        super(name);
    }

    @Override
    public String getType() {
        return "salesman";
    }
}
```



### 타입 코드를 서브클래스로 변경 전 코드 (간접 상속 활용하는 방법)
```java
public class Employee {

    private String name;

    private String type;

    public Employee(String name, String type) {
        this.validate(type);
        this.name = name;
        this.type = type;
    }

    private void validate(String type) {
        List<String> legalTypes = List.of("engineer", "manager", "salesman");
        if (!legalTypes.contains(type)) {
            throw new IllegalArgumentException(type);
        }
    }

    public String capitalizedType() {
        return this.type.substring(0, 1).toUpperCase() + this.type.substring(1).toLowerCase();
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}

public class FullTimeEmployee extends Employee {
    public FullTimeEmployee(String name, String type) {
        super(name, type);
    }
}

public class PartTimeEmployee extends Employee {
    public PartTimeEmployee(String name, String type) {
        super(name, type);
    }
}
```

### 타입 코드를 서브클래스로 변경 후 코드 (간접 상속 활용하는 방법)
```java
public class Employee {

    private String name;

    private EmployType type;

    public Employee(String name, String typeValue) {
        this.name = name;
        this.type=this.employeeType(typeValue);
    }

    private EmployType employeeType(String typeValue) {
        return switch (typeValue) {
            case "engineer" -> new Engineer();
            case "manager" -> new Manager();
            case "salesman" -> new Salesman();
            default -> throw new IllegalArgumentException(typeValue);
        };
    }

    private void validate(String type) {
        List<String> legalTypes = List.of("engineer", "manager", "salesman");
        if (!legalTypes.contains(type)) {
            throw new IllegalArgumentException(type);
        }
    }

    public String capitalizedType() {
        return this.type.capitalizedType();
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", type='" + type.toString() + '\'' +
                '}';
    }
}

public class EmployType {
    public String capitalizedType() {
        return this.toString().substring(0, 1).toUpperCase() + this.toString().substring(1).toLowerCase();
    }
}

public class Engineer extends EmployType{

    @Override
    public String toString(){
        return "engineer";
    }
}

public class Manager extends EmployType{

    @Override
    public String toString(){
        return "manager";
    }
}

public class Salesman extends EmployType{

    @Override
    public String toString(){
        return "salesman";
    }
}
```

### 리팩토링 32. 조건부 로직을 다형성으로 바꾸기 (Replace Conditional with Polymorphism)
- 복잡한 조건식을 상속과 다형성을 사용해 코드를 보다 명확하게 분리할 수 있다ㅣ
- swich 문을 사용해서 타입에 따라 각기 다른 로직을 사용하는 코드
- 기본 동작과(타입에 따른) 특수한 기능이 섞여있는 경우에 상속 구조를 만들어서 기본 동작을
- 상위 클래스에 두고 특수한 기능을 하위 클래스로 옮겨서 각 타입에 따른 `차이점`을 강조할 수 있다
- 모든 조건문을 다형성으로 옮겨야 하는가? 단순한 조건문은 그대로 두어도 좋다.
- 오직 복잡한 조건문을 다형성을 활용해 좀 더 나은 코드로 만들 수 있는 경우에만 적용한다(과용을 조심하자.)

### 조건부 로직을 다형성을 바꾸기 변경 전 코드(switch)
```java
public class Employee {

    private String type;

    private List<String> availableProjects;

    public Employee(String type, List<String> availableProjects) {
        this.type = type;
        this.availableProjects = availableProjects;
    }

    public int vacationHours() {
        return switch (type) {
            case "full-time" -> 120;
            case "part-time" -> 80;
            case "temporal" -> 32;
            default -> 0;
        };
    }

    public boolean canAccessTo(String project) {
        return switch (type) {
            case "full-time" -> true;
            case "part-time", "temporal" -> this.availableProjects.contains(project);
            default -> false;
        };
    }
}
```

### 조건부 로직을 다형성을 바꾸기 변경 후 코드(switch)
```java
public abstract class Employee {

    protected String type;

    protected List<String> availableProjects;

    public Employee(String type, List<String> availableProjects) {
        this.type = type;
        this.availableProjects = availableProjects;
    }

    public Employee(List<String> availableProjects) {
        this.availableProjects = availableProjects;
    }

    public Employee() {
    }

    public abstract int vacationHours();

    public boolean canAccessTo(String project) {
        return this.availableProjects.contains(project);
    }
}

public class FullTimeEmployee extends Employee{
    @Override
    public int vacationHours() {
        return 120;
    }

    @Override
    public boolean canAccessTo(String project) {
        return true;
    }
}
public class PartTimeEmployee extends Employee{

    public PartTimeEmployee(List<String> availableProjects) {
        super(availableProjects);
    }

    @Override
    public int vacationHours() {
        return 80;
    }
}

public class TemporalEmployee extends Employee{

    public TemporalEmployee(List<String> availableProjects) {
        super(availableProjects);
    }

    @Override
    public int vacationHours() {
        return 32;
    }

}
```

### 조건부 로직을 다형성을 바꾸기 변경 전 코드(variation)
```java
public record Voyage(String zone, int length) {
}

public record VoyageHistory(String zone, int profit) {
}

public class VoyageRating {

    private Voyage voyage;

    private List<VoyageHistory> history;

    public VoyageRating(Voyage voyage, List<VoyageHistory> history) {
        this.voyage = voyage;
        this.history = history;
    }

    public char value() {
        final int vpf = this.voyageProfitFactor();
        final int vr = this.voyageRisk();
        final int chr = this.captainHistoryRisk();
        return (vpf * 3 > (vr + chr * 2)) ? 'A' : 'B';
    }

    private int captainHistoryRisk() {
        int result = 1;
        if (this.history.size() < 5) result += 4;
        result += this.history.stream().filter(v -> v.profit() < 0).count();
        if (this.voyage.zone().equals("china") && this.hasChinaHistory()) result -= 2;
        return Math.max(result, 0);
    }

    private int voyageRisk() {
        int result = 1;
        if (this.voyage.length() > 4) result += 2;
        if (this.voyage.length() > 8) result += this.voyage.length() - 8;
        if (List.of("china", "east-indies").contains(this.voyage.zone())) result += 4;
        return Math.max(result, 0);
    }

    private int voyageProfitFactor() {
        int result = 2;

        if (this.voyage.zone().equals("china")) result += 1;
        if (this.voyage.zone().equals("east-indies")) result +=1 ;
        if (this.voyage.zone().equals("china") && this.hasChinaHistory()) {
            result += 3;
            if (this.history.size() > 10) result += 1;
            if (this.voyage.length() > 12) result += 1;
            if (this.voyage.length() > 18) result -= 1;
        } else {
            if (this.history.size() > 8) result +=1 ;
            if (this.voyage.length() > 14) result -= 1;
        }

        return result;
    }

    private boolean hasChinaHistory() {
        return this.history.stream().anyMatch(v -> v.zone().equals("china"));
    }
}
```

### 조건부 로직을 다형성을 바꾸기 변경 후 코드(variation)
```java
public class VoyageRating {

    protected Voyage voyage;

    protected List<VoyageHistory> history;

    public VoyageRating(Voyage voyage, List<VoyageHistory> history) {
        this.voyage = voyage;
        this.history = history;
    }

    public char value() {
        final int vpf = this.voyageProfitFactor();
        final int vr = this.voyageRisk();
        final int chr = this.captainHistoryRisk();
        return (vpf * 3 > (vr + chr * 2)) ? 'A' : 'B';
    }

    protected int captainHistoryRisk() {
        int result = 1;
        if (this.history.size() < 5) result += 4;
        result += this.history.stream().filter(v -> v.profit() < 0).count();
        return Math.max(result, 0);
    }

    private int voyageRisk() {
        int result = 1;
        if (this.voyage.length() > 4) result += 2;
        if (this.voyage.length() > 8) result += this.voyage.length() - 8;
        if (List.of("china", "east-indies").contains(this.voyage.zone())) result += 4;
        return Math.max(result, 0);
    }

    protected int voyageProfitFactor() {
        int result = 2;
        if (this.voyage.zone().equals("china")) result += 1;
        if (this.voyage.zone().equals("east-indies")) result +=1 ;
        result += voyageLengthFactor();
        result += historyLengthFactor();

        return result;
    }

    protected int voyageLengthFactor() {
        return this.voyage.length()> 14 ? -1:0;
    }

    protected int historyLengthFactor() {
        return  (this.history.size() > 8)  ? 1 :0 ;
    }

    private boolean hasChinaHistory() {
        return this.history.stream().anyMatch(v -> v.zone().equals("china"));
    }


}

public record VoyageHistory(String zone, int profit) {
}

public record Voyage(String zone, int length) {
}

public class RatingFactory {

    public static VoyageRating createRating(Voyage voyage, List<VoyageHistory> history){
        if(voyage.zone().equals("china") && hasChinaHistory(history)){
           return new ChinaExperiencedVoyageRating(voyage, history);
        }else{
           return new VoyageRating(voyage, history);
        }
    }
    private static boolean hasChinaHistory(List<VoyageHistory> history) {
        return history.stream().anyMatch(v->v.zone().equals("china"));
    }

}


public class ChinaExperiencedVoyageRating extends VoyageRating{

    public ChinaExperiencedVoyageRating(Voyage voyage, List<VoyageHistory> history) {
        super(voyage, history);
    }

    @Override
    protected int captainHistoryRisk() {
        int result= super.captainHistoryRisk()-2;
        return Math.max(result, 0);
    }

    @Override
    protected int voyageProfitFactor() {
        return super.voyageProfitFactor()+3;
    }

    @Override
    protected int voyageLengthFactor() {
        int result=0;
        if (this.voyage.length() > 12) result += 1;
        if (this.voyage.length() > 18) result -= 1;
        return result;
    }

    @Override
    protected int historyLengthFactor() {
        return this.history.size()>10 ? 1:0;
    }
}
```