# Shopping Cart Application  

## Overview  
This is a **Spring Boot**-based Shopping Cart application that allows users to browse products, add them to a cart, and place orders. The application is containerized using **Docker** for easy deployment and scalability.  

## Features  
- **Product Management**: View available products.  
- **Cart Management**: Add and remove products from the cart.  
- **Order Processing**: Place an order and send details via Kafka.  
- **Logging**: Uses **Logback** for structured and efficient logging.  
- **Dockerized**: The application runs in Docker containers for both the Spring Boot app and PostgreSQL database(excluding Kafka).

---

## Docker Setup  

### 🔹 **Docker Images & Their Purpose**  

| Image Name            | Purpose |
|----------------------|---------|
| `shopping-cart-app`  | Runs the Spring Boot Shopping Cart application. |
| `shopping-cart-db`   | PostgreSQL database container storing application data. |

---

## **Exposed APIs**  

### **Product APIs**  

| Method | Endpoint | Description |
|--------|---------|-------------|
| `GET`  | `/products` | Fetch all available products. |
| `POST` | `/products` | Add a new product. |
| `GET`  | `/products/{id}` | Fetch product details by ID. |
| `PUT` | `/products/{id}/` | Update a product. |
| `DELETE` | `/products/{id}/` | Delete a product. |

Similar APIs for CART to add, remove products.

### **Order API**  

| Method | Endpoint | Description |
|--------|---------|-------------|
| `POST` | `/order/{cartId}` | Place an order (Sends details to Kafka for Inventory & Invoice processing). |

---

## **Installation & Setup**  

### **Clone the Repository**  
```sh
# Clone the repository
git clone https://github.com/AbiramiSuga/shopping-cart.git
cd shopping-cart
```

### **Switch to Docker Branch**  
```sh
git checkout docker_exp
```

### **Docker Compose Setup**  

Make sure you have Docker Compose installed. Then, follow these steps to run the application using Docker Compose.

1. **Build and Run the Containers**  
```sh
# Start all services (shopping-cart-app & shopping-cart-db)
docker-compose up --build
```

This will:
- Build the `shopping-cart-app` Docker image (if not already built).
- Start the PostgreSQL container (`shopping-cart-db`).
- Start the Spring Boot application container (`shopping-cart-app`).

2. **Verify Running Containers**  
```sh
docker ps
```

### **Access the Application**  
- **API Base URL**: `http://localhost:8083/`  
