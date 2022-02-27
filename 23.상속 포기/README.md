## 냄새 23. 상속 포기(Refused Bequest)
- 서브클래스가 슈퍼클래스에서 제공하는 메소드나 데이터를 잘 활용하지 않는다는 것은 해당 상속구조에 문제가 있다는 뜻
  - 기존의 서브클래스 또는 새로운 서브클래스를 만들고 슈퍼클래스에서 `메소드와 필드를 내려주면(Push Down Method/ Field)` 
  - 슈퍼클래스에 공동으로 사용하는 기능만 남길 수 있다.
- 서브클래스가 슈퍼클래스의 기능을 재사용하고 싶지만 인터페이스를 따르고 싶지않은 경우에는 
- `슈퍼클래스 또는 서브클래스를 위임으로 교체하기` 리팩토링을 적용할 수 있다.

### 상속 포기 변경 전 코드
```java
public class Quota {
}

public class Employee {

    protected Quota quota;

    protected Quota getQuota() {
        return new Quota();
    }

}

public class Engineer extends Employee {

}

public class Salesman extends Employee {

}
```

### 상속 포기 변경 후 코드
```java
public class Quota {
}

public class Employee {

}

public class Engineer extends Employee {

}

public class Salesman extends Employee {

    protected Quota quota;

    protected Quota getQuota() {
        return new Quota();
    }
}
```