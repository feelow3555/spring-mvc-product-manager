# Spring MVC 상품 관리 시스템

> ⚠️ 소스코드는 `master` 브랜치에 있습니다.
> 👉 https://github.com/feelow3555/spring-mvc-product-manager/tree/master

## 과제 정보
- 과목: 웹 프레임워크 프로그래밍
- 학번: 2271483
- 성명: 황필호
- 제출일: 2026년 4월 19일

## 기술 스택
Java 21 / Spring MVC 7.0.0 / Thymeleaf 3.1.3 / Hibernate ORM 7.0.0 / MySQL 9.1.0 / Tomcat 11 / Maven

## 실행 방법
1. `mvn clean package -DskipTests`
2. `docker compose up -d`
3. http://localhost:8080/products 접속

## 구현 기능
- Category CRUD (목록 / 등록 / 삭제, 중복 방지 / 연결 상품 예외 처리)
- 상품 키워드 검색 (JPQL LIKE)
- 카테고리 드롭다운 필터
