# Dialog

## 개발 환경 및 버전
- IDE: IntelliJ
- Spring Boot: 3.4.5
- Java: 21

## 협업 규칙
### 회의 및 코어타임 일정
- 일정
	- 월: 09:00-12:00
	- 화-금: 09:00-10:00
	- 토: 12:30-17:30
- 회의 내용은 노션을 통해 공유

### 개발 규칙 및 컨벤션

#### 코드 스타일

기본적으로 [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)를 따릅니다. 별도로 정의된 규칙은 다음과 같습니다:

- **파라미터 3개 이상이면 멀티라인으로 작성**
```java
public exampleClass(String param1,
                    String param2,
                    String param3) {
    // 메서드 내용
}
```

#### Git 컨벤션

##### 브랜치 관리

- **Git Flow 방식 활용**
    - `main`: 배포용 브랜치
    - `develop`: 개발 메인 브랜치
    - `feature/기능명`: 기능 개발 브랜치
    - `hotfix/버그명`: 긴급 버그 수정 브랜치
- **❗❗ main branch에 직접 PR 요청을 날리지 않도록 주의**

##### Commit 메시지

[Udacity Nanodegree Commit Message Style Guide](https://udacity.github.io/git-styleguide/)를 기본으로 따르되, 다음 규칙을 적용합니다
- commit 메시지는 **한글로 작성**
- type, Subject 이외 내용은 필요할 때만 작성

##### PR(Pull Request) 규칙

- PR 생성 시 **merge 후 branch 자동 삭제** 설정
- PR 제목 형식: `#이슈번호 작업내용 간략설명`
    - 예시: `#2 공통 응답 예시 작성`
- PR 본문에 작업 내용을 상세히 기술
- PR 본문 마지막에 `this closes #이슈번호` 작성
- Squash merge 방식 사용

## 팀 구성
<table>
	  <tr>
	     <td align="center"><a href="https://github.com/kwonkeonhyeong"><img src="https://avatars.githubusercontent.com/u/138849238?v=4?v=4?s=100" width="100px;" alt=""/><br /><sub><b>히포</b></sub></a><br /></td>
	    <td align="center"><a href="https://github.com/abc5259"><img src="https://avatars.githubusercontent.com/u/62169861?v=4?s=100" width="100px;" alt=""/><br /><sub><b>서프</b></sub></a><br /></td>
		<td align="center"><a href="https://github.com/DongchannN"><img src="https://avatars.githubusercontent.com/u/96824025?v=4?s=100" width="100px;" alt=""/><br /><sub><b>차니</b></sub></a><br /></td>
	</tr>
</table>

