package com.loan.service;


import com.loan.domain.Application;
import com.loan.domain.Judgment;
import com.loan.domain.Terms;
import com.loan.dto.ApplicationDTO.*;
import com.loan.exception.BaseException;
import com.loan.exception.ResultType;
import com.loan.repository.AcceptTermsRepository;
import com.loan.repository.ApplicationRepository;
import com.loan.repository.JudgmentRepository;
import com.loan.repository.TermsRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final TermsRepository termsRepository;
    private final AcceptTermsRepository acceptTermsRepository;
    private final JudgmentRepository judgmentRepository;

    private final ModelMapper modelMapper;

    @Override
    public Response create(Request request) {
        Application application = modelMapper.map(request, Application.class);
        application.setAppliedAt(LocalDateTime.now());

        Application applied = applicationRepository.save(application);

        return modelMapper.map(applied, Response.class);
    }

    @Override
    public Response get(Long applicationId) {
        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });
        return modelMapper.map(application, Response.class);
    }

    @Override
    public Response update(Long applicationId, Request request) {
        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        application.setName(request.getName());
        application.setCellPhone(request.getCellPhone());
        application.setEmail(request.getEmail());
        application.setHopeAmount(request.getHopeAmount());

        applicationRepository.save(application);

        return modelMapper.map(application, Response.class);
    }

    @Override
    public void delete(Long applicationId) {
        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        application.setIsDeleted(true);

        applicationRepository.save(application);

    }

    @Override
    public Boolean acceptTerms(Long applicationId, AcceptTerms request) {
        applicationRepository.findById(applicationId).orElseThrow(()->{ // 대출 신청이 조회가 되지않으면 에러발생
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        List<Terms> termsList = termsRepository.findAll(Sort.by(Sort.Direction.ASC, "termsId"));
        if (termsList.isEmpty()){ // 약관이 없으면 에러 발생
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }
        List<Long> acceptTermsIds = request.getAcceptTermsIds(); // 불러온 약관의 수와 동의한 약관 수가 맞지않으면 에러
        if (termsList.size() != acceptTermsIds.size()){
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        List<Long> termsIds = termsList.stream().map(Terms::getTermsId).collect(Collectors.toList());
        Collections.sort(acceptTermsIds); // 약관 정렬

        if(!termsIds.containsAll(acceptTermsIds)){ // 가지고 있는 termsId 와 DTO로 받은 값이 포함되지 않으면 에러 발생
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        for (Long termsId : acceptTermsIds){
            com.loan.domain.AcceptTerms accepted = com.loan.domain.AcceptTerms.builder()
                    .termsId(termsId)
                    .applicationId(applicationId)
                    .build();

             acceptTermsRepository.save(accepted);

        }

        return true;
    }

    @Override
    public Response contract(Long applicationId) {
        // 신청 정보가 있는지
        Application application = applicationRepository.findById(applicationId).orElseThrow(()->{
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });
        // 심사 정보가 있는지
        Judgment judgment = judgmentRepository.findByApplicationId(applicationId).orElseThrow(()->{
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });
        // 승인 금액 > 0
        if (application.getApprovalAmount() == null
        || application.getApprovalAmount().compareTo(BigDecimal.ZERO) == 0){
        throw new BaseException(ResultType.SYSTEM_ERROR);


        }
        // 계약 체결
        application.setContractedAt(LocalDateTime.now());
        applicationRepository.save(application);

        return null;
    }

}
