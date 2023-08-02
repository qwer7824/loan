package com.loan.service;

import com.loan.dto.BalanceDTO.*;

public interface BalanceService {

   Response create(Long applicationId,CreateRequest request);

   Response update(Long applicationId, UpdateRequest request);
}
