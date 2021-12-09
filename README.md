# spring-boot-user-management-service

User Management Service created and managed by JWT 


## Usage


- Spring Boot Web
- Spring Boot Test
- Spring Boot Security
- JWT Auth
- SpringDoc OpenAPI3
- MimeMessageHelper
- MimeMessage
- IntegrationTest
- MVCMock
- H2Database


---

## Installation

- First,We define security and jwt dependencies

```java
      <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>

    <dependency>
            <groupId>com.auth0</groupId>
            <artifactId>java-jwt</artifactId>
            <version>3.18.2</version>
        </dependency>
```
- Second,We do Security Configuration


![User-Management-Service-Configure](https://user-images.githubusercontent.com/80245013/145387159-e0a08584-857d-4bd5-8f27-bbef5a7ffc09.PNG)



- Last Added @SecurityRequirement(name = "bearerAuth") in Controller.Class


---
### How do we get our JWT token in OpenAPI3?

- First,Connect to Swagger


![User-Management-Service-Access-TokenOne](https://user-images.githubusercontent.com/80245013/145387187-58436050-8859-4ccb-9f95-ce42892be06e.PNG)



- Second,Enter Username and Password


![User-Management-Service-Access-TokenTwo](https://user-images.githubusercontent.com/80245013/145387222-1dcd587a-cd91-4d2d-8e35-9d1c58f0e41c.PNG)



- Last, If we log in successfully, we can get our token. If you log in incorrectly, we get a warning.



![User-Management-Service-Access-TokenThree](https://user-images.githubusercontent.com/80245013/145387251-19c26153-8498-435d-8b3c-c061c9d1c7dd.PNG)



---

## Owner
[Muharrem Koç](https://github.com/muharremkoc)
