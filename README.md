## 블로그 API
### 다운로드 링크: https://github.com/juhyun-heyd/blog-api/tree/main/jar
<u>jar 파일 실행 시 local redis 설정이 필요합니다.</u>
```shell
# Docker Redis 설정
docker run --name "local-redis" -d -p 6379:6379 redis
```

### 1. API 명세서
#### 블로그 검색 API
GET `/v1/search/blog/{keyword}?sort=accurancy&page=1&size=10`  
**Response Body**
```
{
  "success": true,
  "response": {
    "meta": {
      "total_count": 117080,
      "pageable_count": 800,
      "is_end": false
    },
    "documents": [
      {
        "title": "Novel AI] 캐릭터 표정 명령어<b>keyword</b> 추천",
        "contents": "&lt;시선에 대한 명령어&gt; &#34;그는 ~을 쳐다봤다.&#34; - The character looked at ~ with their eyes. &#34;그는 ~을 바라볼 때 입을 찡그렸다.&#34; ...",
        "url": "http://mooto.tistory.com/49",
        "blogName": null,
        "thumbnail": "https://search4.kakaocdn.net/argon/130x130_85_c/B2uIuduD7vt",
        "datetime": "2023-03-17T16:17:53Z"
      },
      ...
    ]
  },
  "errors": null
}
```

#### 인기 검색어 목록 API
GET `/v1/search/popular-keyword`  
**Response Body**
```
{
  "success": true,
  "response": [
    {
      "keyword": "keyword",
      "count": 20
    },
    ...
  ],
  "errors": null
}
```

<br>

### 2. 외부 라이브러리 및 오픈소스
- Redis
  - 대용량 데이터를 고려하여 In-Memory DB에서 정렬된 순서로 데이터를 관리하는 Redis의 Sorted Set 사용
- Redisson
  - 블로그 검색 시 여러 유저의 분산 환경에서의 동시성 이슈를 고려하여 Redisson 분산 락 적용

<br>

### 3. Build & Run & Test

#### Build
`$ ./gradlew clean build -x test`

#### Run
`$ java -jar ./build/libs/blog-api-0.0.1-SNAPSHOT.jar` 

#### Test
`$ ./gradlew test`

### To-Be
- Naver API 적용 및 
- DB I/O 부하 분리
  - 검색어 횟수 증가 로직은 주요 로직은 아니기에 중간에 Queue를 두어 Retry 등의 로직 추가
- 블로그 검색 리스트 캐싱 추가
  - 현재 카카오 API 검색 결과를 동기 방식으로 반환하고 있으나, 검색 리스트 비동기 처리 등 캐싱 처리를 고려할 필요성이 있음
- Logback 적용 및 로깅 처리
- 각 Config 설정값 최적화

### Architecture
![image](https://user-images.githubusercontent.com/50076031/226584465-e304e488-da1d-478b-99ab-d3b21aeb337d.png)

