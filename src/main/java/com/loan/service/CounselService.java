package com.loan.service;

import com.loan.dto.CounselDTO.Response;
import com.loan.dto.CounselDTO.Request;

public interface CounselService {
    Response create(Request request); // 대출 상담 신청

    Response get(Long counselId); // 대출 상담 정보 조회
}
