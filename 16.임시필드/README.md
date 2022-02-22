## 냄새 16. 임시 필드 (Temporary Field)
- 클래스에 있는 어떤 필드가 특정한 경우에만 값을 갖는 경우
- 어떤 객체의 필드가 `특정한 경우에만` 값을 가진다는 것을 이해하는 것은 일반적으로 예상하지 못하기 때문에 이해하기 어렵다
- 관련 리팩토링
  - `클래스 추출하기(Extract Class)`를 사용해 해당 변수들을 옮길 수 있다
  - `함수 옮기기(Move Function)`을 사용해서 해당 변수를 사용하는 함수를 특정클래스로 옮길 수 있다
  - `특이 케이스 추가하기(Introduct Special Case)`를 적용해 특정한 경우에 해당하는 클래스를 만들어 해당 조건을 제거할 수 있다.

### 리팩토링 36. 특이 케이스 추가하기 (Introduce Special Case)
- 어떤 필드의 특정한 값에 따라 동일하게 동작하는 코드가 반복적으로 나타난다면 해당 필드를 감싸는 `특별한 케이스`를 만들어 해당조건을 표현할 수 있다
- 이러한 매커니즘을 `특이 케이스 패턴`이라고 부르고 `Null Object패턴`을 여러한 패턴의 특수한 형태로 볼 수 있다.

### 특이 케이스 추가하기 변경 전 코드
```java
public class BasicBillingPlan extends BillingPlan {
}

public class BillingPlan {
}

public class Customer {

    private String name;

    private BillingPlan billingPlan;

    private PaymentHistory paymentHistory;

    public Customer(String name, BillingPlan billingPlan, PaymentHistory paymentHistory) {
        this.name = name;
        this.billingPlan = billingPlan;
        this.paymentHistory = paymentHistory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BillingPlan getBillingPlan() {
        return billingPlan;
    }

    public void setBillingPlan(BillingPlan billingPlan) {
        this.billingPlan = billingPlan;
    }

    public PaymentHistory getPaymentHistory() {
        return paymentHistory;
    }

    public void setPaymentHistory(PaymentHistory paymentHistory) {
        this.paymentHistory = paymentHistory;
    }
}

public class CustomerService {

    public String customerName(Site site) {
        Customer customer = site.getCustomer();

        String customerName;
        if (customer.getName().equals("unknown")) {
            customerName = "occupant";
        } else {
            customerName = customer.getName();
        }

        return customerName;
    }

    public BillingPlan billingPlan(Site site) {
        Customer customer = site.getCustomer();
        return customer.getName().equals("unknown") ? new BasicBillingPlan() : customer.getBillingPlan();
    }

    public int weeksDelinquent(Site site) {
        Customer customer = site.getCustomer();
        return customer.getName().equals("unknown") ? 0 : customer.getPaymentHistory().getWeeksDelinquentInLastYear();
    }

}

public class PaymentHistory {

    private int weeksDelinquentInLastYear;

    public PaymentHistory(int weeksDelinquentInLastYear) {
        this.weeksDelinquentInLastYear = weeksDelinquentInLastYear;
    }

    public int getWeeksDelinquentInLastYear() {
        return this.weeksDelinquentInLastYear;
    }
}

public class Site {

    private Customer customer;

    public Site(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }
}
```

### 특이 케이스 추가하기 변경 후 코드
```java
public class BasicBillingPlan extends BillingPlan {
}

public class BillingPlan {
}

public class Customer {

    private String name;

    private BillingPlan billingPlan;

    private PaymentHistory paymentHistory;

    public Customer(String name, BillingPlan billingPlan, PaymentHistory paymentHistory) {
        this.name = name;
        this.billingPlan = billingPlan;
        this.paymentHistory = paymentHistory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BillingPlan getBillingPlan() {
        return billingPlan;
    }

    public void setBillingPlan(BillingPlan billingPlan) {
        this.billingPlan = billingPlan;
    }

    public PaymentHistory getPaymentHistory() {
        return paymentHistory;
    }

    public void setPaymentHistory(PaymentHistory paymentHistory) {
        this.paymentHistory = paymentHistory;
    }

    public boolean isUnknown() {
        return false;
    }
}

public class CustomerService {

    public String customerName(Site site) {
        return site.getCustomer().getName();
    }

    public BillingPlan billingPlan(Site site) {
        return site.getCustomer().getBillingPlan();
    }

    public int weeksDelinquent(Site site) {
        return site.getCustomer().getPaymentHistory().getWeeksDelinquentInLastYear();
    }

}

public class NullPaymentHistory extends PaymentHistory{
    public NullPaymentHistory() {
        super(0);
    }
}

public class PaymentHistory {

    private int weeksDelinquentInLastYear;

    public PaymentHistory(int weeksDelinquentInLastYear) {
        this.weeksDelinquentInLastYear = weeksDelinquentInLastYear;
    }

    public int getWeeksDelinquentInLastYear() {
        return this.weeksDelinquentInLastYear;
    }
}

public class Site {

    private Customer customer;

    public Site(Customer customer) {
        this.customer = customer.getName().equals("unknown") ? new UnknownCustomer() : customer;
    }

    public Customer getCustomer() {
        return customer;
    }
}

public class UnknownCustomer extends Customer{
    public UnknownCustomer() {
        super("unknown",new BasicBillingPlan(),new NullPaymentHistory());
    }

    @Override
    public boolean isUnknown() {
        return true;
    }

    @Override
    public String getName() {
        return "occupant";
    }
}
```