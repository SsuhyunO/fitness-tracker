package com.example.analysis_service.controller;

import com.example.analysis_service.dto.AnalysisRequestDto;
import com.example.analysis_service.entity.Analysis;
import com.example.analysis_service.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
public class AnalysisController {

    private final AnalysisService analysisService;

    @PostMapping
    public ResponseEntity<Analysis> createAnalysis(@RequestBody AnalysisRequestDto dto) {
        Analysis result = analysisService.analyzeAndSave(dto); 
        return ResponseEntity.ok(result);
    }
}