## 5주차 리뷰
### 권다운
- conroller에서 service 2개의 메서드를 실행시키고 싶을 때 그 2개의 메서드가 원자성을 보장하게 하기 위해서는 어떻게 짜야할지 고민입니다.
  원자성을 포기하고 contorller에서 2개의 메서드를 실행하게 짜야할지, 아니면 다른 서비스에서 해야할 역할을 직접 만들어서 구현할지 고민입니다! 예전에는 파사드패턴을 사용하여 해결했는데 구조가 너무 복잡해 질것 같아 보류했습니다. 
### 김건

### 신형진
* 비동기로 수행되는 메서드들의 성공을 보장하기 위해서 비동기 로그 테이블을 생성하려고 합니다. 그러면 비동기 리스너 1개당 하나의 로그 테이블이 생성되어야 하는건가요? 그렇지 않다면 일반적으로 비동기로 처리되는 로직의 성공을 보장하기 위해서 어떤 방법을 사용하나요?

### 유승욱
