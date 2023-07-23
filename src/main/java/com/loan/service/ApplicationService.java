package com.loan.service;

import com.loan.dto.ApplicationDTO.*;

public interface ApplicationService{

    Response create (Request request);

    Response get (Long applicationId);

}
