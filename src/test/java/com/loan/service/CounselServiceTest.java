package com.loan.service;

import com.loan.domain.Counsel;
import com.loan.repository.CounselRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import com.loan.dto.CounselDTO.Request;
import com.loan.dto.CounselDTO.Response;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CounselServiceTest { // 유닛 테스트

    @InjectMocks
    CounselServiceImpl counselService;

    @Mock
    private CounselRepository counselRepository;

    @Spy
    private ModelMapper modelMapper;

    @Test
    void Should_ReturnResponseOfNewCounselEntity_When_RequestCounsel() {
        Counsel entity = Counsel.builder()
                .name("Member Kim")
                .cellPhone("010-1111-2222")
                .email("mail@abc.de")
                .memo("I hope to get a loan")
                .zipCode("123456")
                .address("Somewhere in Gangnam-gu, Seoul")
                .addressDetail("What Apartment No. 101, 1st floor No. 101")
                .build();

        Request request = Request.builder()
                .name("Member Kim")
                .cellPhone("010-1111-2222")
                .email("mail@abc.de")
                .memo("I hope to get a loan")
                .zipCode("123456")
                .address("Somewhere in Gangnam-gu, Seoul")
                .addressDetail("What Apartment No. 101, 1st floor No. 101")
                .build();

        when(counselRepository.save(ArgumentMatchers.any(Counsel.class))).thenReturn(entity);

        Response actual = counselService.create(request);

        assertThat(actual.getName()).isSameAs(entity.getName());

    }


    }