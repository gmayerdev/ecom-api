# ecom-api
Simple E-commerce Application with a price surge mechanism


**Use Cases**
- Ability to have users visualize items online
- Utilize a price surge mechanism in order to raise prices on items that are visualized more than 10 times in the last hour

**Solution Design**


**Technical Specifications**
- Java 11
- Spring Boot 2.6.1
- Gradle

**How To**
1. Clone repo
2. Open a terminal and navigate to the folder
3. Build application using Gradle by running `gradle clean build`
4. Run application using Gradle by running `gradle bootRun`

**API**
1. Get Items
    - `curl -i -H "Content-Type: application/json" -X GET http://localhost:8088/items`
2. Get Item
    - `curl -i -H "Content-Type: application/json" -X GET http://localhost:8088/items`
3. Create Item
    - `curl -i -H "Content-Type: application/json" -X GET http://localhost:8088/items`
