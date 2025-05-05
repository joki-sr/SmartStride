package com.example.medicalsystem.service;

import com.example.medicalsystem.dto.DoctorPatientRelationDTO;
import com.example.medicalsystem.exception.NotFoundException;

import java.util.List;

public interface DoctorPatientRelationService {
    /**
     * 获取所有医生-患者关系
     * @return 关系列表
     */
    List<DoctorPatientRelationDTO> getAllRelations();
    
    /**
     * 添加医生-患者关系
     * @param doctorId 医生ID
     * @param patientId 患者ID
     * @return 创建的关系
     */
    DoctorPatientRelationDTO addRelation(Long doctorId, Long patientId);
    
    /**
     * 更新医生-患者关系
     * @param relationId 关系ID
     * @param doctorId 医生ID
     * @param patientId 患者ID
     * @return 更新后的关系
     */
    DoctorPatientRelationDTO updateRelation(Long relationId, Long doctorId, Long patientId) throws NotFoundException;
    
    /**
     * 删除医生-患者关系
     * @param relationId 关系ID
     */
    void deleteRelation(Long relationId) throws NotFoundException;
}
