# Trip Note

---

<div style="text-align:center"><img alt="image" width="329" src="https://s3.ap-northeast-2.amazonaws.com/tripnote.s3/logo-green.png" /></div>

## Trip Note Team Project

------------

> Elice 부트캠프 세 번째 팀 프로젝트
> 
> 개발기간: 2024. 5. 20 ~ 2024. 6. 14

## 배포 주소

------------


> 홈페이지: https://tripnote.site
> 
> 프론트엔드:
> 
> 백엔드:

## 개발자들 소개

------------


| 정유경  | 김수현 | 백지민 | 정주용 | 조부건 |  김도현  | 오성현|
|:----:|:---:|:------------:|:------------:|:------------:|:-----:|:------------:|
| 백엔드  | 백엔드 | 백엔드 | 백엔드 | 백엔드 | 프론트엔드 | 프론트엔드|
| 관리자/해시태그 API | 여행지 API  | 경로 API | 회원/메일 API Spring Security 설정 | 후기/댓글 API | 경로 생성 경로 추천 | 후기 마이페이지 |
| 서버 배포 관리자 페이지 구현 | 네이버 검색 API 활용 | 카카오 로그인/로그아웃 | 관리자 JWT access token 관리 | 관리자 페이지 구현 문서화 작업 | 경로 생성 경로 추천 로그인/로그아웃 | 메인페이지|
## 프로젝트 소개

------------

Tripnote는 여행을 가고 싶지만 동선을 짜기 어려워하거나 좋은 경로를 
추천받고 싶은 사람을 위해 만든 프로젝트입니다.
이 프로젝트의 두 가지 핵심 요소는 여행 경로 생성과 추천입니다. 

Tripnote 회원은 여행지들을 조합해서 여행 경로를 만들 수 있으며,
여행지 하나를 고르면 회원들이 그 다음으로 많이 찾은 여행지들을 한눈에 알 수 있습니다.
또한 기존에 존재하던 경로를 추천받으려고 할 때 사이트 이용자는 특정 여행지들을 포함하면서 인기 있는 여행 경로들을 추천받을 수 있습니다.

자신이 만든 여행 경로로 여행을 다녀온 회원은 후기를 작성해 자신의 경험을 다른 회원들과 공유할 수 있습니다.
다른 회원들은 후기글을 보고 좋아요 혹은 북마크를 남길 수 있으며, 댓글도 작성이 가능합니다. 

마지막으로 Tripnote 회원은 검색한 여행 경로 혹은 후기를 좋아요, 북마크할 수 있으며 해당 목록들은 마이페이지에서 확인할 수 있습니다. 

위와 같은 기능을 통해 Tripnote 이용자는 간편하게 여행 경로를 계획하고, 
다른 이용자들의 실제 여행 경험을 공유하며, 
자신만의 여행 정보를 효과적으로 관리할 수 있습니다.

## 시작 가이드

------------

### 요구사항

- 프론트엔드

  - [React 18.2.0](https://github.com/facebook/react/releases/tag/v18.2.0)

[//]: # (  TO DO: 추가할 것이 있으면 여기 넣어주세요 )

- 백엔드
  - [Java 17](https://openjdk.org/projects/jdk/17/)
  - [Spring Boot 3.2.5](https://github.com/spring-projects/spring-boot/releases/tag/v3.2.5)
  
[//]: # (  TO DO: 추가할 것이 있으면 여기 넣어주세요 )

### Installation - Backend
``` bash
$ git clone https://kdt-gitlab.elice.io/cloud_track/class_02/web_project3/team01/tripnote.git
$ ./gradlew build
$ cd build/libs
$ java -jar tripnote-0.0.1-SNAPSHOT.jar 
```

### Installation - FrontEnd
``` bash
$ git clone https://kdt-gitlab.elice.io/cloud_track/class_02/web_project3/team01/trip-note.git
$ npm install
$ npm run dev
```

## 기술 스택

- 백엔드

  <img src="https://img.shields.io/badge/openjdk-000000?style=for-the-badge&logo=openjdk&logoColor=white">
  <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
  <img src="https://img.shields.io/badge/springsecurity-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white">
  <img src="https://img.shields.io/badge/jpa-6DB33F?style=for-the-badge&logo=jpa&logoColor=white">
  <img src="https://img.shields.io/badge/queryDSL-6DB33F?style=for-the-badge&logo=queryDSL&logoColor=white">
  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
  <img src="https://img.shields.io/badge/amazons3-569A31?style=for-the-badge&logo=amazons3&logoColor=white">
  <img src="https://img.shields.io/badge/amazonec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white">
  <img src="https://img.shields.io/badge/amazonrds-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white"> 
  <img src="https://img.shields.io/badge/swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=white"> 
  <img src="https://img.shields.io/badge/oauth-85EA2D?style=for-the-badge&logo=oauth&logoColor=white"> 
  <img src="https://img.shields.io/badge/redis-FF4438?style=for-the-badge&logo=redis&logoColor=white"> 


- 프론트엔드

  <img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=white">
  <img src="https://img.shields.io/badge/react-61DAFB?style=for-the-badge&logo=react&logoColor=white">
  <img src="https://img.shields.io/badge/tailwindcss-06B6D4?style=for-the-badge&logo=tailwindcss&logoColor=white">
  <img src="https://img.shields.io/badge/vite-646CFF?style=for-the-badge&logo=vite&logoColor=white">
  <img src="https://img.shields.io/badge/zustand-646CFF?style=for-the-badge&logo=zustand&logoColor=white">

-------

## 화면 구성 

| 메인 페이지  |  후기 페이지  |  
| :-------------------------------------------: | :------------: |
|  <img width="329" src="https://s3.ap-northeast-2.amazonaws.com/tripnote.s3/%EB%A9%94%EC%9D%B8+%ED%8E%98%EC%9D%B4%EC%A7%80.jpg"/> |  <img width="329" src="https://s3.ap-northeast-2.amazonaws.com/tripnote.s3/%ED%9B%84%EA%B8%B0.png"/>|  
| 경로 생성 페이지   |  경로 추천 페이지   |  
| <img width="400" src="https://s3.ap-northeast-2.amazonaws.com/tripnote.s3/%EC%83%9D%EC%84%B1.jpg"/>   |  <img width="400" src="https://s3.ap-northeast-2.amazonaws.com/tripnote.s3/%EC%B6%94%EC%B2%9C.jpg"/>     |




---

## 서비스 차트
  <img alt ="FlowChart" src="https://s3.ap-northeast-2.amazonaws.com/tripnote.s3/flowchart.jpg">
  [Figma](https://www.figma.com/file/cDikAF7eAjtXnYTEZFS1yE?embed_host=notion&kind=file&node-id=0-1&t=4Hoi9O6XgTOaD8vY-0&viewer=1)


---

## ERD
  <img alt ="DB ERD" src="https://s3.ap-northeast-2.amazonaws.com/tripnote.s3/trip+note+erd.png">

---


## 와이어프레임
  [Figma](https://www.figma.com/board/sGnAjT5v20aHp9qURFdKcf/Untitled?node-id=0-1&t=AzCSYLUmwCI8eo9V-0)

---

## API 명세서
  [swaggerDoc](https://s3.ap-northeast-2.amazonaws.com/tripnote.s3/swaggerDoc.html)

---

## 아키텍쳐
  <img alt ="Packege Architecture" src="https://s3.ap-northeast-2.amazonaws.com/tripnote.s3/arch.jpg">

---
