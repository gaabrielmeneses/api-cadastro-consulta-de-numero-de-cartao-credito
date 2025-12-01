@echo off
echo Starting Credit Card API locally...

echo Setting environment variables...
set DB_HOST=localhost
set DB_PORT=3306
set DB_NAME=creditcard_db
set DB_USERNAME=root
set DB_PASSWORD=password
set JWT_SECRET=mySecretKey123456789012345678901234567890
set ENCRYPTION_KEY=myEncryptionKey1234567890123456

echo Starting MySQL container...
docker run -d --name mysql-creditcard -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=creditcard_db -p 3306:3306 mysql:8.0

echo Waiting for MySQL to be ready...
timeout /t 30

echo Starting application...
cd ..
mvnw.cmd spring-boot:run

pause