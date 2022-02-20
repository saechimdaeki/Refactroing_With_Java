## 냄새 13. 반복문 (Loops)
- 프로그래밍 언어 초기부터 있었던 반복문은 처음엔 별다른 대안이 없어서 간과했지만 최근 Java와 같은 언어에서 
- 함수형 프로그래밍을 지원하면서 반복문에 비해 더 나은 대안책이 생겼다.
- `반복문을 파이프라인으로 바꾸는 (Replace Loop with PipeLine)` 리팩토링을 적용하면 필터나 맵핑과 같은
- 파이프라인 기능을 사용해 보다 빠르게 어떤 작업을 하는지 파악할 수 있다.


### 리팩토링 33. 반복문을 파이프라인으로 바꾸기(Replace Loop with PipeLine)
- 콜렉션 파이프라인 (자바의 Stream, C#의 LINQ - Laungage Integrated Query)
- 고전적인 반복문을 파이프라인 오퍼레이션을 사용해 표현하면 코드를 더 명확하게 만들 수 있다,
  - 필터(filter): 전달받은 조건의 true에 해당하는 데이터만 다음 오퍼레이션으로 전달.
  - 맵(map): 전달받은 함수를 사용해 입력값을 원하는 출력값으로 변환하여 다음 오퍼레이션으로 전달
- https://martinfowler.com/articles/refactoring-pipelines.html

### 반복문을 파이프라인으로 변경 전 코드
```java
public class Author {

    private String company;

    private String twitterHandle;

    public Author(String company, String twitterHandle) {
        this.company = company;
        this.twitterHandle = twitterHandle;
    }

    static public List<String> TwitterHandles(List<Author> authors, String company) {
        var result = new ArrayList<String>();
        for (Author a : authors) {
            if (a.company.equals(company)) {
                var handle = a.twitterHandle;
                if (handle != null)
                    result.add(handle);
            }
        }
        return result;
    }
}
```

### 반복문을 파이프라인으로 변경 후 코드
```java
public class Author {

    private String company;

    private String twitterHandle;

    public Author(String company, String twitterHandle) {
        this.company = company;
        this.twitterHandle = twitterHandle;
    }

    static public List<String> TwitterHandles(List<Author> authors, String company) {
        return authors.stream()
                .filter(author -> author.company.equals(company))
                .map(author -> author.twitterHandle)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}
```