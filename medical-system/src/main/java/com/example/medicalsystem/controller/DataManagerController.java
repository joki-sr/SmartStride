package com.example.medicalsystem.controller;

import com.example.medicalsystem.service.DataManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/data")
@CrossOrigin(origins = "*")
public class DataManagerController {

    private static final Logger logger = LoggerFactory.getLogger(DataManagerController.class);
    
    private final DataManagerService dataManagerService;
    
    @Autowired
    public DataManagerController(DataManagerService dataManagerService) {
        this.dataManagerService = dataManagerService;
    }
    
    @PostMapping("/store-csv")
    public ResponseEntity<?> storeCsv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "请选择一个CSV文件上传");
            return ResponseEntity.badRequest().body(response);
        }
        
        if (!file.getOriginalFilename().endsWith(".csv")) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "只支持CSV文件格式");
            return ResponseEntity.badRequest().body(response);
        }
        
        try {
            // 创建临时文件
            String tempDir = System.getProperty("java.io.tmpdir");
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path tempFile = Paths.get(tempDir, fileName);
            
            // 保存上传的文件到临时目录
            Files.write(tempFile, file.getBytes());
            
            // 处理CSV文件
            String result = dataManagerService.storeCsv(tempFile.toFile());
            
            // 删除临时文件
            Files.deleteIfExists(tempFile);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", result);
            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            logger.error("处理CSV文件时发生错误", e);
            Map<String, String> response = new HashMap<>();
            response.put("error", "处理CSV文件时发生错误: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @PostMapping("/clean-csv")
    public ResponseEntity<?> cleanCsv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "请选择一个CSV文件上传");
            return ResponseEntity.badRequest().body(response);
        }
        
        if (!file.getOriginalFilename().endsWith(".csv")) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "只支持CSV文件格式");
            return ResponseEntity.badRequest().body(response);
        }
        
        try {
            // 创建临时文件
            String tempDir = System.getProperty("java.io.tmpdir");
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path tempFile = Paths.get(tempDir, fileName);
            
            // 保存上传的文件到临时目录
            Files.write(tempFile, file.getBytes());
            
            // 清洗CSV文件
            String result = dataManagerService.cleanCsv(tempFile.toFile());
            
            // 删除临时文件
            Files.deleteIfExists(tempFile);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", result);
            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            logger.error("清洗CSV文件时发生错误", e);
            Map<String, String> response = new HashMap<>();
            response.put("error", "清洗CSV文件时发生错误: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
