<h1 align="center">DynaApp </h1>


## How To Start
  Clone this reposity, then try one of these:<br>  
    - java -jar target/dynaApp-0.0.1-SNAPSHOT.jar while in the source folder<br>
    - mvn spring-boot:run (If You have Maven installed)<br>
    - run app trough the IntelliJ
    
## Swagger Documentation:
 - http://localhost:8080/swagger-ui/index.html#/
 
## How to use(examples):
  - to get average exchange rate:<br>
  curl "http://localhost:8080/rate?date=2023-04-15&code=usd"
  
  - to get max and min average value<br>
    curl "http://localhost:8080/minmax?code=USD&quotations=255"<br>
    min should be: 4.2006<br>
    max should be: 5.0381
  
  - to get the major difference between<br>
    curl "http://localhost:8080/diff?code=USD&quotations=255"<br>
    difference should be: 0.1004
