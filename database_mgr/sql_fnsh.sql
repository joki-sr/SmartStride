-- CREATE DATABASE test;
USE dsd;

CREATE TABLE doctor (
    work_id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(10) NOT NULL,
    hospital_name VARCHAR(20) NOT NULL
);

CREATE TABLE patient (
    patient_id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(10) NOT NULL,
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

INSERT INTO doctor (work_id, name, hospital_name) VALUES
('D001', '张三', '北京协和医院'),
('D002', '李四', '上海瑞金医院'),
('D003', '王五', '广州中山医院');

INSERT INTO patient (patient_id, name, gender, disease, doctor_id) VALUES
('P001', '赵一', 'M', '感冒', 'D001'),
('P002', '钱二', 'F', '肺炎', 'D001'),
('P003', '孙三', 'M', '高血压', 'D002'),
('P004', '李四', 'F', '糖尿病', 'D003'),
('P005', '周五', 'M', '哮喘', 'D003');


-- 不需要raw
-- CREATE TABLE raw (
--     id VARCHAR(100) PRIMARY KEY,
--     time_stamp DATETIME NOT NULL,
--     device_name TEXT NOT NULL,
--     accel_x REAL,
--     accel_y REAL,
--     accel_z REAL,
--     gyro_x REAL,
--     gyro_y REAL,
--     gyro_z REAL,
--     angle_x REAL,
--     angle_y REAL,
--     angle_z REAL,
--     quat_0 REAL,
--     quat_1 REAL,
--     quat_2 REAL,
--     quat_3 REAL
-- );
-- INSERT INTO raw (
--     id, time_stamp, device_name,
--     accel_x, accel_y, accel_z,
--     gyro_x, gyro_y, gyro_z,
--     angle_x, angle_y, angle_z,
--     quat_0, quat_1, quat_2, quat_3
-- ) VALUES (
--     'A4TCCCcOpH8j7oTmMud4gA==', '2025-04-17 05:54:22.601',
--     'WT9O1BLE67', 0.1035, -0.1118, 1.0654,
--     29.85, 16.54, 18.92,
--     0.0, 0.0, 0.0,
--     0.0, 0.0, 0.0, 0.0
-- );


INSERT INTO coordinate (id, time, x, y, z) VALUES
(1, '2025-04-16 10:00:00', 1.1, 2.2, 3.3),
(2, '2025-04-16 10:05:00', 4.4, 5.5, 6.6),
(3, '2025-04-16 10:10:00', 7.7, 8.8, 9.9);

INSERT INTO analyzed_data (id) VALUES
(1),
(2),
(3);

-- SELECT * FROM doctor;
-- SELECT * FROM patient;
-- SELECT * FROM raw;
-- SELECT * FROM coordinate;
-- SELECT * FROM analyzed_data;

select * from sensor_raw_data;
select * from sensor_time_clean_data;


select *
from sensor_raw_data
where 时间戳='2025-04-17 06:16:46'
;

select *
from sensor_time_clean_data
where 时间戳='2025-04-17 06:16:46'
;

