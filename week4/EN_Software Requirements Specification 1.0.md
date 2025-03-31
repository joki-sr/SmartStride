# Data module Requirements Specification 1.0

| Date | Author | Description |
| ---- | ------ | ----------- |
| March 28 | SmartStride | Data module |

[toc]

---

This document covers the main needs of patients and database administrators and provides detailed **goals, frequencies, preconditions, postconditions and processes** to ensure that the requirements are clear and implementable.

## **User case diagram**

![EN_usercase](D:\学习资料\3-2学期课程\DSD\RA\EN_usercase.png)

## **End User（Patient）**  

A person using a rehabilitation device is considered an End User.  

### **Power On Device**  

- **Goal**: To turn on the rehabilitation device and prepare it for data collection.  
- **Summary**: The patient powers on the device, which initializes sensors and prepares for motion data collection.  
- **Frequency**: Every rehabilitation session.  
- **Precondition**: The device is charged and in working condition.  
- **Postconditions**: The device is turned on, sensors are initialized, and ready for connection.  

#### **Flows**  

| Actor  | System |
|--------|--------|
| Patient presses power button | Device powers on and initializes sensors |

---

### **Connect Device to Data Management System**  

- **Goal**: To establish a connection between the rehabilitation device and the cloud-based data management system.  
- **Summary**: The patient pairs the device with the system, enabling real-time data transmission and remote monitoring.  
- **Frequency**: Every rehabilitation session.  
- **Precondition**: The device is powered on, and the system is accessible.  
- **Postconditions**: A stable connection is established, allowing data synchronization.  

#### **Flows**  

| Actor  | System |
|--------|--------|
| Patient initiates device pairing | System searches for available devices |
| Patient selects the correct device | System establishes a secure connection |
| Patient confirms connection | System displays connection status |

---

### **Start Sensor Data Collection**  

- **Goal**: To collect motion data from the patient's lower limbs during rehabilitation exercises.  
- **Summary**: Once the device is connected, the patient starts the data collection process, and sensor readings are recorded.  
- **Frequency**: Multiple times per session.  
- **Precondition**: The device is powered on and connected to the system.  
- **Postconditions**: Data is continuously recorded and transmitted.  

#### **Flows**  

| Actor  | System |
|--------|--------|
| Patient starts data collection | System begins recording sensor data |
| Patient performs rehabilitation movements | System captures and processes movement data |
| Patient stops data collection | System saves the collected data |

---

### **View Rehabilitation Progress**  

- **Goal**: To allow patients to track their rehabilitation progress.  
- **Summary**: The patient accesses the system's interface to review their historical data and progress insights.  
- **Frequency**: As needed.  
- **Precondition**: Data has been collected and processed.  
- **Postconditions**: The patient receives feedback on their rehabilitation progress.  

#### **Flows**  

| Actor  | System |
|--------|--------|
| Patient accesses progress dashboard | System retrieves and displays data insights |

---

## **Enterprise（Database Administrator）**  
The Enterprise MAY also act as an end user with the cloud platform, accessing data of any device.  
The Enterprise MAY only access embedded devices if physically present and connected.  

### **Set up Cloud & Configure Device**  

See Deployment Documentation.  

---
### **Abnormal Data Cleaning and Filtering**  

- **Goal**: Ensure the accuracy and continuity of collected data to improve the reliability of rehabilitation training analysis.  
- **Summary**:  
  - **Abnormal Data Removal**: Set reasonable joint angle thresholds. Data exceeding the range (e.g., knee joint angles should be between 0° and 140°) will be marked as invalid and excluded from analysis.  
  - **Data Compensation**: When BLE transmission is interrupted or data is lost, a linear interpolation method is used to repair missing data, ensuring data continuity and completeness.  
- **Frequency**: Automatically executed each time data is uploaded to the server or before data analysis.  
- **Precondition**: The device has successfully collected raw motion data and uploaded it to the server.  
- **Postcondition**: Abnormal data is removed, missing data is compensated, and data integrity is ensured.  

#### **Flows**  

| Actor  | System |
|--------|--------|
| Server receives motion data | Parses data and detects anomalies |
| System detects abnormal data | Marks invalid data and removes it from the analysis dataset |
| Server detects missing data | Uses linear interpolation to compensate for missing values |
| Data processing completes | Stores cleaned data into the database |

---

### **Database CRUD (manage patients' data)**  

- **Goal**: Manage rehabilitation data in the system, including storing, updating, querying, and deleting records to ensure data integrity and availability.  
- **Summary**: The database administrator can perform CRUD operations on patient rehabilitation data to support data maintenance and analysis.  
- **Frequency**: Performed periodically or as needed.  
- **Precondition**: The system has stored patient rehabilitation data, and the administrator has the necessary access permissions.  
- **Postcondition**: Database contents are updated, ensuring data accuracy and consistency.  

#### **Flows**  

| Actor  | System |
|--------|--------|
| Database administrator requests to insert data | System validates the data format and inserts it into the database |
| Database administrator requests to update data | System updates relevant records in the database |
| Database administrator requests to query data | System retrieves and returns matching records |
| Database administrator requests to delete data | System removes specified data from the database |

---

### **Deliver Data to Analysis Team**  

- **Goal**: To transfer raw rehabilitation data for further analysis.  
- **Summary**: The system allows administrators to send collected data to the analysis team for in-depth processing.  
- **Frequency**: Periodically, based on analysis requirements.  
- **Precondition**: Data collection has been completed.  
- **Postconditions**: The analysis team receives structured data.  

#### **Flows**  

| Actor  | System |
|--------|--------|
| Admin selects data batch | System prepares data for export |
| Admin confirms data transfer | System sends data to analysis team |

---

### **Store Analysis Results**  

- **Goal**: To securely store analyzed data for future reference and comparisons.  
- **Summary**: Once data has been processed, it is stored in the system for medical professionals to review.  
- **Frequency**: Ongoing.  
- **Precondition**: Data analysis has been completed.  
- **Postconditions**: The system securely saves the analyzed data.  

#### **Flows**  

| Actor  | System |
|--------|--------|
| Analysis team submits results | System verifies and stores data |

---

### **Provide Processed Data to UI for Visualization**  

- **Goal**: To allow medical professionals to visually analyze patient data.  
- **Summary**: The system presents processed data in an easy-to-understand format, aiding in decision-making.  
- **Frequency**: As needed.  
- **Precondition**: Processed data is available.  
- **Postconditions**: The user interface displays data insights.  

#### **Flows**  

| Actor  | System |
|--------|--------|
| Admin requests data visualization | System retrieves and displays processed data |

---

