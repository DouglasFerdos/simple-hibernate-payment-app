# simple-hibernate-payment-app
A simple payment app using JPA interface, to make it as vendor independent as possible, using the Hibernate implementation of JPA and PostgreSQL for storing the persistence data.

#### Password hashing using Argon2 was made with the Bouncy Castle Provider v1.78.1.
For password hashing was used a 32bytes hash length, with salt of 16 bytes and pepper of 32 bytes.

*Considering that most database comprimises occurs through SQL injections and does not see the code that actually made the hash, adding a PEPPER in this case will add to the strength of password.

## The following steps can be used to execute the program yourself:

### Create a database with the name `hibernatepaymentapp` in PostgreSQL
1. This command can be used to login in the PostgreSQL db as superuser:
   ```
   psql -d postgres -U postgres
   ```
2. press `Enter`, type your password, the default one is `postgres`, and `Enter` again.
   
3. Now just use this code to create the needed database:
   ```
   CREATE DATABASE hibernatepaymentapp;
   ```
#### *** If you are using other user and password than `postgres` and `postgres`, update the PostgresPU in persistence.xml with your info.
## Don't forget to check if the maven dependencies are installed

### This project has four dependencies:

* PostgreSQL JDBC Driver: version `42.7.4` as can be seen in the pom.xml file. (Neccessary).

* Hibernate Core: version `6.6.0.Final` as can be seen in the pom.xml file. (Neccessary).

* Bouncy Castle Provider: version `6.6.0.Final` as can be seen in the pom.xml file. (Neccessary).

* JUnit 5 (Jupiter): version `1.78.1` as can be seen in the pom.xml file. (Only if you want to use the Junit tests).

#### You are ready to run the tests, that will create the necessary tables and check if everything is working properly.

# Business rules used in this project

* The account has the following data: `SSN/EIN`, `Full Name`, `Email`, `balance` and `password`.
* Both Users and Stores should have unique SSN/EIN, a email can have one User and one Store registered.
* User can tranfer money between Users and to Stores.
* Stores can only receive transferences.
