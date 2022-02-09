## 냄새 2. 중복 코드 (Duplicated Code)
- 중복 코드의 단점
  - 비슷한지, 완전히 동일한 코드인지 주의 깊게 봐야한다.
  - 코드를 변경할 때, 동일한 모든 곳의 코드를 변경해야한다
- 사용할 수 있는 리팩토링 기술
  - 동일한 코드를 여러 메소드에서 사용하는 경우, 함수 추출하기(Extract Function)
  - 코드가 비슷하게 생겼지만 완전히 같지는 않은 경우, 코드 분리하기(Slide Statements)
  - 여러 하위 클래스에 동일한 코드가 있다면, 메소드 올리기(Pull Up Method)

### [실습 코드](/src/main/java/me/saechimdaeki/refactoring/_02_duplicated_code/_01_before/StudyDashboard.java)

#### 리팩토링 전 코드
```java
public class StudyDashboard {

    private void printParticipants(int eventId) throws IOException {
        // Get github issue to check homework
        GitHub gitHub = GitHub.connect();
        GHRepository repository = gitHub.getRepository("whiteship/live-study");
        GHIssue issue = repository.getIssue(eventId);

        // Get participants
        Set<String> participants = new HashSet<>();
        issue.getComments().forEach(c -> participants.add(c.getUserName()));

        // Print participants
        participants.forEach(System.out::println);
    }

    private void printReviewers() throws IOException {
        // Get github issue to check homework
        GitHub gitHub = GitHub.connect();
        GHRepository repository = gitHub.getRepository("whiteship/live-study");
        GHIssue issue = repository.getIssue(30);

        // Get reviewers
        Set<String> reviewers = new HashSet<>();
        issue.getComments().forEach(c -> reviewers.add(c.getUserName()));

        // Print reviewers
        reviewers.forEach(System.out::println);
    }

    public static void main(String[] args) throws IOException {
        StudyDashboard studyDashboard = new StudyDashboard();
        studyDashboard.printReviewers();
        studyDashboard.printParticipants(15);

    }

}
```

### 리팩토링 4. 함수 추출하기 (Extract Function)
- "의도"와 "구현" 분리하기
- 무슨일을 하는 코드인지 알아내려고 노력해야 하는 코드라면 해당 코드를 함수로 분리하고 함수이름으로
- "무슨 일을 하는지" 표현할 수 있다.
- 한줄 짜리 메소드도 괜찮은가?
- 거대한 함수 안에 들어있는 주석은 추출한 함수를 찾는데 있어서 좋은 단서가 될 수 있다.

### 함수 추출하기 후 코드


```java
// 변수는 그를 사용하는 근처에 놓자.
public class StudyDashboard {

    private void printParticipants(int eventId) throws IOException {
        GHIssue issue = getGhIssue(eventId);
        Set<String> participants = getUsernames(issue);
        participants.forEach(System.out::println);
    }

    private Set<String> getUsernames(GHIssue issue) throws IOException {
        Set<String> usernames = new HashSet<>();
        issue.getComments().forEach(c -> usernames.add(c.getUserName()));
        return usernames;
    }

    private GHIssue getGhIssue(int eventId) throws IOException {
        GitHub gitHub = GitHub.connect();
        GHRepository repository = gitHub.getRepository("whiteship/live-study");
        return repository.getIssue(eventId);
    }

    private void printReviewers() throws IOException {
        GHIssue issue = getGhIssue(30);
        Set<String> reviewers = getUsernames(issue);
        reviewers.forEach(System.out::println);
    }

    public static void main(String[] args) throws IOException {
        StudyDashboard studyDashboard = new StudyDashboard();
        studyDashboard.printReviewers();
        studyDashboard.printParticipants(15);
    }
}
```

### 리팩토링 5. 코드 정리하기 (Slide Statements)
- 관련있는 코드끼리 묶여있어야 코드를 더 쉽게 이해할 수 있다
- 함수에서 사용할 변수를 상단에 미리 정의하기 보다는, 해당 변수를 사용하는 코드 바로위에 선언하자
- 관련있는 코드끼리 묶은다음 함수 추출하기(Extract Function)를 사용해서 더 깔끔하게 분리할 수도 있다.

### 코드 정리하기 후 코드
```java
public class StudyDashboard {

    private void printParticipants(int eventId) throws IOException {
        // Get github issue to check homework
        GitHub gitHub = GitHub.connect();
        GHRepository repository = gitHub.getRepository("whiteship/live-study");
        GHIssue issue = repository.getIssue(eventId);

        // Get participants
        Set<String> participants = new HashSet<>();
        issue.getComments().forEach(c -> participants.add(c.getUserName()));

        // Print participants
        participants.forEach(System.out::println);
    }

    private void printReviewers() throws IOException {
        // Get github issue to check homework
        GitHub gitHub = GitHub.connect();
        GHRepository repository = gitHub.getRepository("whiteship/live-study");
        GHIssue issue = repository.getIssue(30);

        // Get reviewers
        Set<String> reviewers = new HashSet<>();
        issue.getComments().forEach(c -> reviewers.add(c.getUserName()));

        // Print reviewers
        reviewers.forEach(System.out::println);
    }

    public static void main(String[] args) throws IOException {
        StudyDashboard studyDashboard = new StudyDashboard();
        studyDashboard.printReviewers();
        studyDashboard.printParticipants(15);
    }
}
```

### 리팩토링 6. 메소드 올리기 (Pull Up Method)
- 중복 코드는 당작은 잘 동작하더라도 미래에 버그를 만들어 낼 빌미를 제공한다.
  - 예) A에서 코드를 고치고, B에는 반영하지 않은 경우
- 여러 하위 클래스에 동일한 코드가 있다면, 손쉽게 이방법을 적용할 수 있다
- 비슷하지만 일부 값만 다른 경우라면, "함수 매개변수화하기" 리팩토링을 적용한 이후에, 이방법을 사용할 수 있다.
- 하위 클래스에 있는 코드가 상위 클래스가 아닌 하위클래스 기능에 의존하고 있다면 "필드 올리기"
- 를 적용한 이후에 이 방법을 적용할 수 있다.
- 두 메소드가 비슷한 절차를 따르고 있다면, "템플릿 메소드 패턴" 적용을 고려할 수 있다.
  
### 메소드 올리기 전 기존 코드  
```java
public class Dashboard {

    public static void main(String[] args) throws IOException {
        ReviewerDashboard reviewerDashboard = new ReviewerDashboard();
        reviewerDashboard.printReviewers();

        ParticipantDashboard participantDashboard = new ParticipantDashboard();
        participantDashboard.printParticipants(15);
    }
}

public class ParticipantDashboard extends Dashboard {

    public void printParticipants(int eventId) throws IOException {
        // Get github issue to check homework
        GitHub gitHub = GitHub.connect();
        GHRepository repository = gitHub.getRepository("whiteship/live-study");
        GHIssue issue = repository.getIssue(eventId);

        // Get participants
        Set<String> participants = new HashSet<>();
        issue.getComments().forEach(c -> participants.add(c.getUserName()));

        // Print participants
        participants.forEach(System.out::println);
    }
}

public class ReviewerDashboard extends Dashboard {

    public void printReviewers() throws IOException {
        // Get github issue to check homework
        Set<String> reviewers = new HashSet<>();
        GitHub gitHub = GitHub.connect();
        GHRepository repository = gitHub.getRepository("whiteship/live-study");
        GHIssue issue = repository.getIssue(30);

        // Get reviewers
        issue.getComments().forEach(c -> reviewers.add(c.getUserName()));

        // Print reviewers
        reviewers.forEach(System.out::println);
    }
}
```

### 메소드 올리기 후 코드
```java
public class Dashboard {

    public static void main(String[] args) throws IOException {
        ReviewerDashboard reviewerDashboard = new ReviewerDashboard();
        reviewerDashboard.printReviewers();

        ParticipantDashboard participantDashboard = new ParticipantDashboard();
        participantDashboard.printUsernames(15);
    }

    public void printUsernames(int eventId) throws IOException {
        // Get github issue to check homework
        GitHub gitHub = GitHub.connect();
        GHRepository repository = gitHub.getRepository("whiteship/live-study");
        GHIssue issue = repository.getIssue(eventId);

        // Get participants
        Set<String> participants = new HashSet<>();
        issue.getComments().forEach(c -> participants.add(c.getUserName()));

        // Print participants
        participants.forEach(System.out::println);
    }
}


public class ParticipantDashboard extends Dashboard {
}

public class ReviewerDashboard extends Dashboard {
    public void printReviewers() throws IOException {
        super.printUsernames(30);
    }
}
```