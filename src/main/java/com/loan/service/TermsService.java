package com.loan.service;

import com.loan.dto.TermsDTO.*;
import org.springframework.stereotype.Service;

import java.util.List;

public interface TermsService {

    Response create(Request request);

    List<Response> getAll();

}
