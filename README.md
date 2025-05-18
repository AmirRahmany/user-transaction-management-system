# 🚀 TransMon - User Transaction Management System

**TransMon** is a lightweight, scalable system for managing user registrations and tracking financial transactions.  
Built with **Spring Boot**, **PostgreSQL**, and **Docker**, this project follows clean code practices and sets a foundation for real-world, production-grade backend services.

---

## 📋 Features

- ✅ User Registration & Management
- ✅ Transaction Creation (Deposit / Withdraw)
- ✅ View Transaction History per User
- ✅ Balance Calculation
- ✅ Suspicious Transaction Detection
- ✅ PostgreSQL Integration
- ✅ Dockerized Setup with `docker-compose`
- ✅ Unit & Integration Testing
- ✅ Clean Code & TDD Practices

---

## 🛠️ Tech Stack

- **Backend:** Java 17, Spring Boot
- **Database:** PostgreSQL (Product) - H2(Test)
- **Containerization:** Docker, Docker-Compose
- **Version Control:** Git, GitHub Flow
- **Testing:** JUnit 5, Mockito
- **Build Tool:** Maven
---

## Getting Started

The easiest way to get started with this project is using Docker Compose. Ensure you have [Docker](https://docs.docker.com/get-docker/) and [Docker Compose](https://docs.docker.com/compose/install/) installed on your system.

### Running the Application

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/AmirRahmany/user-transaction-management-system.git](https://github.com/AmirRahmany/user-transaction-management-system.git)
    cd user-transaction-management-system
    ```

2.  **Start the application using Docker Compose:**
    ```bash
    docker-compose up -d
    ```
    This command will build the necessary Docker image and start the application containers in detached mode.
### Accessing the Application

Once the containers are running, you can access the application at the following URL:

* **Backend API:** [Likely `http://localhost:8083`, but adjust if your `docker-compose.yml` specifies a different port for the backend service]

Refer to your application's documentation or API specifications for available endpoints and functionalities.
### Accessing the Application

The API is running at `http://localhost:8083`.

## API Endpoints

Below are the available API endpoints for the User Transaction Management System. You can find more details and examples in the `Transaction Management.postman_collection.json` file.

### Authentication

* **`POST /api/auth/register`**: Registers a new user.

    * **Request Body:**

        ```json
        {
            "firstName": "John",
            "lastName": "Doe",
            "email": "john.doe@example.com",
            "password": "securePassword",
            "phoneNumber": "123-456-7890"
        }
        ```
* **`POST /api/auth/signin`**: Signs in an existing user.

    * **Request Body:**

        ```json
        {
            "username": "john.doe@example.com",
            "password": "securePassword"
        }
        ```

### User Management

* **`POST /api/user/activation`**: Activates a user account. Requires a valid Bearer token in the `Authorization` header.

    * **Request Body:**

        ```json
        {
            "username": "john.doe@example.com"
        }
        ```

### Account Management

* **`POST /api/account`**: Opens a new bank account. Requires a valid Bearer token in the `Authorization` header.

    * **Request Body:**

        ```json
        {
            "username": "john.doe@example.com",
            "balance": 100.00
        }
        ```
* **`POST /api/account/activation`**: Activates a bank account. Requires a valid Bearer token in the `Authorization` header.

    * **Request Body:**

        ```json
        {
            "accountNumber": "1234567890"
        }
        ```

### Transaction Management

* **`POST /api/transaction/deposit`**: Deposits funds into an account. Requires a valid Bearer token in the `Authorization` header.

    * **Request Body:**

        ```json
        {
            "amount": 100.00,
            "accountNumber": "1234567890",
            "description": "Sample deposit"
        }
        ```
* **`POST /api/transaction/withdraw`**: Withdraws funds from an account. Requires a valid Bearer token in the `Authorization` header.

    * **Request Body:**

        ```json
        {
            "funds": 50.00,
            "fromAccountNumber": "1234567890",
            "description": "Sample withdrawal"
        }
        ```
* **`GET /api/transaction/history/{accountNumber}`**: Retrieves the transaction history for a specific account. Requires a valid Bearer token in the `Authorization` header.

    * **Path Parameter:** `accountNumber` - The account number for which to retrieve the history.
