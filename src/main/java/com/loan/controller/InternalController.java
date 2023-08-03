package com.loan.controller;

import com.loan.dto.EntryDTO.*;
import com.loan.dto.ResponseDTO;
import com.loan.service.EntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/internal/applications")
public class InternalController extends AbstractController{ // 사후 처리에 대한 모든 관리를 내부임직원이 한다는 가정

    private final EntryService entryService;

    @PostMapping("{applicationId}/entries")
    public ResponseDTO<Response> create(@PathVariable Long applicationId, @RequestBody Request request) {
        return ok(entryService.create(applicationId, request));
    }

    @GetMapping("/{applicationId}/entries")
    public ResponseDTO<Response> get(@PathVariable Long applicationId){
        return ok(entryService.get(applicationId));
    }
    @PutMapping("/entries/{entryId}")
    public ResponseDTO<UpdateResponse> update(@PathVariable Long entryId, @RequestBody Request request) {
        return ok(entryService.update(entryId, request));
    }

    @DeleteMapping("/entries/{entryId}")
    public ResponseDTO<Void> delete(@PathVariable Long entryId){
        entryService.delete(entryId);
        return ok();
    }
}
