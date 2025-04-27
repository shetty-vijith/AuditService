# AuditService
Audit Service is a container based microservice subscribing to notifications and saving the events in the database.

## Getting Started
### Dependencies
Docker <br/>
Java 21 <br/>
Maven <br/>

### Steps to execute
Build the project
```
mvn clean package
```
Build the docker image
```
docker compose build
```
Launch the service
```
docker compose up
```
### Steps to test
Notification API's can be used to simulate the inter service communication, these should generate events on Kafka <br/>
http://localhost:8080/v1/notification/login - Generates LOGIN event <br/>
http://localhost:8080/v1/notification/getClientInfo - Generates GET_CLIENT_INFO event <br/>
http://localhost:8080/v1/notification/scan - Generates SCAN event <br/>

Audit API to view the audit events based on the roles <br/>
http://localhost:8080/v1/audit/messages?userId=user1 - User1 -> ADMIN -> {LOGIN, GET_CLIENT_INFO, SCAN} <br/>
http://localhost:8080/v1/audit/messages?userId=user2 - User2 -> SALES -> {GET_CLIENT_INFO} <br/>
http://localhost:8080/v1/audit/messages?userId=user3 - User3 -> DEVELOPER -> {LOGIN, SCAN} <br/>

Config API to view and set the rollover time <br/>
GET: http://localhost:8080/v1/config - {"rolloverInDays": 7} <br/>
PUT: http://localhost:8080/v1/config - {"rolloverInDays": 7} <br/>
