package com.loan.controller;

import com.loan.dto.ResponseDTO;
import com.loan.dto.TermsDTO.*;
import com.loan.service.TermsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/terms")
public class TermsController extends AbstractController{

    private final TermsService termsService;

    @PostMapping
    public ResponseDTO<Response> create(@RequestBody Request request){
        return ok(termsService.create(request));
    }

    @GetMapping
    public ResponseDTO<List<Response>> getAll(){
        return ok(termsService.getAll());
    }
}
