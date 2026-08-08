# Codev

**Codev** is a Java Swing-based code editor and quiz application built with **FlatLaf**, **RSyntaxTextArea**, **MySQL**, and the **QuizAPI**.

## Features

* Syntax highlighting
* Auto-save
* Create, delete, and rename files
* Open a native terminal
* Support for:

  * Java
  * Python
  * JavaScript
  * More to be added
* Dark and light themes
* Multiple color schemes, including:

  * Monokai
  * Eclipse
* Take quizzes using [QuizAPI](https://quizapi.io/)
* Store usernames, passwords, and quiz scores in MySQL

## Requirements

Before running the application, make sure you have:

* Java JDK installed
* MySQL installed and running
* A QuizAPI account and API token
* A Java-compatible IDE such as IntelliJ IDEA or Eclipse

---

## Quiz API Setup

Codev uses [QuizAPI](https://quizapi.io/) to provide quiz questions.

### Get an API Token

1. Go to [QuizAPI](https://quizapi.io/).
2. Create an account or sign in.
3. Get your API token from your account dashboard.
4. Add the token to `config.properties` as described below.

---

## Database Setup

Codev uses **MySQL** to store user information and quiz scores.

Create a database and the required tables:

```sql
CREATE DATABASE {your_db};

USE {your_db};

CREATE TABLE users (
    id CHAR(36) NOT NULL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    gender ENUM('MALE', 'FEMALE') NOT NULL DEFAULT 'MALE',
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE scores (
    id CHAR(36) NOT NULL PRIMARY KEY,
    quiz_id VARCHAR(100) NOT NULL,
    user_id CHAR(36) NOT NULL,
    score INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_scores_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);
```

### Database Schema

#### `users`

| Column       | Type           | Constraints                |
| ------------ | -------------- | -------------------------- |
| `id`         | `CHAR(36)`     | Primary Key                |
| `username`   | `VARCHAR(100)` | Unique, Not Null           |
| `password`   | `VARCHAR(255)` | Not Null                   |
| `gender`     | `ENUM`         | `MALE`, `FEMALE`           |
| `first_name` | `VARCHAR(100)` | Nullable                   |
| `last_name`  | `VARCHAR(100)` | Nullable                   |
| `created_at` | `TIMESTAMP`    | Default: Current Timestamp |

#### `scores`

| Column       | Type           | Constraints                |
| ------------ | -------------- | -------------------------- |
| `id`         | `CHAR(36)`     | Primary Key                |
| `quiz_id`    | `VARCHAR(100)` | Not Null                   |
| `user_id`    | `CHAR(36)`     | Foreign Key → `users.id`   |
| `score`      | `INT`          | Default: `0`               |
| `created_at` | `TIMESTAMP`    | Default: Current Timestamp |


## Configuration

Create a `config.properties` file inside:

```text
src/main/resources/config.properties
```

Add the following configuration:

```properties
# API Configuration
api.token={your_api_token}

# Database Configuration
connection_string=jdbc:mysql://localhost:3306/{your_db}
db_username={your_username}
db_password={your_password}
```

### Example

```properties
api.token=your_api_token_here

connection_string=jdbc:mysql://localhost:3306/quiz_db
db_username=root
db_password=your_password
```

> **Important:** Never commit your real API token or database password to GitHub.


## Running the Application

1. Clone the repository.
2. Open the project in your preferred Java IDE.
3. Make sure MySQL is running.
4. Create the database using the SQL script above.
5. Create `config.properties` in `src/main/resources`.
6. Add your QuizAPI token and MySQL credentials.
7. Build and run the application.

---

## Technologies

* **Java Swing** — Desktop UI
* **FlatLaf** — Look and feel
* **RSyntaxTextArea** — Code editor and syntax highlighting
* **MySQL** — User and score storage
* **QuizAPI** — Quiz questions
