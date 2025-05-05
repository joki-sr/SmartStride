package com.example.medicalsystem.service;

import com.example.medicalsystem.dto.PatientReportDTO;
import com.example.medicalsystem.exception.NotFoundException;

import java.util.List;

public interface PatientReportService {
    /**
     * 获取患者的所有报告
     * @param patientId 患者ID
     * @return 报告列表
     */
    List<PatientReportDTO> getReportsByPatientId(Long patientId);
    
    /**
     * 获取特定类型的患者报告
     * @param patientId 患者ID
     * @param type 报告类型
     * @return 报告列表
     */
    List<PatientReportDTO> getReportsByPatientIdAndType(Long patientId, String type);
    
    /**
     * 创建新的患者报告
     * @param patientId 患者ID
     * @param reportDTO 报告数据
     * @return 创建的报告
     */
    PatientReportDTO createReport(Long patientId, PatientReportDTO reportDTO) throws NotFoundException;
    
    /**
     * 删除患者报告
     * @param reportId 报告ID
     */
    void deleteReport(Long reportId) throws NotFoundException;
}
