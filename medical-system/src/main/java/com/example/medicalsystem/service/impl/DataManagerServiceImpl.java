package com.example.medicalsystem.service.impl;

import com.example.medicalsystem.entity.ImuData;
import com.example.medicalsystem.repository.ImuDataRepository;
import com.example.medicalsystem.service.DataManagerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataManagerServiceImpl implements DataManagerService {

    private static final Logger logger = LoggerFactory.getLogger(DataManagerServiceImpl.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Value("${python.script.path:/scripts}")
    private String pythonScriptPath;

    @Value("${python.executable:python}")
    private String pythonExecutable;

    private final ImuDataRepository imuDataRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public DataManagerServiceImpl(ImuDataRepository imuDataRepository, ObjectMapper objectMapper) {
        this.imuDataRepository = imuDataRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public String storeCsv(File csvFile) throws IOException {
        logger.info("开始处理CSV文件: {}", csvFile.getName());
        
        List<ImuData> dataList = new ArrayList<>();
        int lineCount = 0;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            String[] headers = null;
            
            // 读取CSV头部
            if ((line = reader.readLine()) != null) {
                headers = line.split(",");
            }
            
            if (headers == null) {
                throw new IOException("CSV文件格式错误：没有找到头部");
            }
            
            // 读取数据行
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length != headers.length) {
                    logger.warn("跳过格式不正确的行: {}", line);
                    continue;
                }
                
                Map<String, Object> dataMap = new HashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    try {
                        // 尝试将数值转换为数字
                        dataMap.put(headers[i].trim(), Double.parseDouble(values[i].trim()));
                    } catch (NumberFormatException e) {
                        // 如果不是数字，则保留为字符串
                        dataMap.put(headers[i].trim(), values[i].trim());
                    }
                }
                
                ImuData imuData = new ImuData();
                imuData.setTimestamp(LocalDateTime.now());
                imuData.setData(objectMapper.writeValueAsString(dataMap));
                
                dataList.add(imuData);
                lineCount++;
            }
        }
        
        // 批量保存到数据库
        imuDataRepository.saveAll(dataList);
        
        logger.info("CSV文件处理完成，共处理 {} 行数据", lineCount);
        return String.format("成功处理CSV文件 %s，共导入 %d 行数据", csvFile.getName(), lineCount);
    }

    @Override
    public String cleanCsv(File csvFile) throws IOException {
        logger.info("开始清洗CSV文件: {}", csvFile.getName());
        
        // 构建Python脚本路径
        String cleanScriptPath = pythonScriptPath + "/clean_csv.py";
        
        // 构建命令
        ProcessBuilder processBuilder = new ProcessBuilder(
                pythonExecutable,
                cleanScriptPath,
                csvFile.getAbsolutePath()
        );
        
        // 设置工作目录
        processBuilder.directory(new File(pythonScriptPath).getParentFile());
        
        // 合并标准错误和标准输出
        processBuilder.redirectErrorStream(true);
        
        try {
            // 启动进程
            Process process = processBuilder.start();
            
            // 读取输出
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }
            
            // 等待进程完成
            int exitCode = process.waitFor();
            
            if (exitCode == 0) {
                logger.info("Python脚本执行成功: {}", output);
                return String.format("成功清洗CSV文件 %s: %s", csvFile.getName(), output);
            } else {
                logger.error("Python脚本执行失败，退出码: {}, 输出: {}", exitCode, output);
                throw new IOException("Python脚本执行失败: " + output);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Python脚本执行被中断", e);
        }
    }
}
