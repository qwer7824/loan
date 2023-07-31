package com.loan.service;

import com.loan.dto.ApplicationDTO;
import com.loan.dto.JudgmentDTO.*;

public interface JudgmentService {

    Response create(Request request);

    Response get(Long judgmentId);

    Response getJudgmentOfApplication(Long applicationId); // 신청 정보 아이디를 통해서 심사정보를 가져옴

    Response update(Long judgmentId,Request request);

    void delete(Long judgmentId);
    ApplicationDTO.GrantAmount grant(Long judgmentId);
}
