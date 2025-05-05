package com.example.medicalsystem.service;

import com.example.medicalsystem.dto.UploadResultDTO;
import com.example.medicalsystem.exception.NotFoundException;

import java.util.List;
import java.util.Map;

public interface ImuDataService {
    /**
     * 上传IMU数据
     * @param patientId 患者ID
     * @param dataPoints 数据点列表
     * @return 上传结果
     */
    UploadResultDTO uploadData(Long patientId, List<Map<String, Object>> dataPoints) throws NotFoundException;
    
    /**
     * 获取患者的IMU数据
     * @param patientId 患者ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 数据点列表
     */
    List<Map<String, Object>> getDataByPatientId(Long patientId, String startTime, String endTime);
    
    /**
     * 删除患者的IMU数据
     * @param patientId 患者ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    void deleteDataByPatientId(Long patientId, String startTime, String endTime);
}
