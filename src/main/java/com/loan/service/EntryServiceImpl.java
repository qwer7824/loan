package com.loan.service;

import com.loan.domain.Application;
import com.loan.domain.Entry;
import com.loan.dto.BalanceDTO;
import com.loan.dto.EntryDTO.*;
import com.loan.exception.BaseException;
import com.loan.exception.ResultType;
import com.loan.repository.ApplicationRepository;
import com.loan.repository.EntryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntryServiceImpl implements EntryService{

    private final EntryRepository entryRepository;
    private final ApplicationRepository applicationRepository;
    private final ModelMapper modelMapper;
    private final BalanceService balanceService;

    @Override
    public Response create(Long applicationId, Request request) {
        // 계약 체결 검증
    if(!isContractedApplication(applicationId)){
        throw new BaseException(ResultType.SYSTEM_ERROR);
    }
    Entry entry = modelMapper.map(request,Entry.class);
    entry.setApplicationId(applicationId);

    entryRepository.save(entry);

    // 대출 잔고 관리
    balanceService.create(applicationId,
            BalanceDTO.CreateRequest.builder()
                    .entryAmount(request.getEntryAmount())
                    .build());


        return modelMapper.map(entry, Response.class);
    }

    @Override
    public Response get(Long applicationId) {
        Optional<Entry> entry = entryRepository.findByApplicationId(applicationId);

        if(entry.isPresent()){
            return modelMapper.map(entry, Response.class);
        }else{
            return null;
        }
    }

    private boolean isContractedApplication(Long applicationId){
       Optional<Application> existed = applicationRepository.findById(applicationId);
       if(existed.isEmpty()){
           return false;
       }
       return existed.get().getContractedAt() != null;
    }
}
