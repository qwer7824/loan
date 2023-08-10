package com.loan.service;

import com.loan.domain.Application;
import com.loan.domain.Entry;
import com.loan.domain.Repayment;
import com.loan.dto.BalanceDTO;
import com.loan.dto.RepaymentDTO;
import com.loan.exception.BaseException;
import com.loan.exception.ResultType;
import com.loan.repository.ApplicationRepository;
import com.loan.repository.EntryRepository;
import com.loan.repository.RepaymentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RepaymentServiceImpl implements RepaymentService {

    private final RepaymentRepository repaymentRepository;

    private final ApplicationRepository applicationRepository;

    private final EntryRepository entryRepository;

    private final BalanceService balanceService;

    private final ModelMapper modelMapper;

    @Override
    public RepaymentDTO.Response create(Long applicationId, RepaymentDTO.Request request) {
        // 검증 1. 계약을 완료한 상태 2.집행이 되어 있어야함
        if (!isRepayableApplication(applicationId)) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        Repayment repayment = modelMapper.map(request, Repayment.class);
        repayment.setApplicationId(applicationId);

        repaymentRepository.save(repayment);

        //잔고 balance : 500 -> 100 = 400
        BalanceDTO.Response updatedBalance = balanceService.repaymentUpdate(applicationId,
                BalanceDTO.RepaymentRequest.builder()
                        .repaymentAmount(request.getRepaymentAmount())
                        .type(BalanceDTO.RepaymentRequest.RepaymentType.REMOVE)
                        .build());

        RepaymentDTO.Response response = modelMapper.map(repayment, RepaymentDTO.Response.class);
        response.setBalance(updatedBalance.getBalance());

        return response;
    }

    @Override
    public List<RepaymentDTO.ListResponse> get(Long applicationId) {
        List<Repayment> repayments = repaymentRepository.findAllByApplicationId(applicationId);
        return repayments.stream().map(r -> modelMapper.map(r, RepaymentDTO.ListResponse.class)).collect(Collectors.toList());
    }

    @Override
    public RepaymentDTO.UpdateResponse update(Long repaymentId, RepaymentDTO.Request request) {
        Repayment repayment = repaymentRepository.findById(repaymentId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        Long applicationId = repayment.getApplicationId();
        BigDecimal beforeRepaymentAmount = repayment.getRepaymentAmount();

        // 500 - 100 = 400
        // 400 + 100 = 500

        balanceService.repaymentUpdate(applicationId, BalanceDTO.RepaymentRequest.builder()
                .repaymentAmount(beforeRepaymentAmount)
                .type(BalanceDTO.RepaymentRequest.RepaymentType.ADD)
                .build());

        repayment.setRepaymentAmount(request.getRepaymentAmount());
        repaymentRepository.save(repayment);

        BalanceDTO.Response updatedBalance = balanceService.repaymentUpdate(applicationId, BalanceDTO.RepaymentRequest.builder()
                .repaymentAmount(repayment.getRepaymentAmount())
                .type(BalanceDTO.RepaymentRequest.RepaymentType.REMOVE)
                .build());

        return RepaymentDTO.UpdateResponse.builder()
                .applicationId(applicationId)
                .beforeRepaymentAmount(beforeRepaymentAmount)
                .afterRepaymentAmount(repayment.getRepaymentAmount())
                .balance(updatedBalance.getBalance())
                .createdAt(repayment.getCreatedAt())
                .updatedAt(repayment.getUpdatedAt())
                .build();
    }

    @Override
    public void delete(Long repaymentId) {
        Repayment repayment = repaymentRepository.findById(repaymentId).orElseThrow(()->{
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        Long applicationId = repayment.getApplicationId();
        BigDecimal removeRepaymentAmount = repayment.getRepaymentAmount();

        balanceService.repaymentUpdate(applicationId,BalanceDTO.RepaymentRequest.builder()
                        .repaymentAmount(removeRepaymentAmount)
                        .type(BalanceDTO.RepaymentRequest.RepaymentType.ADD)
                .build());

        repayment.setIsDeleted(true);
        repaymentRepository.save(repayment);
    }

    private boolean isRepayableApplication (Long applicationId){
            Optional<Application> existedApplication = applicationRepository.findById(applicationId);
            if (existedApplication.isEmpty()) {
                return false;
            }
            if (existedApplication.get().getContractedAt() == null) {
                return false;
            }

            Optional<Entry> existedEntry = entryRepository.findByApplicationId(applicationId);
            return existedEntry.isPresent();
        }
    }
