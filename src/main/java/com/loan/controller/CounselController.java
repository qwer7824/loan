package com.loan.controller;

import com.loan.dto.CounselDTO.Request;
import com.loan.dto.CounselDTO.Response;
import com.loan.dto.ResponseDTO;
import com.loan.service.CounselService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/counsels")
public class CounselController extends AbstractController {

    private final CounselService counselService;

    @PostMapping
    public ResponseDTO<Response> create(@RequestBody Request request){
        return ok(counselService.create(request));

    }

    @GetMapping("/{counselId}")
    public ResponseDTO<Response> get(@PathVariable Long counselId){
        return ok(counselService.get(counselId));
    }


}
