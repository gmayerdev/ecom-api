# ecom-api
Simple E-commerce Application with a price surge mechanism


**Use Cases**
- Ability to have users visualize items online via HTTP-accessible API
- Utilize a price surge mechanism in order to raise prices on items that are visualized more than 10 times in the last hour


**Solution Design**

Assumptions:
- Item API is accessible and there is no authentication required
- When getting an item then create an Item View record, then retrieve the total count of Item Views, finally
if the count of Item Views is equal or more than 10 recalculate the Item price by increasing it by 10%.
- When a purchasing an item Spring Security must identify if the User is authenticated, if not then ??????


ToDo's:
- Need to create a Schedule Job to delete the Item Views older than an hour, consider running it every hour
- Need to implement pagination for retrieving all of the items


**Technical Specifications**
- Java 11
- Spring Boot 2.6.1
- Gradle
- H2 in memory Database


**How To Run**
1. Clone repo
2. Open a terminal and navigate to the folder
3. Build application using Gradle by running `gradle clean build`
4. Run application using Gradle by running `gradle bootRun`


**API**

### GET: All Items
Request: /items

Response:
`[
  {
    "itemId": "830956f1-389a-4860-bcae-0fa78a60480b",
    "itemName": "Basketball",
    "itemDescription": "An official NBA Basketball",
    "itemPrice": 40.0
  },
  {
    "itemId": "8ee4f1a4-b0b9-409f-8c71-caaae5bd8b37",
    "itemName": "Football",
    "itemDescription": "An official World Cup Football",
    "itemPrice": 100.0
  },
]`

Example:
`curl --request GET \
  --url http://localhost:8088/items`


### GET: Item by Id
Request: /items/{id}

Response:
  `{
    "itemId": "8ee4f1a4-b0b9-409f-8c71-caaae5bd8b37",
    "itemName": "Football",
    "itemDescription": "An official World Cup Football",
    "itemPrice": 100.0
  }`

Example:
`curl --request GET \
  --url http://localhost:8088/items/3df5e84b-bc55-4afc-a93f-09bbb855b821`

### POST: Create an Item
Request: /items

Body:
  `{
	"itemName":"Football",
	"itemDescription":"An official World Cup Football",
	"itemPrice":"100.00"
  }`

Response:
  `{
    "itemId": "8ee4f1a4-b0b9-409f-8c71-caaae5bd8b37",
    "itemName": "Football",
    "itemDescription": "An official World Cup Football",
    "itemPrice": 100.0
  }`

Example:
`curl --request POST \
  --url http://localhost:8088/items \
  --header 'Content-Type: application/json' \
  --data '{
	"itemName":"Football",
	"itemDescription":"An official Europe Leage Football",
	"itemPrice":"30.00"
}'`
