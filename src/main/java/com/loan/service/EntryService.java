package com.loan.service;

import com.loan.dto.EntryDTO.*;

public interface EntryService {

    Response create(Long applicationId , Request request);

    Response get(Long applicationId);
    UpdateResponse update(Long entryId , Request request);
}
