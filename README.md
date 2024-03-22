
Inventory REST api's with product and shipping information written in Java using SpringBoot framework and Mysql DB as a storage.
1.SpringBoot application supported IDE
2.Mysql DB
3.Postman 

Download the build from  the link
Import the build in the IDE
Run InventoryApplication as Java application. Below tables will be automatically created.
    Products
    Shipping info

Check the Postman api's for CRUD operations and make requests. Check the data using GET api
/generate-bill api will return the invoice for the requested products.

Design and Architecture
1.API Layer (Controller):
 
     This layer will handle incoming HTTP requests from clients.
It will contain routes for various endpoints, such as:
Endpoints to calculate prices, apply discounts, and generate invoices.
CRUD endpoints for managing products and shipping rates.

2.Business Logic Layer:

     This layer will contain the business logic for calculating prices, applying discounts, generating invoices, and managing product and shipping rate data.
     It will interact with the database layer to retrieve and manipulate product and shipping rate information.
    Special offer rules, taxation rules, and shipping fee calculations will be implemented here.

3.Data Access Layer:

     This layer will handle interactions with the database.
     It will include database models, queries, and operations for CRUD functionalities related to  products and shipping rates.
     Object-relational mapping (ORM) library Hibernate is used to interact with the database.

4.Database:

       Products, shipping rates will be stored in the database.
    Tables are designed to efficiently store and retrieve data, ensuring data integrity and consistency.
   Since it's simple and relational data used Mysql for this application. 

5.Error Handling and Logging:
     Implemented error handling mechanisms at various layers to handle exceptions gracefully and provide meaningful error messages to clients.

Documentation:
   README file will contain information about how to run the program, design and architecture overview, API endpoints, and any other relevant details.
Postman collection will provide a collection of APIs with sample requests and responses for testing and demonstration purposes.

To do tasks:
1.For User Interaction 
  Can store user credentials (password in encrypted format) & Account details with orders, payment informations like saved cards, Favourite products
  Web page to show all the inventory information and place order
  Cart functionality & check out features
  Error handling in the webpage itself for out of stock products 
  Schedulers send notifications  when the previously visited out of stock item is in stock.

2.For security
   User login verification.
   MFA verification.
   JWT(Json Web Token) for session handling and captcha based verification during signin to avoid DOS attacks.
   Threshold handling for multiple requests & Locking mechanism for a specific time after threshold / multiple invalid attempts.
   Logging functionality can be included to record important events and debug information.
   Unit tests.

3.Scalability
  To handle multiple user requests can use a Caching mechanisam based on the load to store some details. After the data updated in DB, cache data will be repopulated for that particular data.
  Vertical scaling to add new DB machines & App servers to support huge volume of data
  Load balancer to route the requests.For example using user id / session id

4.Admin handling
  To add / update / deelete inventory details  seperate admin console & access permissions.
  To block / unblock user.
  
  

