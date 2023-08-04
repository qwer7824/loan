package com.loan.service;

import com.loan.dto.RepaymentDTO.*;

public interface RepaymentService {

    Response create(Long applicationId, Request request);
}
