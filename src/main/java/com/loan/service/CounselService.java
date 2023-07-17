package com.loan.service;

import com.loan.dto.CounselDTO.Response;
import com.loan.dto.CounselDTO.Request;

public interface CounselService {
    Response create(Request request);
}
