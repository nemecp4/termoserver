1. Create User sensor
2. prepare log dir
3. prepare db dir 


as root
-------
useradd --system sensor
install -d -o sensor /var/log/sensorServer/
install -d -o sensor /var/run/sensorServer/
