package com.loan.service;

import com.loan.domain.Counsel;
import com.loan.dto.CounselDTO.Request;
import com.loan.dto.CounselDTO.Response;
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
}
