package com.loan.service;

import com.loan.domain.Counsel;
import com.loan.dto.CounselDTO.Request;
import com.loan.dto.CounselDTO.Response;
import com.loan.exception.BaseException;
import com.loan.exception.ResultType;
import com.loan.repository.CounselRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CounselServiceImpl implements CounselService{

    private final CounselRepository counselRepository;

    private final ModelMapper modelMapper; // DTO -> Entity , Entity -> DTO

    @Override
    public Response create(Request request){ // 대출 상담 메소드
        Counsel counsel = modelMapper.map(request, Counsel.class); // request 가 Counsel Entity 로 매핑을 해줌
        counsel.setAppliedAt(LocalDateTime.now());

        Counsel created = counselRepository.save(counsel);

        return modelMapper.map(created, Response.class);
    }

    @Override
    public Response get(Long counselId) {
        Counsel counsel = counselRepository.findById(counselId).orElseThrow(()->{
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });
        return modelMapper.map(counsel,Response.class);
    }

    @Override
    public Response update(Long counselId, Request request) {
        Counsel counsel = counselRepository.findById(counselId).orElseThrow(()->{
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        counsel.setName(request.getName());
        counsel.setCellPhone(request.getCellPhone());
        counsel.setEmail(request.getEmail());
        counsel.setMemo(request.getMemo());
        counsel.setAddress(request.getAddress());
        counsel.setAddressDetail(request.getAddress());
        counsel.setZipCode(request.getZipCode());

        counselRepository.save(counsel);

        return modelMapper.map(counsel,Response.class);
    }

    @Override
    public void delete(Long counselId) {
        Counsel counsel = counselRepository.findById(counselId).orElseThrow(()->{
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        counsel.setIsDeleted(true);

        counselRepository.save(counsel);
    }
}
