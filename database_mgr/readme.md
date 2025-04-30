
#### 运行
1. 运行sql template建表

2. 修改两个.py文件的csv_file路径

3. 修改clean_.py的frequency
4. 分别运行
```commandline
raw_data.py
clean.py
```
```sql
SELECT * FROM dsd.`imu_data_2025-04-19-13-04-59-20hz.csv` WHERE time_id=0;
```