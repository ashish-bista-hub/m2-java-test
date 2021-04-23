# spring-boot-h2-crud

This project explains CRUD (**C**reate, **R**ead, **U**pdate, **D**elete) operations using spring boot and H2 in-memory database.
In this app we are using Spring Data JPA for built-in methods to do CRUD operations.     
`@EnableJpaRepositories` annotation is used on main class to Enable H2 DB related configuration, which will read properties from `application.properties` file.

## Prerequisites 
- Java 8
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Maven](https://maven.apache.org/guides/index.html)
- [H2 Database](https://www.h2database.com/html/main.html)
- [Lombok](https://objectcomputing.com/resources/publications/sett/january-2010-reducing-boilerplate-code-with-project-lombok)


## Tools
- Eclipse or IntelliJ IDEA (or any preferred IDE) with embedded Gradle
- Maven (version >= 3.6.0)
- Postman (or any RESTful API testing tool)

<br/>

###  Build and Run application
_GOTO >_ **~/absolute-path-to-directory/m2-msisdn**  
and try below command in terminal
> **```mvn spring-boot:run```** it will run application as spring boot application

or
> **```mvn clean install```** it will build application and create **jar** file under target directory 

Run jar file from below path with given command
> **```java -jar ~/path-to-spring-boot-h2-crud/target/m2-msisdn-0.0.1-SNAPSHOT.jar```**

Or
> run main method from `M2MsisdnApplication.java` as spring boot application.  


### Code Snippets
1. #### Maven Dependencies
    Need to add below dependencies to enable H2 DB related config in **pom.xml**. Lombok's dependency is to get rid of boiler-plate code.   
    ```
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
   
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
   
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    ```
   
   
2. #### Properties file
    Reading H2 DB related properties from **application.properties** file and configuring JPA connection factory for H2 database.  

    **src/main/resources/application.properties**
     ```
     server.port=8088
    
     spring.datasource.url=jdbc:h2:mem:testdb
     spring.datasource.driverClassName=org.h2.Driver
     spring.datasource.username=sa
     spring.datasource.password=
     spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
    
     spring.h2.console.enabled=true
    
     #spring.data.rest.base-path=/phone
     spring.data.rest.base-default-page-size=10
     spring.data.rest.base-max-page-size=20
    
     springdoc.version=1.0.0
     springdoc.swagger-ui.path=/swagger-ui-custom.html 
     ```
      
3. ### API Endpoints (try http://localhost:7080/swagger-ui-custom.html)

- #### MSISDN csv file upload & download
    > **GET Mapping** http://localhost:7080/v1/m2/msisdn  - Get all MSISDN entries 
    
    > **GET Mapping** http://localhost:7080/v1/m2/msisdn/+912346789012  - Get MSISDN by ID
    
    > **GET Mapping** http://localhost:7080/v1/m2/msisdn/download/rejected/  - Download rejected MSISDN entries 
    
    > **GET Mapping** http://localhost:7080/v1/m2/msisdn/download/output/  - Downwload output MSISDN.txt file 
    
    > **POST Mapping** http://localhost:7080/v1/m2/msisdn  - Upload csv file and process 

4. ### H2 Database (try http://localhost:7080/h2-console)

    > Enter url=jdbc:h2:mem:testdb
    > Enter user=sa
    > Enter password=
    > Test Connection Or
    > Connect

