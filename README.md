# Geo Resolver Application
Java Spring-Boot 2 application for Geolocation API services, originally designed for Gollgi.com and released in 2020 as an open source project  
On API's invocation Geo-Resolrcer engine will retrieve the richiest location results from a list of Geolocation provider

## Geo Resolver API REST Endpoints
- '/secured/get-location-by-resolver/{address}/{resolver}' 
    - This endpoint will produce Geolocation object by a given address and provider's name
        
    
- '/secured/get-location-by-resolver/{address}/{resolver}' 
    - This endpoint will produce Geolocation object by a given address and will produce the richest results from a list of Geolocation providers  
      
    
- '/secured/get-resolver' 
    - This endpoint will produce a list of all available Geolocation providers  
      


## Git Branches
- master - For Deploying a Spring-Boot application as a Tomcat war/ Embedded Tomcat jar  files
- geo-location-wildfly - For deploying a Spring-Boot application as a Wildfly/Jboss (or any JEE application server) war file


# Module Major Dependencies
- Spring-Boot V2.3.1.RELEASE
- Spring-Security V2.3.1.RELEASE
- Spring-Date V2.0.0.RELEASE

# Server Specifications
- Java Maven project
- Spring-Boot
- Persistence - Spring-JPA-repository
- Security - Spring-Security


# Client Specifications
- Angular V5
- Bootstrap 4

# Environment
 - Ubuntu/Windows
 
# Requirements
- JVM
- DB connection

## Running up Environment 
- server
    - `/geo-resolver/`
    - `$ mvn spring boot:run`
      
- client
    - `/geo-resolver/angular-client`
    - `$ ng serve`


## Accessing UI For Tester Page
- http://localhost:8080/


# Contact
- For any questions you can send a mail to orenhoffman1777@gmail.com