## 냄새 15. 추측성 일반화 (Speculative Generality)
- 나중에 이런 저러한 기능이 생길 것으로 예상하여, 여러 경우에 필요로 할만한 기능을 만들어 놨지만 
- "그런일은 없었고.." 결국에 쓰이지 않는 코드가 발생한 경우
- XP의 YAGNI(You aren't gonna need it) 원칙을 따르자
- 관련 리팩토링
  - 추상 클래스를 만들었지만 크게 유요하지 않다면 `계층 합치기(Collapse Hierarchy)`
  - 불필요한 위임은 `함수 인라인(Inline Function)` 또는 `클래스 인라인(Inline Class)`
  - 사용하지 않는 매개변수를 가진 함수는 `함수 선언 변경하기(Change Function Declaration)`
  - 오로지 테스트 코드에서만 사용하고 있는 코드는 `죽은 코드 제거하기(Reomve Dead code)`


### 리팩토링 35. 죽은 코드 제거하기 (Remove Dead Code)
- 사용하지 않는 코드가 애플리케이션 성능이나 기능에 영향을 끼치지는 않는다.
- 하지만, 해당 소프트웨어가 어떻게 동작하는지 이해하려는 사람들에게는 꽤 고통을 줄 수 있다.
- 실제로 나중에 필요해질 코드라 하더라도 지금 쓰이지 않는 코드라면(주석으로 감싸는게 아니라) 삭제해야 한다.
  - 나중에 정말로 다시 필요해진다면 git과 같은 버전 관리 시스템을 사용해 복원할 수 있다.

### 죽은 코드 제거하기 변경 전 코드
```java
public class Reservation {

    private String title;

    private LocalDateTime from;

    private LocalDateTime to;

    private LocalDateTime alarm;

    public Reservation(String title, LocalDateTime from, LocalDateTime to) {
        this.title = title;
        this.from = from;
        this.to = to;
    }

    public void setAlarmBefore(int minutes) {
        this.alarm = this.from.minusMinutes(minutes);
    }

    public LocalDateTime getAlarm() {
        return alarm;
    }
}
```

#### 이는 인텔리제이의 usage 를보고 죽은 코드를 확인한 후 지울 수 있다.