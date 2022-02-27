### 냄새 21. 서로 다른 인터페이스의 대안 클래스들(Alternative classes with defferenct interfaces)

- 비슷한 일을 여러 곳에서 서로 다른 규약을 사용해 지원하고 있는 코드 냄새
- 대안 클래스로 사용하려면 동일한 인터페이스를 구현하고 있어야 한다
- `함수 선언 변경하기(Change Function Declaration)`와 `함수 옮기기(Move Function)`을 사용해서
- 서로 동일한 인터페이스를 구현하게끔 코드를 수정할 수 있다
- 두 클래스에서 일부 코드가 중복되는 경우에는 `슈퍼클래스 추출하기(Extract Superclass)`를 사용해 중복된 코드를
- 슈퍼클래스로 옮기고 두 클래스를 새로운 슈퍼클래스의 서브클래스로 만들 수 있다.

### 서로 다른 인터페이스의 대안 클래스들 변경 전 코드
```java
public class AlertMessage {

    public void setMessage(String message) {

    }

    public void setFor(String email) {

    }
}

public interface AlertService {
    void add(AlertMessage alertMessage);
}

public class EmailMessage {
    public void setTitle(String title) {

    }

    public void setTo(String to) {

    }

    public void setFrom(String from) {

    }
}

public interface EmailService {
    void sendEmail(EmailMessage emailMessage);
}

public class Notification {

    private String title;
    private String receiver;
    private String sender;

    private Notification(String title) {
        this.title = title;
    }

    public static Notification newNotification(String title) {
        return new Notification(title);
    }

    public Notification receiver(String receiver) {
        this.receiver = receiver;
        return this;
    }

    public Notification sender(String sender) {
        this.sender = sender;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSender() {
        return sender;
    }
}

public class Order {
    public String getEmail() {
        return null;
    }
}

public class OrderAlerts {

    private AlertService alertService;
    
    public void alertShipped(Order order) {
        AlertMessage alertMessage = new AlertMessage();
        alertMessage.setMessage(order.toString() + " is shipped");
        alertMessage.setFor(order.getEmail());
        alertService.add(alertMessage);
    }
}

public class OrderProcessor {

    private EmailService emailService;

    public void notifyShipping(Shipping shipping) {
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setTitle(shipping.getOrder() + " is shipped");
        emailMessage.setTo(shipping.getEmail());
        emailMessage.setFrom("no-reply@whiteship.com");
        emailService.sendEmail(emailMessage);
    }

}

public class Shipping {
    public String getOrder() {
        return "Order 11231";
    }

    public String getEmail() {
        return "aaa@email.com";
    }
}
```

### 서로 다른 인터페이스의 대안 클래스들 변경 후 코드
```java
public class AlertMessage {

    public void setMessage(String message) {

    }

    public void setFor(String email) {

    }
}

public class AlertNotificationService implements NotificationService{

    private AlertService alertService;
    @Override
    public void sendNotification(Notification notification) {
        AlertMessage alertMessage = new AlertMessage();
        alertMessage.setMessage(notification.getTitle());
        alertMessage.setFor(notification.getReceiver());
        alertService.add(alertMessage);
    }
}

public class EmailMessage {
    public void setTitle(String title) {

    }

    public void setTo(String to) {

    }

    public void setFrom(String from) {

    }
}

public class EmailNotificationService implements NotificationService{

    private EmailService emailService;
    @Override
    public void sendNotification(Notification notification) {
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setTitle(notification.getTitle());
        emailMessage.setTo(notification.getReceiver());
        emailMessage.setFrom("no-reply@whiteship.com");
        emailService.sendEmail(emailMessage);
    }
}

public interface EmailService {
    void sendEmail(EmailMessage emailMessage);
}

public class Notification {

    private String title;
    private String receiver;
    private String sender;

    private Notification(String title) {
        this.title = title;
    }

    public static Notification newNotification(String title) {
        return new Notification(title);
    }

    public Notification receiver(String receiver) {
        this.receiver = receiver;
        return this;
    }

    public Notification sender(String sender) {
        this.sender = sender;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSender() {
        return sender;
    }
}

public interface NotificationService {
    void sendNotification(Notification notification);
}

public class Order {
    public String getEmail() {
        return null;
    }
}

public class OrderAlerts {

    private NotificationService notificationService;

    public OrderAlerts(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void alertShipped(Order order) {
        Notification notification = Notification.newNotification(order.toString()+" is shipped")
                .receiver(order.getEmail());
        notificationService.sendNotification(notification);
    }

}

public class OrderProcessor {

    private NotificationService notificationService;

    public OrderProcessor(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void notifyShipping(Shipping shipping) {
        Notification notification=Notification.newNotification(shipping.getOrder()+" is shipped")
                .receiver(shipping.getEmail())
                .sender("no-reply@whiteship.com");
        notificationService.sendNotification(notification);
    }

}

public class Shipping {
    public String getOrder() {
        return "Order 11231";
    }

    public String getEmail() {
        return "aaa@email.com";
    }
}

```