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

@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService{

    private final BalanceRepository balanceRepository;

    private final ModelMapper modelMapper;

    @Override
    public Response create(Long applicationId, CreateRequest request) {
        if (balanceRepository.findAllByApplicationId(applicationId).isPresent()){
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        Balance balance = modelMapper.map(request , Balance.class);

        BigDecimal entryAmount = request.getEntryAmount();
        balance.setApplicationId(applicationId);
        balance.setBalance(entryAmount);

        Balance saved = balanceRepository.save(balance);

        return modelMapper.map(saved , Response.class);
    }
    @Override
    public Response update(Long applicationId, UpdateRequest request) {

        Balance balance = balanceRepository.findAllByApplicationId(applicationId).orElseThrow(()->{
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
}
