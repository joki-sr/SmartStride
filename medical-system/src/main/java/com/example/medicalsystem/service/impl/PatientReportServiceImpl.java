package com.example.medicalsystem.service.impl;

import com.example.medicalsystem.dto.PatientReportDTO;
import com.example.medicalsystem.entity.Patient;
import com.example.medicalsystem.entity.PatientReport;
import com.example.medicalsystem.exception.NotFoundException;
import com.example.medicalsystem.repository.PatientReportRepository;
import com.example.medicalsystem.repository.PatientRepository;
import com.example.medicalsystem.service.PatientReportService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PatientReportServiceImpl implements PatientReportService {

    private final PatientReportRepository reportRepository;
    private final PatientRepository patientRepository;
    private final ObjectMapper objectMapper;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public PatientReportServiceImpl(
            PatientReportRepository reportRepository,
            PatientRepository patientRepository,
            ObjectMapper objectMapper) {
        this.reportRepository = reportRepository;
        this.patientRepository = patientRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<PatientReportDTO> getReportsByPatientId(Long patientId) {
        return reportRepository.findByPatientId(patientId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PatientReportDTO> getReportsByPatientIdAndType(Long patientId, String type) {
        return reportRepository.findByPatientIdAndType(patientId, type).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PatientReportDTO createReport(Long patientId, PatientReportDTO reportDTO) throws NotFoundException {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new NotFoundException("患者不存在"));
        
        PatientReport report = new PatientReport();
        report.setPatient(patient);
        report.setType(reportDTO.getType());
        report.setSummary(reportDTO.getSummary());
        report.setCreatedAt(LocalDateTime.now());
        
        try {
            String dataJson = objectMapper.writeValueAsString(reportDTO.getData());
            report.setData(dataJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("数据转换失败", e);
        }
        
        PatientReport savedReport = reportRepository.save(report);
        
        return convertToDTO(savedReport);
    }

    @Override
    @Transactional
    public void deleteReport(Long reportId) throws NotFoundException {
        if (!reportRepository.existsById(reportId)) {
            throw new NotFoundException("报告不存在");
        }
        
        reportRepository.deleteById(reportId);
    }
    
    private PatientReportDTO convertToDTO(PatientReport report) {
        PatientReportDTO dto = new PatientReportDTO();
        dto.setDate(report.getCreatedAt().format(DATE_FORMATTER));
        dto.setType(report.getType());
        dto.setSummary(report.getSummary());
        
        try {
            Map<String, int[]> data = objectMapper.readValue(report.getData(), new TypeReference<Map<String, int[]>>() {});
            dto.setData(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("数据解析失败", e);
        }
        
        return dto;
    }
}
