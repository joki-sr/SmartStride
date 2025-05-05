package com.example.medicalsystem.service;

import java.io.File;
import java.io.IOException;

/**
 * 数据管理服务接口，用于处理CSV文件数据
 */
public interface DataManagerService {
    
    /**
     * 将CSV文件数据存储到数据库中
     * @param csvFile CSV文件
     * @return 处理结果消息
     * @throws IOException 如果文件处理过程中发生IO异常
     */
    String storeCsv(File csvFile) throws IOException;
    
    /**
     * 对CSV文件进行数据清洗和时间对齐，然后存储到数据库中
     * 通过调用Python脚本实现
     * @param csvFile CSV文件
     * @return 处理结果消息
     * @throws IOException 如果文件处理过程中发生IO异常
     */
    String cleanCsv(File csvFile) throws IOException;
}
