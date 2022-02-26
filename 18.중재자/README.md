## 냄새 18. 중재자(Middle Man)
- 캡슐화를 통해 내부의 구체적인 정보를 최대한 감출 수 있다.
- 그러나, 어떤 클래스의 메소드가 대부분 다른 클래스로 메소드 호출을 위임하고 있다면 중재자를 제거하고
- 클라이언트가 해당 클래스를 직접 사용하도록 코드를 개선할 수 있다.
- 관련 리팩토링
  - `중재자 제거하기(Remove Middle Man)` 리팩토링을 사용해 클라이언트가 필요한 클래스를 직접 사용하도록 개선할 수 있다
  - `함수 인라인(Inline Function)`을 사용해서 메소드 호출한 쪽으로 코드를 보내서 중재자를 없앨 수도있다.
  - `슈퍼클래스를 위임으로 바꾸기(Replace Superclass with Delegate)`
  - `서브클래스를 위임으로 바꾸기(Replace Subclass with Delegate)`

### 리팩토링 38. 중재자 제거하기(Remove Middle Man)
- `위임 숨기기`의 반대에 해당하는 리팩토링
- 필요한 캡슐화의 정도는 시간에 따라 그리고 상황에 따라 바뀔 수 있다
- 캡슐화의 정도를 `중재자 제거하기`와 `위임 숨기기` 리팩토링을 통해 조절할 수 있다.
- 위임하고 있는 객체를 클라이언트가 사용할 수 있도록 getter를 제공하고, 클라이언트는 메시지 체인을 사용하도록
- 코드를 고친뒤에 캡슐화에 사용했던 메소드를 제거한다.
- Law of Demeter를 지나치게 따르기 보다는 상황에 맞게 활용하도록 하자
  - 디미터의 법칙, `가장 가까운 객체만 사용한다`

### 중재자 제거하기 변경 전 코드
```java
public class Person {

    private Department department;

    private String name;

    public Person(String name, Department department) {
        this.name = name;
        this.department = department;
    }

    public Person getManager() {
        return this.department.getManager();
    }
}

public class Department {
    private Person manager;

    public Department(Person manager) {
        this.manager = manager;
    }

    public Person getManager() {
        return manager;
    }
}
```

### 중재자 제거하기 변경 후 코드
```java

public class Department {

    private Person manager;

    public Department(Person manager) {
        this.manager = manager;
    }

    public Person getManager() {
        return manager;
    }
}

public class Person {

    private Department department;

    private String name;

    public Person(String name, Department department) {
        this.name = name;
        this.department = department;
    }

    public Department getDepartment() {
        return department;
    }
}
```

### 리팩토링 39. 슈퍼클래스를 위임으로 바꾸기 (Replace Superclass with Delegate)
- 객체지향에서 `상속`은 기존의 기능을 재사용하는 쉬우면서 강력한 방법이지만 때로는 적절하지 않은 경우도 있다
- 서브클래스는 슈퍼클래스의 모든 기능을 지원해야 한다
  - Stack이라는 자료구조를 만들 때 List를 상속받는것이 좋을까?
- 서브클래스는 슈퍼클래스 자리를 대체하더라도 잘 동작해야 한다.
  - 리스코프 치환 원칙
- 서브클래스는 슈퍼클래스의 변경에 취약하다.
- 그렇다면 상속을 사용하지 않는 것이 좋은가?
  - 상속은 적절한 경우에 사용한다면 매우 쉽고 효율적인 방법이다
  - 따라서 우선 상속을 적용한 이후에, 적절치 않다고 판단이 된다면 그때에 이 리팩토링을 적용하자.

### 슈퍼클래스를 위임으로 바꾸기 변경 전 코드
```java
public class CategoryItem {

    private Integer id;

    private String title;

    private List<String> tags;

    public CategoryItem(Integer id, String title, List<String> tags) {
        this.id = id;
        this.title = title;
        this.tags = tags;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean hasTag(String tag) {
        return this.tags.contains(tag);
    }
}

public class Scroll extends CategoryItem {

    private LocalDate dateLastCleaned;

    public Scroll(Integer id, String title, List<String> tags, LocalDate dateLastCleaned) {
        super(id, title, tags);
        this.dateLastCleaned = dateLastCleaned;
    }

    public long daysSinceLastCleaning(LocalDate targetDate) {
        return this.dateLastCleaned.until(targetDate, ChronoUnit.DAYS);
    }
}
```

### 슈퍼클래스를 위임으로 바꾸기 변경 후 코드
```java
public class CategoryItem {

    private Integer id;

    private String title;

    private List<String> tags;

    public CategoryItem(Integer id, String title, List<String> tags) {
        this.id = id;
        this.title = title;
        this.tags = tags;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean hasTag(String tag) {
        return this.tags.contains(tag);
    }
}

public class Scroll {

    private LocalDate dateLastCleaned;

    private CategoryItem categoryItem;

    public Scroll(Integer id, String title, List<String> tags, LocalDate dateLastCleaned) {
        this.dateLastCleaned = dateLastCleaned;
        this.categoryItem=new CategoryItem(id,title,tags);
    }

    public long daysSinceLastCleaning(LocalDate targetDate) {
        return this.dateLastCleaned.until(targetDate, ChronoUnit.DAYS);
    }
}

```

### 리팩토링 40. 서브클래스를 위임으로 바꾸기(Replace Subclass with Delegate)
- 어떤 객체의 행동이 카테고리에 따라 바뀐다면, 보통 상속을 사용해서 일반적인 로직은 슈퍼클래스에두고 
- 특이한 케이스에 해당하는 로직을 서브클래스를 사용해 표현한다
- 하지만, 대부분의 프로그래밍 언어에서 상속은 오직 한번만 사용할 수 있다.
  - 만약에 어떤 객체를 두가지 이상의 카테고리로 구분해야 한다면?
  - 위임을 사용하면 얼마든지 여러가지 이유로 여러 다른 객체로 위임을 할 수 있다.
- 슈퍼클래스가 바뀌면 모든 서브클래스에 영향을 줄 수 있다. 따라서 슈퍼클래스를 변경할 때 서브클래스까지 신경써야한다
  - 만약에 서브클래스가 전혀 다른 모듈에 있다면?
  - 위임을 사용한다면 중간에 인터페이스를 만들어 의존성을 줄일 수 있다
- `상속 대신 위임을 선호하라`는 결코 `상속은 나쁘다`라는 말이 아니다
  - 처음엔 상속을 적용하고 언제든지 이런 리팩토링을 사용해 위임으로 전환할 수 있다.


### 서브클래스를 위임으로 바꾸기 변경 전 코드
```java
public class Booking {

    protected Show show;

    protected LocalDateTime time;

    public Booking(Show show, LocalDateTime time) {
        this.show = show;
        this.time = time;
    }

    public boolean hasTalkback() {
        return this.show.hasOwnProperty("talkback") && !this.isPeakDay();
    }

    protected boolean isPeakDay() {
        DayOfWeek dayOfWeek = this.time.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    public double basePrice() {
        double result = this.show.getPrice();
        if (this.isPeakDay()) result += Math.round(result * 0.15);
        return result;
    }

}

public class PremiumBooking extends Booking {

    private PremiumExtra extra;

    public PremiumBooking(Show show, LocalDateTime time, PremiumExtra extra) {
        super(show, time);
        this.extra = extra;
    }

    @Override
    public boolean hasTalkback() {
        return this.show.hasOwnProperty("talkback");
    }

    @Override
    public double basePrice() {
        return Math.round(super.basePrice() + this.extra.getPremiumFee());
    }

    public boolean hasDinner() {
        return this.extra.hasOwnProperty("dinner") && !this.isPeakDay();
    }
}

public class PremiumExtra {

    private List<String> properties;

    private double premiumFee;

    public PremiumExtra(List<String> properties, double premiumFee) {
        this.properties = properties;
        this.premiumFee = premiumFee;
    }

    public double getPremiumFee() {
        return premiumFee;
    }

    public boolean hasOwnProperty(String property) {
        return this.properties.contains(property);
    }
}

public class Show {

    private List<String> properties;

    private double price;

    public Show(List<String> properties, double price) {
        this.properties = properties;
        this.price = price;
    }

    public boolean hasOwnProperty(String property) {
        return this.properties.contains(property);
    }

    public double getPrice() {
        return price;
    }
}


// 테스트 코드

class BookingTest {
    @Test
    void basePrice() {
        Show lionKing = new Show(List.of(), 120);
        LocalDateTime weekday = LocalDateTime.of(2022, 1, 20, 19, 0);

        Booking booking = new Booking(lionKing, weekday);
        assertEquals(120, booking.basePrice());

        Booking premium = new PremiumBooking(lionKing, weekday, new PremiumExtra(List.of(), 50));
        assertEquals(170, premium.basePrice());
    }

    @Test
    void basePrice_on_peakDay() {
        Show lionKing = new Show(List.of(), 120);
        LocalDateTime weekend = LocalDateTime.of(2022, 1, 15, 19, 0);

        Booking booking = new Booking(lionKing, weekend);
        assertEquals(138, booking.basePrice());

        Booking premium = new PremiumBooking(lionKing, weekend, new PremiumExtra(List.of(), 50));
        assertEquals(188, premium.basePrice());
    }

    @Test
    void talkback() {
        Show lionKing = new Show(List.of(), 120);
        Show aladin = new Show(List.of("talkback"), 120);
        LocalDateTime weekday = LocalDateTime.of(2022, 1, 20, 19, 0);
        LocalDateTime weekend = LocalDateTime.of(2022, 1, 15, 19, 0);

        assertFalse(new Booking(lionKing, weekday).hasTalkback());
        assertTrue(new Booking(aladin, weekday).hasTalkback());
        assertFalse(new Booking(aladin, weekend).hasTalkback());

        PremiumExtra premiumExtra = new PremiumExtra(List.of(), 50);
        assertTrue(new PremiumBooking(aladin, weekend, premiumExtra).hasTalkback());
        assertFalse(new PremiumBooking(lionKing, weekend, premiumExtra).hasTalkback());
    }

    @Test
    void hasDinner() {
        Show lionKing = new Show(List.of(), 120);
        LocalDateTime weekday = LocalDateTime.of(2022, 1, 20, 19, 0);
        LocalDateTime weekend = LocalDateTime.of(2022, 1, 15, 19, 0);
        PremiumExtra premiumExtra = new PremiumExtra(List.of("dinner"), 50);

        assertTrue(new PremiumBooking(lionKing, weekday, premiumExtra).hasDinner());
        assertFalse(new PremiumBooking(lionKing, weekend, premiumExtra).hasDinner());
    }
}
```

### 서브클래스를 위임으로 바꾸기 변경 후 코드
```java
public class Booking {

    protected Show show;

    protected LocalDateTime time;

    protected PremiumDelegate premiumDelegate;


    public Booking(Show show, LocalDateTime time) {
        this.show = show;
        this.time = time;
    }

    public static Booking createBook(Show show, LocalDateTime time){
        return new Booking(show, time);
    }

    public static Booking createPremiumBook(Show show, LocalDateTime time,PremiumExtra extra){
        Booking booking = createBook(show, time);
        booking.premiumDelegate=new PremiumDelegate(booking,extra);
        return booking;

    }

    public boolean hasTalkback() {
        return (this.premiumDelegate!=null ) ? this.premiumDelegate.hasTalkback() : this.show.hasOwnProperty("talkback")&&!this.isPeakDay();
    }

    protected boolean isPeakDay() {
        DayOfWeek dayOfWeek = this.time.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    public double basePrice() {
        double result = this.show.getPrice();
        if (this.isPeakDay()) result += Math.round(result * 0.15);
        return (this.premiumDelegate != null) ? this.premiumDelegate.extendBasePrice(result) : result;
    }
    public boolean hasDinner() {
        return this.premiumDelegate != null && this.premiumDelegate.hasDinner();
    }
}

public class PremiumDelegate {
    private Booking host;

    private PremiumExtra extra;

    public PremiumDelegate(Booking host, PremiumExtra extra) {
        this.host = host;
        this.extra = extra;
    }

    public boolean hasTalkback() {
        return this.host.show.hasOwnProperty("talkback");
    }

    public double extendBasePrice(double result) {
        return Math.round(result+this.extra.getPremiumFee());
    }

    public boolean hasDinner() {
        return this.extra.hasOwnProperty("dinner") && !host.isPeakDay();
    }
}

public class PremiumExtra {

    private List<String> properties;

    private double premiumFee;

    public PremiumExtra(List<String> properties, double premiumFee) {
        this.properties = properties;
        this.premiumFee = premiumFee;
    }

    public double getPremiumFee() {
        return premiumFee;
    }

    public boolean hasOwnProperty(String property) {
        return this.properties.contains(property);
    }
}

public class Show {

    private List<String> properties;

    private double price;

    public Show(List<String> properties, double price) {
        this.properties = properties;
        this.price = price;
    }

    public boolean hasOwnProperty(String property) {
        return this.properties.contains(property);
    }

    public double getPrice() {
        return price;
    }
}

class BookingTest {
    @Test
    void basePrice() {
        Show lionKing = new Show(List.of(), 120);
        LocalDateTime weekday = LocalDateTime.of(2022, 1, 20, 19, 0);

        Booking booking = Booking.createBook(lionKing, weekday);
        assertEquals(120, booking.basePrice());

        Booking premium = Booking.createPremiumBook(lionKing, weekday, new PremiumExtra(List.of(), 50));
        assertEquals(170, premium.basePrice());
    }

    @Test
    void basePrice_on_peakDay() {
        Show lionKing = new Show(List.of(), 120);
        LocalDateTime weekend = LocalDateTime.of(2022, 1, 15, 19, 0);

        Booking booking = Booking.createBook(lionKing, weekend);
        assertEquals(138, booking.basePrice());

        Booking premium = Booking.createPremiumBook(lionKing, weekend, new PremiumExtra(List.of(), 50));
        assertEquals(188, premium.basePrice());
    }

    @Test
    void talkback() {
        Show lionKing = new Show(List.of(), 120);
        Show aladin = new Show(List.of("talkback"), 120);
        LocalDateTime weekday = LocalDateTime.of(2022, 1, 20, 19, 0);
        LocalDateTime weekend = LocalDateTime.of(2022, 1, 15, 19, 0);

        assertFalse(Booking.createBook(lionKing, weekday).hasTalkback());
        assertTrue(Booking.createBook(aladin, weekday).hasTalkback());
        assertFalse(Booking.createBook(aladin, weekend).hasTalkback());

        PremiumExtra premiumExtra = new PremiumExtra(List.of(), 50);
        assertTrue(Booking.createPremiumBook(aladin, weekend, premiumExtra).hasTalkback());
        assertFalse(Booking.createPremiumBook(lionKing, weekend, premiumExtra).hasTalkback());
    }

    @Test
    void hasDinner() {
        Show lionKing = new Show(List.of(), 120);
        LocalDateTime weekday = LocalDateTime.of(2022, 1, 20, 19, 0);
        LocalDateTime weekend = LocalDateTime.of(2022, 1, 15, 19, 0);
        PremiumExtra premiumExtra = new PremiumExtra(List.of("dinner"), 50);

        assertTrue(Booking.createPremiumBook(lionKing, weekday, premiumExtra).hasDinner());
        assertFalse(Booking.createPremiumBook(lionKing, weekend, premiumExtra).hasDinner());
        assertFalse(Booking.createBook(lionKing, weekend).hasDinner());

    }
}

```