package com.loan.service;

import com.loan.dto.TermsDTO.*;
import org.springframework.stereotype.Service;

public interface TermsService {

    Response create(Request request);

}
