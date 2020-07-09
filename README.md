# grocer

Simple demo application to simulate workflows of a grocer and their consumers using Spring Boot Web Flux and Reactive Programming.

Objective:
Implement following 4 REST API end points and necessary back-end implementation:

1)  Register a consumer (POST call to "/mystore/register/consumer")
2)  Register a grocer with 5 items (POST call to "/mystore/register/gorcer")
3)  Return grocers for the given location (GET call to "/mystore/stocklist/location")
4)  Return grocers selling specific item (GET call to "/mystore/stocklist/item")

Error 404 for GET calls and 500 for POST calls.

Model:
We use 2 entities - Consumer and Grocer. 

Database:
We use embedded MongoDB with 2 collections, one each for the entity. Corresponding 2 Reactive Mongo Repositories are used which are utilized by the Service level. The service objects implement the business logic.

API End Points:
Two registration APIs are exposed via RegistrationController while two get calls are exposed via StockListController.

Why Choice of Reactive Framework i.e. Mono and Flux?
Primarily reactive Spring Boot is finding utility so as essentially 'producer' does not produce if 'consumer' is underwater. If there are a lot of grocers who sell a particular item, but if the client/browser is slow; we 'read' from DB slowly. All that is done by the framework essentially abstracted out. A prime example is REST API endpoints return Mono and Flux and in Postman you see rendering without any issues as WebFlux comes into the play. The same handling is done between MongoDB to Spring Boot App layer as we use Reactive MongoDB drivers when you would have actual persistence (rather than in-memory DB in this case). 

Testing:
A core part of testing involves creating a grocery distribution of 5 stores among 3 cities. These stores sell 5 of 7 items so we have some non-trivial cases for checking that we return exactly those stores which sell the specific item. MongoDB provides a nifty 'in' operator to return all Grocer documents which has the specified item in the 'itemOnSale' list of that store. All this deployment is elaborately described and encapsulated in the class com.neosemantix.grocer.SampleGroceryDistribution under test/java folder. It also has some fun code using 'bitwise' operations to determine what each store sells.

How to run?
You can import the maven project in Eclipse or your IDE of choice and from the POM file you will get the project. If it is cleanly build, you can run the jar as a standalong application.

Maven tests would run tests in 2 classes - GroceryLocationTest and RegistrationTest. The RestApiTest is more of a functional test than a unit test. This being a small application, instead of mocking for API tests; you would run the application at "localhost:8080" and the API calls will be made against that when you invoke this test.
