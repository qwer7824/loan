package com.loan.service;

import com.loan.dto.ApplicationDTO.*;

public interface ApplicationService{

    Response create (Request request); // 대출 신청 등록

    Response get (Long applicationId); // 대출 신청 조회

    Response update (Long applicationId , Request request); // 대출 신청 수정

    void delete (Long applicationId); // 대출 신청 삭제

    Boolean acceptTerms(Long applicationId, AcceptTerms request); // 약관 동의

    Response contract(Long applicationId); // 대출 계약

}
