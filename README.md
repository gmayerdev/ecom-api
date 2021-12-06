# ECOM API
Simple E-commerce Application with a price surge mechanism


### Use Cases
- Ability to have users visualize items online via HTTP-accessible API
- Utilize a price surge mechanism in order to raise prices on items that are visualized more than 10 times in the last hour


### Solution Design

**Assumptions:**
- Item API is accessible and there is no authentication required
- When getting an item then create an Item View record, then retrieve the total count of Item Views, finally
if the count of Item Views is equal or more than 10 recalculate the Item price by increasing it by 10%.
- When purchasing an item authentication is enforced, the session will lasts for

**Design**
- Spring Boot RESTFul API, separating layers according to MVC pattern
- Spring Security with JWT authentication
- Entities and relationship
    -- Item has 0 or many ItemViews
    -- ItemView belongs to one Item, only exists if the Item exists
-

**Things I would like to improve:**
- Implement a scheduler to delete Item View records older than an hour, this would improve performance reducing the amount of records in the database
- Implement pagination mechanism so that the request of all items if paged, this would improve performance not retrieving all items from the database


### Technical Specifications
- Java 11
- Spring Boot 2.6.1
- Gradle
- H2 in memory Database


### How To Run
1. Clone repo
2. Open a terminal and navigate to the folder
3. Build application using Gradle by running `gradle clean build`
4. Run application using Gradle by running `gradle bootRun`


### API

**GET: All Items**
Request: /items

Response:
```
[{
 "itemId": "830956f1-389a-4860-bcae-0fa78a60480b",
 "itemName": "Basketball",
 "itemDescription": "An official NBA Basketball",
 "itemPrice": 40.00
},
{
 "itemId": "8ee4f1a4-b0b9-409f-8c71-caaae5bd8b37",
 "itemName": "Football",
 "itemDescription": "An official World Cup Football",
 "itemPrice": 100.00
}]
```

Example:
```
curl --request GET \
--url http://localhost:8088/items
```

---

**GET: Item by Id**
Request: /items/{id}

Response:
```
{
 "itemId": "830956f1-389a-4860-bcae-0fa78a60480b",
 "itemName": "Basketball",
 "itemDescription": "An official NBA Basketball",
 "itemPrice": 40.00
}
```

Example:
```
curl --request GET \
--url http://localhost:8088/items/830956f1-389a-4860-bcae-0fa78a60480b
```

---

**POST: Create an Item**
Request: /items
Body:
```
{
 "itemName":"Football",
 "itemDescription":"An official World Cup Football",
 "itemPrice":"100.00"
}
```

Response:
```
{
 "itemId": "8ee4f1a4-b0b9-409f-8c71-caaae5bd8b37",
 "itemName": "Football",
 "itemDescription": "An official World Cup Football",
 "itemPrice": 100.00
}
```

Example:
```
curl --request POST \
  --url http://localhost:8088/items \
  --header 'Content-Type: application/json' \
  --data '{
 	"itemName": "Football",
 	"itemDescription": "An official World Cup Football",
	"itemPrice":"100.00"
}'
```