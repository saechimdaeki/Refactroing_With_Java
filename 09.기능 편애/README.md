## 냄새 9. 기능 편애(Feature Envy)
- 어떤 모듈에 있는 함수가 다른 모듈에 있는 데이터나 함수를 더 많이 참조하는 경우에 발생한다.
  - 예) 다른 객체의 getter를 여러개 사용하는 메소드
- 관련 리팩토링 기술
  - `함수 옮기기(Move Function)`를 사용해서 함수를 적절한 위치로 옮긴다
  - 함수일부분만 다른 곳의 데이터와 함수를 많이 참조한다면 `함수 추출하기`로 함수를 나눈다음에 함수를 옮길 수 있다.
- 만약에 여러 모듈을 참조하고 있다면? 그중에서 가장 많은 데이터를 참조하는 곳으로 옮기거나
- 함수를 여러개로 쪼개서 각 모듈로 분산 시킬 수도 있다
- 데이터와 해당 데이터를 참조하는 행동을 같은 곳에 두도록 하자
- 예외적으로, 데이터와 행동을 분리한 디자인 패턴(전략패턴 또는 방문자 패턴)을 적용할 수도 있다.

### 기능 편애 예시 코드
```java
public class GasUsage {

    private double amount;

    private double pricePerUnit;

    public GasUsage(double amount, double pricePerUnit) {
        this.amount = amount;
        this.pricePerUnit = pricePerUnit;
    }

    public double getAmount() {
        return amount;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }
}

public class ElectricityUsage {

    private double amount;

    private double pricePerUnit;

    public ElectricityUsage(double amount, double pricePerUnit) {
        this.amount = amount;
        this.pricePerUnit = pricePerUnit;
    }

    public double getAmount() {
        return amount;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }
}


public class Bill {

    private ElectricityUsage electricityUsage;

    private GasUsage gasUsage;

    public double calculateBill() {
        var electicityBill = electricityUsage.getAmount() * electricityUsage.getPricePerUnit();
        var gasBill = gasUsage.getAmount() * gasUsage.getPricePerUnit();
        return electicityBill + gasBill;
    }

}
```

### 기능 편애 개선 후 코드
```java
public class Bill {

    private ElectricityUsage electricityUsage;

    private GasUsage gasUsage;

    public double calculateBill() {
        return electricityUsage.getElecticityBill() + gasUsage.getGasBill();
    }
}

public class ElectricityUsage {

    private double amount;

    private double pricePerUnit;

    public ElectricityUsage(double amount, double pricePerUnit) {
        this.amount = amount;
        this.pricePerUnit = pricePerUnit;
    }

    public double getAmount() {
        return amount;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public double getElecticityBill() {
        return this.getAmount() * this.getPricePerUnit();
    }

}

public class GasUsage {

    private double amount;

    private double pricePerUnit;

    public GasUsage(double amount, double pricePerUnit) {
        this.amount = amount;
        this.pricePerUnit = pricePerUnit;
    }

    public double getAmount() {
        return amount;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public double getGasBill() {
        return this.getAmount() * this.getPricePerUnit();
    }
}
```