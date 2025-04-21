-- CREATE DATABASE test;
USE dsd;

CREATE TABLE doctor (
    work_id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(10) NOT NULL,
    hospital_name VARCHAR(20) NOT NULL
);

CREATE TABLE patient (
    patient_id VARCHAR(10) PRIMARY KEY,
    patient_pswd VARCHAR(50),
    name VARCHAR(10),
    gender ENUM('M','F'),
    disease TEXT,
    doctor_id VARCHAR(10),
    FOREIGN KEY (doctor_id) REFERENCES doctor(work_id)
);

CREATE TABLE coordinate (
    id INT PRIMARY KEY,
    time DATETIME NOT NULL,
    x FLOAT NOT NULL,
    y FLOAT NOT NULL,
    z FLOAT NOT NULL
);

CREATE TABLE analyzed_data (
    id INT PRIMARY KEY
);

-- CREATE TABLE history_record (
--     history_id INT PRIMARY KEY,
--     history_data_name VARCHAR(50) NOT NULL
-- );

INSERT INTO doctor (work_id, name, hospital_name) VALUES
('D001', '张三', '北京协和医院'),
('D002', '李四', '上海瑞金医院'),
('D003', '王五', '广州中山医院');

INSERT INTO patient (patient_id, patient_pswd, name, gender, disease, doctor_id) VALUES
('P001', '123456', '赵一', 'M', '感冒', 'D001'),
('P002', '123456', '钱二', 'F', '肺炎', 'D001'),
('P003', '123456', '孙三', 'M', '高血压', 'D002'),
('P004', '123456', '李四', 'F', '糖尿病', 'D003'),
('P005', '123456', '周五', 'M', '哮喘', 'D003');

INSERT INTO coordinate (id, time, x, y, z) VALUES
(1, '2025-04-16 10:00:00', 1.1, 2.2, 3.3),
(2, '2025-04-16 10:05:00', 4.4, 5.5, 6.6),
(3, '2025-04-16 10:10:00', 7.7, 8.8, 9.9);

INSERT INTO analyzed_data (id) VALUES
(1),
(2),
(3);

-- INSERT INTO history_record (history_id, history_data_name) VALUES
-- (0,"history_record_name_1");

SELECT * FROM doctor;
SELECT * FROM patient;
SELECT * FROM coordinate;
SELECT * FROM analyzed_data;
-- select * from history_record;

