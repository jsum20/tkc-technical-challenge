# TKC Technical Challenge - Interstellar Route Planner

This is my solution to the technical challenge. It is a Springboot application, written in Java
that calculates the costs of interstellar journeys using the HSTC network including route planning
between different gates.

## Table of Contents
1. [Prerequisites](#prerequisites)
2. [Running the Application Locally](#running-the-application-locally)

---

## Prerequisites

Before running the application, make sure you have the following installed:

- **JDK 17**: [Download JDK 17](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
- **Maven**: [Download Maven](https://maven.apache.org/download.cgi)
- **PostgreSQL**: [Download PostgreSQL](https://www.postgresql.org/download/)

## Running the Application Locally

### 1. Clone the Repository
```bash
git clone git@github.com:jsum20/tkc-technical-challenge.git
```

### 2. Set up the database
2.1 Start Postgres and create a db `createdb tkc_db`<br>
2.2 Update the connection details in the application.properties file. Currently there
should be a `applications.example.properties` file. Use the command `mv src/main/resources/application.example.properties src/main/resources/application.properties`
in the root directory and update the fields.

### 3. Build the Application
Run `mvn clean package` to build the JAR files.

### 4. Run the Application
Run `java -jar target/tkc-api-1.0.0.jar`
The application will then start on port 8080 and can be tested locally with postman or cURL.




