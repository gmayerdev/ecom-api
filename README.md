# ECOM API
Simple E-commerce Application with a price surge mechanism


### Use Cases
- Ability to have users visualize items online via HTTP-accessible API
- Restrict non authenticated users from buying an item
- Utilize a price surge mechanism in order to raise prices on items that are visualized more than 10 times in the last hour


### Solution Design


**Assumptions:**
- Unauthenticated users and authenticated users viewing an item count against the price surge calculation.
- The same user viewing the same item multiple times count against the price surge calculation.


**Design**

![Diagram](https://user-images.githubusercontent.com/57781585/144940937-483e7a56-4145-4504-869c-1b0b4b36410c.png)
- Spring Boot RESTFul API
  - Resource layer
  - Service layer
  - Repository layer interfacing with a H2 in memory database
- Spring Security with JWT authentication
  - Custom Authentication Provider
  - JWT filters to intercept authentication and generate/validate token
    - OBS: JSon Web Token along with Spring Security was used as the authentication/authorization mechanism as it allows the application to scale if needed as its a Stateless form of authentication (token holds the information about the user).
- Entities and relationship:
  - Item has 0 or many ItemViews
  - ItemView belongs to one Item, only exists if the Item exists
  - User

**User Flow**
1) User (Unauthenticated or Authenticated) views a specific item, system then finds item, after that the system creates an Item View record in the database, then the system recalculates the item price based on the amount of Item Views found in the last hour. If the amount is greater or equal to 10 then the price of the item is recalculated adding an additional 10% increase. The system then returns the Item.

2) User (Unauthenticated or Authenticated) views all items, system returns all items and performs recalculation if a certain Item has more than 10 Item Views (same condition as #1).

3) User (Unauthenticated) attempts to buy an Item, the system then intercepts the request and blocks the User as there is no token present returning a 401 Unauthorized.

4) User (Unauthenticated) performs login, system intercepts the request and validates the username and password, if invalid returns a 401 Unauthorized, if valid returns 200 Ok with the Header response containin the JWT Token.

5) User (Authenticated) buys an Item, the system then intercepts the request and validates the JWT Token, if invalid the system throws a bad request returning a 500, if valid the systems performs the purchase returning a 201 Created.


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
5. Use API curl requests in the terminal (or Postman) to test application.

<em>*As the application is using an in memory database stored within the application folder, do not run tests while the application is running or else you will see an exception thrown.</em>


### Testing the price surge mechanism
1. Use below API to create an item
2. From the create item response get the generated id of the Item
3. Use the below API to get an item by id, run this for atleast 10 times
4. Once the 10th time is reached the Item price will return with a 10% increase, and will stay the same price increase until within the last hour there are below 10 views. If you want to test this within a minute then open the cloned project folder, find and open the ItemViewService class, change the constant VIEW_COUNT_MIN_THRESHOLD to 1. Then run gradle clean build, and then gradle bootRun.


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

---

**GET: Login**
Request: /login

<em>*There is a User created upon application startup User:test@ecomapi.com with Password:12345, below curl contains the Base64 of this User as well as the verbose parameter so that the Response Headers are printed in the terminal</em>

Response:
```
{
  "username": "test@ecomapi.com"
}
```
<em>*Authorization JWT token gets added to the Headers to be used by requests</em>

Example:
```
curl --verbose --request GET \
  --url http://localhost:8088/login \
  --header 'Authorization: Basic dGVzdEBlY29tYXBpLmNvbToxMjM0NQ=='
```

---

**POST: Buy an Item**
Request: /items/buy
Required Headers: Authorization obtained from Login
Body:
```
{
 "itemId":"8ee4f1a4-b0b9-409f-8c71-caaae5bd8b37",
 "itemQuantity":"2",
 "itemPrice":"80.00"
}
```

Response:
```
{
 "itemId": "8ee4f1a4-b0b9-409f-8c71-caaae5bd8b37",
 "itemQuantity": 2,
 "itemPrice": 80.0,
 "purchaseId": "e1449de8-6f94-47cf-9343-06312b97c0b0"
}
```

Example:
```
curl --request POST \
  --url http://localhost:8088/items/buy \
  --header 'Authorization: eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJFQ09NLUFQSSIsInN1YiI6IkpXVCBUb2tlbiIsInVzZXJuYW1lIjoidGVzdEBlY29tYXBpLmNvbSIsImlhdCI6MTYzODgzMzkzMSwiZXhwIjoxNjM4ODM0ODMxfQ.fVUfc59xQp57JoJMuB7XVF-s6pqKSoxnTXTNbK3BToyUtwBEM1UI_dDs7K9fsQaHsO0S1GmEaQq-HhSmuLeVPw' \
  --header 'Content-Type: application/json' \
  --data '{
	"itemId":"8ee4f1a4-b0b9-409f-8c71-caaae5bd8b37",
	"itemQuantity":"2",
	"itemPrice":"80.00"
}'
```


