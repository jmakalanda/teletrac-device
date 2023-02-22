# Read Me First
This project is a Maven project. This project covers the following Requirement:

Using any Java tools/framework, develop a backend to implement the following
- Accept a JSON payload via HTTP POST with mandatory field validation
    - NOTES: IS NULL and IS EMPTY validations.
- Simple token based authorization
    - NOTES: Simple Jwt token based authorization with one hardcoded user. Please refer to the class JwtUserDetailsService for details
- Store the content in the database
    - NOTES: Device details are persisted using an H2 runtime DB
- Audit the request
    - NOTES:Basic in memory auditing via actuator, auditing authorization
- Integration test
    - NOTES: Integration test covering Generic Authorization, Generic Device Controller, Validation Test and Persistence (DB) test.
- Simple demo using curl
  - If URL ends in ‘/nocontent’, return HTTP 204
  - If URL is ‘/echo’, return HTTP 200 and original payload
  - If URL is ‘/device’, return HTTP 200 and only ‘DeviceId’ field
  - Else return HTTP 400

          Sample payload
          {
          "recordType": "xxx",
          "deviceId": "357370040159770",
          "eventDateTime": "2014-05-12T05:09:48Z",
          "fieldA": 68,
          "fieldB": "xxx",
          "fieldC": 123.45
          }

    
# How to build and run
- mvn clean install
- ./mvnw spring-boot:run

# CURL commands
## Auth request to get the JWT
    curl -v -d '{"username": "foo","password": "foo"}' -H "Content-Type: application/json" http://localhost:8080/login

## Auth echo POST request (200 OK)
    curl -v -d '{"recordType": "xxx","deviceId": "357370040159770", "eventDateTime": "2014-05-12T05:09:48Z","fieldA": 68,"fieldB": "xxx","fieldC": 123.45}' \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer <JWT>" \
    http://localhost:8080/echo

## Auth nocontent POST request (204 NO Content)
    curl -v -d '{"recordType": "xxx","deviceId": "357370040159770", "eventDateTime": "2014-05-12T05:09:48Z","fieldA": 68,"fieldB": "xxx","fieldC": 123.45}' \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer <JWT>" \
    http://localhost:8080/nocontent

## Auth create device POST request (201 Created)
    curl -v -d '{"recordType": "xxy","deviceId": "357370040159799", "eventDateTime": "2014-05-12T05:09:48Z","fieldA": 68,"fieldB": "xxx","fieldC": 123.45}' \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer <JWT>" \
    http://localhost:8080/device
    
    curl -v -d '{"recordType": "xxy","deviceId": "357370040159999", "eventDateTime": "2014-05-12T05:09:48Z","fieldA": 68,"fieldB": "xzx","fieldC": 123.45}' \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer <JWT>" \
    http://localhost:8080/device

##  GET all devices in a List from the DB (200 OK)
    curl -v \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer <JWT>" \
    -X GET \
    http://localhost:8080/device

##  Auth random URL - 400 Bad request
    curl -v \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer <JWT>" \
    http://localhost:8080/blahblah

## Unauth device GET request- 403 Forbidden
    curl -v \
    -H "Content-Type: application/json" \
    -X GET \
    http://localhost:8080/device

## Unauth random URL - 403 Forbidden
    -H "Content-Type: application/json" \
    -X GET \
    http://localhost:8080/blahblah

## Auth audit URL
    curl -H "Content-Type: application/json" \
    -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmb28iLCJleHAiOjE2NzcwODIwOTcsImlhdCI6MTY3NzA0NjA5N30.ILJrL8Fn_scrbWZ5ghK3-Y_6Fn53OgdYe6Kt3l_bpOg" \
    -X GET \
    http://localhost:8080/actuator/auditevents
