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
- Spring Boot RESTFul API adopting MVC
- Spring Security with JWT authentication
- Entities and relationship:
    -- Item has 0 or many ItemViews
    -- ItemView belongs to one Item, only exists if the Item exists

![Diagram](https://user-images.githubusercontent.com/57781585/144940937-483e7a56-4145-4504-869c-1b0b4b36410c.png)

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


