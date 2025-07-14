# Shopping Mall API

This is a Spring Boot application that provides a RESTful API for a simple shopping mall.

## Table of Contents

- [Features](#features)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [API Endpoints](#api-endpoints)
  - [Authentication](#authentication)
  - [Products](#products)
  - [Cart](#cart)
  - [Payment](#payment)
- [Technologies Used](#technologies-used)

## Features

- User registration and authentication with JWT.
- Browse and manage products.
- Add products to a shopping cart.
- Checkout and pay with Stripe.

## Getting Started

### Prerequisites

- Java 24
- Maven
- MySQL

### Installation

1. **Clone the repository:**

   ```bash
   git clone https://github.com/Falasefemi2/shopping-mall.git
   ```

2. **Configure the database:**

   - Open `src/main/resources/application.properties` and update the following properties with your MySQL database credentials:

     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/shoppingmall_db
     spring.datasource.username=root
     spring.datasource.password=admin
     ```

3. **Configure Stripe:**

   - Sign up for a [Stripe](https://stripe.com/) account and get your API keys.
   - Open `src/main/resources/application.properties` and update the following property with your Stripe secret key:

     ```properties
     stripe.api.key=your-stripe-secret-key
     ```

4. **Run the application:**

   ```bash
   ./mvnw spring-boot:run
   ```

   The application will be running on `http://localhost:8080`.

## API Endpoints

### Authentication

- `POST /api/auth/register`: Register a new user.
- `POST /api/auth/login`: Login and get a JWT token.
- `POST /api/auth/refresh`: Refresh the JWT token.

### Products

- `GET /api/product`: Get all products.
- `GET /api/product/{id}`: Get a product by ID.
- `POST /api/product`: Create a new product.
- `PUT /api/product/{id}`: Update a product.
- `DELETE /api/product/{id}`: Delete a product.

### Cart

- `POST /api/cart/add`: Add a product to the cart.
- `GET /api/cart/{userId}`: Get the cart for a user.

### Payment

- `POST /api/payment/checkout/{cartId}`: Checkout and pay for the items in the cart.

## Technologies Used

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Security](https://spring.io/projects/spring-security)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [MySQL](https://www.mysql.com/)
- [Stripe](https://stripe.com/)
- [JWT](https://jwt.io/)
- [Maven](https://maven.apache.org/)
