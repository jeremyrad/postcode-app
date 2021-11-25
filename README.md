# postcode-app
Spring Boot REST Api Demo

A simple REST API in spring boot that accepts a list of Suburb names and Postcodes in the HTTP request body, and persist the data in an H2 in memory database.

GET /api/v1/postcode/{postcode} - Retrieve a specific postcode and associated suburbs.

POST /api/v1/postcode - Upload a list of postcodes and the suburb names.

GET /api/v1/postcode/from/{postcode}/to/{postcode} - Retrive a list of suburb names that are associated to the post code range, sorted alphabetically as well as the total number of characters of all names combined.

DELETE /api/v1/postcode/{postcode} - Remove a postcode

DELETE /api/v1/postcode/{postcode}/suburb/{suburb} - Remove a postcode suburb by name

Constraints
Postcodes are 4 digit codes range from 0000 to 9999.
Suburbs are Strings that can have a maximum length of 50 characters.
 
Working version of the app on AWS

http://postcodes-env.eba-mhyrqhin.af-south-1.elasticbeanstalk.com/swagger-ui.html#/
