package com.example.medicalsystem.service.impl;

import com.example.medicalsystem.dto.UploadResultDTO;
import com.example.medicalsystem.entity.ImuData;
import com.example.medicalsystem.entity.Patient;
import com.example.medicalsystem.exception.NotFoundException;
import com.example.medicalsystem.repository.ImuDataRepository;
import com.example.medicalsystem.repository.PatientRepository;
import com.example.medicalsystem.service.ImuDataService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ImuDataServiceImpl implements ImuDataService {

    private final ImuDataRepository imuDataRepository;
    private final PatientRepository patientRepository;
    private final ObjectMapper objectMapper;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public ImuDataServiceImpl(
            ImuDataRepository imuDataRepository,
            PatientRepository patientRepository,
            ObjectMapper objectMapper) {
        this.imuDataRepository = imuDataRepository;
        this.patientRepository = patientRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public UploadResultDTO uploadData(Long patientId, List<Map<String, Object>> dataPoints) throws NotFoundException {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new NotFoundException("患者不存在"));
        
        LocalDateTime now = LocalDateTime.now();
        
        for (Map<String, Object> dataPoint : dataPoints) {
            ImuData imuData = new ImuData();
            imuData.setPatient(patient);
            imuData.setTimestamp(now);
            
            try {
                String dataJson = objectMapper.writeValueAsString(dataPoint);
                imuData.setData(dataJson);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("数据转换失败", e);
            }
            
            imuDataRepository.save(imuData);
        }
        
        UploadResultDTO result = new UploadResultDTO();
        result.setReceivedAt(now.format(DATE_FORMATTER));
        
        return result;
    }

    @Override
    public List<Map<String, Object>> getDataByPatientId(Long patientId, String startTime, String endTime) {
        LocalDateTime start = LocalDateTime.parse(startTime, DATE_FORMATTER);
        LocalDateTime end = LocalDateTime.parse(endTime, DATE_FORMATTER);
        
        List<ImuData> dataList = imuDataRepository.findByPatientIdAndTimestampBetween(patientId, start, end);
        
        return dataList.stream()
                .map(this::convertToMap)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteDataByPatientId(Long patientId, String startTime, String endTime) {
        LocalDateTime start = LocalDateTime.parse(startTime, DATE_FORMATTER);
        LocalDateTime end = LocalDateTime.parse(endTime, DATE_FORMATTER);
        
        imuDataRepository.deleteByPatientIdAndTimestampBetween(patientId, start, end);
    }
    
    private Map<String, Object> convertToMap(ImuData imuData) {
        try {
            Map<String, Object> data = objectMapper.readValue(imuData.getData(), new TypeReference<Map<String, Object>>() {});
            data.put("timestamp", imuData.getTimestamp().format(DATE_FORMATTER));
            return data;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("数据解析失败", e);
        }
    }
}
