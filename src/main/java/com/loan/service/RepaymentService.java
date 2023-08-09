package com.loan.service;

import com.loan.dto.RepaymentDTO.*;

import java.util.List;

public interface RepaymentService {

    Response create(Long applicationId, Request request);

    List<ListResponse> get(Long applicationId);

    UpdateResponse update(Long repaymentId,Request request);
}
