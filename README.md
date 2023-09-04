# loan domain server

## 요구사항 분석 

- 대출에 필요한 상담 , 신청 , 심사 , 집행 , 상환 기능들을 간단하게 API로 구현하였다.
- 모든 도메인의 삭제 기능은 '@Where(clause = "is_deleted=false")' 설정으로 소프트 삭제(soft delete) 처리를 하였다.
- modelmapper 를 사용하여 매핑하였다.
- 상담 신청 > 대출 신청 > 대출 심사 등록 > 한도 부여 > 심사 체결 > 대출 집행 > 대출 상환 순으로 처리 됨.


## API 정보
<img width="1299" alt="스크린샷 2023-09-04 오후 3 56 05" src="https://github.com/qwer7824/loan/assets/8233989/7478bd25-8f8b-418e-9721-97a7c4c5475c">

<img width="1299" alt="스크린샷 2023-09-04 오후 3 57 28" src="https://github.com/qwer7824/loan/assets/8233989/9ae305eb-a8bd-4cf5-aea1-b3a3a3f5b3e4">

<img width="1299" alt="스크린샷 2023-09-04 오후 3 58 00" src="https://github.com/qwer7824/loan/assets/8233989/4030f458-b5b3-43bd-9772-b499d0cb13db">

<img width="1299" alt="스크린샷 2023-09-04 오후 3 58 45" src="https://github.com/qwer7824/loan/assets/8233989/79f23285-d649-4495-b007-dbd4962a22af">

<img width="1299" alt="스크린샷 2023-09-04 오후 3 58 55" src="https://github.com/qwer7824/loan/assets/8233989/73ef3274-1a99-4be6-826a-b7ff377b8738">

<img width="1299" alt="스크린샷 2023-09-04 오후 3 59 04" src="https://github.com/qwer7824/loan/assets/8233989/33d6ed65-9131-4982-ac14-fbd80bcd13ae">


## Tech Stack   

- JDK 11
- Spring Boot 2.7.1
- Spring Data JPA
- Gradle
- Lombok
- modelmapper
- h2
- mysql
