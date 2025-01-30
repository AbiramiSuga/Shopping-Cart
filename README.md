# Shopping Cart Application  

## Overview  
This is a **Spring Boot**-based Shopping Cart application that allows users to browse products, add them to a cart, and place orders. The application is containerized using **Docker** for easy deployment and scalability.  

## Features  
- **Product Management**: View available products.  
- **Cart Management**: Add and remove products from the cart.  
- **Order Processing**: Place an order and send details via Kafka.  
- **Logging**: Uses **Logback** for structured and efficient logging.  
- **Dockerized**: The application runs in a Docker container (excluding Kafka).
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

### **Build and Run the Application**  
```sh
# Build the Docker image
docker build -t shopping-cart-app .

# Run the PostgreSQL locally
or
# Create a container and run
docker run --name shopping-cart-db -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=password -e POSTGRES_DB=shopping_cart -p 5432:5432 -d postgres

# Run the application container
docker run --name shopping-cart-app --link shopping-cart-db -p 8083:8083 shopping-cart-app
```

### **Verify Running Containers**  
```sh
docker ps
```

### **Access the Application**  
- **API Base URL**: `http://localhost:8083/`  
- **PostgreSQL DB**: Connect using `docker exec -it shopping-cart-db psql -U postgres -d shopping_cart`

---


---





