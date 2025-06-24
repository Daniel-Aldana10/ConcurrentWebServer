# Concurrent Web Proxy Server

This is a multithreaded web proxy server written in Java. It receives a remote URL (e.g., https://www.github.com), fetches the content (HTML, images, text, etc.), and returns it to the client as a valid HTTP response. The server supports concurrent client handling using a thread pool, cookie management, and CORS. It also includes advanced concurrent and unit testing.

---

## Getting Started
These instructions will help you get a copy of the project running on your local machine for development and testing purposes.

### Prerequisites
You will need the following installed:
- Java 17+
- Maven 3.8+
- Git (optional, for cloning)

---

## Installing
Clone the repository:
```sh
git clone <YOUR_REPO_URL>
cd concurrentserver
```
Build the project using Maven:
```sh
mvn clean install
```

---

## Running the Application
You can run the server with:
```sh
java -cp target/classes org.example.Server
```
This will start the server on port 8080. It will listen for HTTP POST requests containing a URL as the body. It will fetch and return the content from that URL (HTML, image, etc.).

**Example using curl:**
```sh
curl -X POST http://localhost:8080 -d "https://www.github.com"
```
The server will:
- Download the content from the given URL.
- Return the content to the client as a valid HTTP response.
- Manage cookies for user sessions.
- Add CORS headers for browser compatibility.

---

## Running the Tests
Run the automated tests with Maven:
```sh
mvn test
```

### Unit and Integration Tests
Tests are written using JUnit 5 and simulate concurrent client behavior to verify the server's response under multithreaded conditions. There are also unit tests for cookie management, HTTP response building, and content fetching.

**Example unit test:**
```java
@Test
void testParseCookies() {
    String header = "userId=abc123; session=xyz";
    Map<String, String> cookies = CookieManager.parseCookies(header);
    assertEquals("abc123", cookies.get("userId"));
}
```

---

## Deployment
To build a standalone JAR:
```sh
mvn package
```
Then run using:
```sh
java -cp target/concurrentserver-1.0-SNAPSHOT.jar org.example.Server
```

---

## Design
- **Server**: Main class that starts the server and handles connections using a thread pool.
- **CookieManager**: Utility for parsing and building HTTP cookies.
- **ContentFetcher**: Utility to download content from a given URL.
- **HttpResponseBuilder**: Utility to build HTTP responses.
- **ConcurrentTest**: Simulates multiple concurrent clients and measures server performance.
---

## Test Report
- **Files Tested**
  - Server.java
  - CookieManager.java
  - ContentFetcher.java
  - HttpResponseBuilder.java
- **Summary**
  - All core functionalities are covered by unit and integration tests.

---

## Javadoc
Generate documentation with:
```sh
mvn javadoc:javadoc
```
Docs will be available at: `target/site/apidocs/index.html`

---

## Built With
- Java 17
- Maven 
- JUnit 5

---


## Authors
* **Daniel Aldana** - [GitHub](https://github.com/Daniel-Aldana10)

---

## License
This project is licensed under the MIT License – see the LICENSE file for details. 