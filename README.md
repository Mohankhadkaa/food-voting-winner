# Food Voting Winner App

A Spring Boot web application for voting on different food items. Users can:

- Browse food items
- Vote with 1 to 5 ratings
- Add new food items
- Delete food items
- View ranked results
- View the final Winner Food

## Tech Stack

- Java 21
- Spring Boot 3.4.4
- Spring MVC + Thymeleaf
- Spring Data JPA
- H2 in-memory database
- Maven
- Docker + Render deployment

## Main Pages

- `/` - Home page and voting cards
- `/results` - Ranked voting results
- `/winner` - Winner Food page
- `/add` - Add Food form
- `/h2-console` - H2 database console

H2 JDBC URL:

```text
jdbc:h2:mem:foodvotingwinner
```

## Run Locally in IntelliJ

1. Open IntelliJ IDEA.
2. Click **File > Open**.
3. Select this project folder.
4. Wait until Maven finishes loading.
5. Open `src/main/java/com/mohan/foodvotingwinner/FoodVotingWinnerApplication.java`.
6. Click the green Run button.
7. Open this URL in browser:

```text
http://localhost:8080
```

## Run Locally from Terminal

```bash
mvn spring-boot:run
```

## Build

```bash
mvn clean package
```

## Docker

```bash
docker build -t food-voting-winner .
docker run -p 8080:8080 food-voting-winner
```

## Deploy to Render

1. Push this project to GitHub.
2. Go to Render Dashboard.
3. Click **New +**.
4. Choose **Web Service**.
5. Connect your GitHub repository.
6. Select Docker runtime if needed.
7. Deploy.

## API Endpoints

- `GET /api/foods`
- `POST /api/foods`
- `DELETE /api/foods/{id}`
- `POST /api/votes`
- `GET /api/results`
- `GET /api/winner`
