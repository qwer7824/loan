package com.loan.controller;

import com.loan.dto.ApplicationDTO;
import com.loan.dto.JudgmentDTO.*;
import com.loan.dto.ResponseDTO;
import com.loan.service.JudgmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/judgments")
public class JudgmentController extends AbstractController {

    private final JudgmentService judgmentService;

    @PostMapping
    public ResponseDTO<Response> create(@RequestBody Request request) {
        return ok(judgmentService.create(request));
    }
    @GetMapping("/{judgmentId}")
    public ResponseDTO<Response> get(@PathVariable Long judgmentId){
        return ok(judgmentService.get(judgmentId));
    }

    @GetMapping("/applications/{applicationId}")
    public ResponseDTO<Response> getJudgmentOfApplication(@PathVariable Long applicationId){
        return ok(judgmentService.getJudgmentOfApplication(applicationId));
    }

    @DeleteMapping("/{judgmentId}")
    public ResponseDTO<Void> delete(@PathVariable Long judgmentId){
        judgmentService.delete(judgmentId);
        return ok();
    }
    @PatchMapping("/{judgmentId}/grant") // 심사 Id 를 최종적으로 승인을 해주면 승인된 금액이 반영
    public ResponseDTO<ApplicationDTO.GrantAmount> grant(@PathVariable Long judgmentId){
        return ok(judgmentService.grant(judgmentId));
    }
}

