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
Notification API's can be used to simulate the inter service communication, these should generate events on Kafka
http://localhost:8080/v1/notification/login - Generates LOGIN event
http://localhost:8080/v1/notification/getClientInfo - Generates GET_CLIENT_INFO event
http://localhost:8080/v1/notification/scan - Generates SCAN event

Audit API to view the audit events based on the roles
http://localhost:8080/v1/audit/messages?userId=user1 - User1 -> ADMIN -> {LOGIN, GET_CLIENT_INFO, SCAN}
http://localhost:8080/v1/audit/messages?userId=user2 - User2 -> SALES -> {GET_CLIENT_INFO}
http://localhost:8080/v1/audit/messages?userId=user3 - User3 -> DEVELOPER -> {LOGIN, SCAN}

Config API to view and set the rollover time
GET: http://localhost:8080/v1/config - {"rolloverInDays": 7}
PUT: http://localhost:8080/v1/config - {"rolloverInDays": 7}
