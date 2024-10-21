# Basic Java Web Server Project

<img src="docs/img/java-img.jpg" width="100%">

This is a basic web server project built with **native Java**, utilizing only the Java standard library without any external frameworks. It demonstrates fundamental concepts such as routing, handling different HTTP methods, and processing URL parameters and query strings. The goal of this project is to serve as a reference for anyone interested in understanding how a web service can be implemented using just Java.

## Features

- **Basic Routing**: Set up routes for different endpoints.
- **Route with Parameters**: Handle dynamic routes with URL parameters.
- **Query Parameters**: Extract and process query strings from the URL.
- **HTTP Methods**: Support for common HTTP methods: `GET`, `POST`, `PATCH`, `DELETE`.

## Getting Started

### Prerequisites

Ensure you have **Java Development Kit (JDK)** installed on your machine.

- [Download JDK](https://www.oracle.com/java/technologies/downloads/) (or the version you prefer)

### Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/FarrelAD/Basic-Web-Server-Java.git
    ```
2. Navigate into the project directory:
    ```bash
    cd Basic-Web-Server-Java
    ```

### Running the Server

1. Compile Java program first:
    ```bash
    javac -d bin src/Main.java src/controller/*.java src/model/*.java src/utils/*.java
    ```
2. Run compiled program
    ```bash
    java -cp bin Main
    ```

The server will be running at http://localhost:8000


## Endpoints
Here are the key endpoints that you can interact with:

```
GET / : Returns a simple welcome message and provide simple form to handle request input
GET /users : Fetch all users
GET /users/:id : Fetch a data by their ID
POST /users : Add a new user
PATCH /users/:id : Update user information
DELETE /users/:id : Delete a user by their ID
GET /search?name=&job= : Retrieves data based on user input from a search form, filtering results by name and job
```

## Query Parameters
Example: /search?name=John&job=data+analyst
Query parameters can be extracted and used in request processing.

## HTTP Methods
The service supports the following HTTP methods:

```
GET: Retrieve data.
POST: Submit new data.
PATCH: Update partial existing data.
DELETE: Remove data.
```

## Code Structure
The project is simple and has the following structure:

```bash
/root
├───bin  # compiled Java file
│   ├─── /controller
│   ├─── /model
│   └─── /utils
├───lib
└───src
    ├─── /controller    # A handler for any request
    ├─── /model         # A temporary data user blueprint
    ├─── /utils         # Some helper function
    └─── Main.java      # Main server logic
```

## License
This project is licensed under the MIT License.

## Acknowledgments
This project is intended to help others learn the basics of building a web service with Java native ☕.