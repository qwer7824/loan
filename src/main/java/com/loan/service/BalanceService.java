package com.loan.service;

import com.loan.dto.BalanceDTO.*;

public interface BalanceService {

   Response create(Long applicationId,CreateRequest request);

   Response get(Long applicationId);

   Response update(Long applicationId, UpdateRequest request);

   Response repaymentUpdate(Long applicationId,RepaymentRequest request);

   void delete(Long applicationId);

}
