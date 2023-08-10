package com.loan.service;

import com.loan.domain.Balance;
import com.loan.dto.BalanceDTO.*;
import com.loan.exception.BaseException;
import com.loan.exception.ResultType;
import com.loan.repository.BalanceRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService{

    private final BalanceRepository balanceRepository;

    private final ModelMapper modelMapper;

    @Override
    public Response create(Long applicationId, CreateRequest request) {
        Balance balance = modelMapper.map(request , Balance.class);

        BigDecimal entryAmount = request.getEntryAmount();
        balance.setApplicationId(applicationId);
        balance.setBalance(entryAmount);

        balanceRepository.findByApplicationId(applicationId).ifPresent(b -> {
            balance.setBalanceId(b.getBalanceId());
            balance.setIsDeleted(b.getIsDeleted());
            balance.setCreatedAt(b.getCreatedAt());
            balance.setUpdatedAt(LocalDateTime.now());
        });

        Balance saved = balanceRepository.save(balance);

        return modelMapper.map(saved , Response.class);
    }
    @Override
    public Response get(Long applicationId) {
        Balance balance = balanceRepository.findById(applicationId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        return modelMapper.map(balance, Response.class);
    }

    @Override
    public Response update(Long applicationId, UpdateRequest request) {

        Balance balance = balanceRepository.findByApplicationId(applicationId).orElseThrow(()->{
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        BigDecimal beforeEntryAmount = request.getBeforeEntryAmount();
        BigDecimal afterEntryAmount = request.getAfterEntryAmount();
        BigDecimal updatedBalance = balance.getBalance();

        updatedBalance = updatedBalance.subtract(beforeEntryAmount).add(afterEntryAmount);
        balance.setBalance(updatedBalance);

        Balance updated = balanceRepository.save(balance);
        return modelMapper.map(updated,Response.class);
    }

    @Override
    public Response repaymentUpdate(Long applicationId, RepaymentRequest request) {
        Balance balance = balanceRepository.findByApplicationId(applicationId).orElseThrow(()->{
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        BigDecimal updatedBalance = balance.getBalance();
        BigDecimal repaymentAmount = request.getRepaymentAmount();

        // 상환 정산 : balance - repaymentAmount
        // 상환금 롤백 : balance + repaymentAmount
        if (request.getType().equals(RepaymentRequest.RepaymentType.ADD)){
        updatedBalance = updatedBalance.add(repaymentAmount);
        }else {
        updatedBalance = updatedBalance.subtract(repaymentAmount);
        }

        balance.setBalance(updatedBalance);

        Balance updated = balanceRepository.save(balance);

        return modelMapper.map(updated,Response.class);
    }

    @Override
    public void delete(Long applicationId) {
        Balance balance = balanceRepository.findByApplicationId(applicationId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        balance.setIsDeleted(true);

        balanceRepository.save(balance);
    }
}
