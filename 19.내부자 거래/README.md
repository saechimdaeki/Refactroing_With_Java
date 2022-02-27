## 냄새 19. 내부자 거래(Insider Trading)

- 어떤 모듈이 다른 모듈의 내부 정보를 지나치게 많이 알고 있는 코드 냄새. 
- 그로인해 지나치게 강한 결합도 (coupling)가 생길 수 있다.
- 적절한 모듈로 `함수 옮기기(Move Function)`와 `필드 옮기기(Move Field)`를 사용해서 결합도를 낮출 수 있다
- 여러 모듈이 자주 사용하는 공통적인 기능은 새로운 모듈을 만들어 잘 관리하거나, `위임 숨기기(Hide Delegate)`를 사용해 특정 모듈의 중재자처럼 사용할 수도 있다.
- 상속으로 인한 결합도를 줄일 때는 `슈퍼클래스 또는 서브클래스를 위임으로 교체하기`를 사용할 수 있다.

### 내부자 거래 변경 전 코드
```java
public class CheckIn {

    public boolean isFastPass(Ticket ticket) {
        LocalDate earlyBirdDate = LocalDate.of(2022, 1, 1);
        return ticket.isPrime() && ticket.getPurchasedDate().isBefore(earlyBirdDate);
    }
}

public class Ticket {

    private LocalDate purchasedDate;

    private boolean prime;

    public Ticket(LocalDate purchasedDate, boolean prime) {
        this.purchasedDate = purchasedDate;
        this.prime = prime;
    }

    public LocalDate getPurchasedDate() {
        return purchasedDate;
    }

    public boolean isPrime() {
        return prime;
    }
}
```

### 현재 체크인에서 티켓쪽으로 결합이 직접적으로 강했는데 티켓만 사용하도록 변경한다.
```java
public class Ticket {

    private LocalDate purchasedDate;

    private boolean prime;

    public Ticket(LocalDate purchasedDate, boolean prime) {
        this.purchasedDate = purchasedDate;
        this.prime = prime;
    }

    public LocalDate getPurchasedDate() {
        return purchasedDate;
    }

    public boolean isPrime() {
        return prime;
    }
}
```