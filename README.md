# EasyShop Capstone 3

## Overview
This project is an E-Commerce API and website built using Java and Spring Boot. It provides a robust backend for managing categories, products, shopping carts, user profiles, and orders. The API supports a variety of operations, making it suitable for modern e-commerce applications.

---

## Features
- **User Authentication**: Registration and login for users and admins.
- **Category Management**: CRUD operations for product categories.
- **Product Management**: Manage products with filters for category, price, and color.
- **Shopping Cart**: Add, update, and delete items in the cart.
- **User Profiles**: Update and retrieve user profiles.
- **Order Processing**: Place orders from the shopping cart.

---

## Bugs and Resolutions

### Bug 1: Incorrect Product Filtering
**Description**: Products were not being filtered correctly when multiple query parameters (e.g., `minPrice` and `color`) were provided.  

**Solution**
![Screenshot 2024-12-20 at 01 12 36](https://github.com/user-attachments/assets/8319b314-7fc7-4f33-8e7e-434c859089c2)



---

### Bug 2: Update Product Error 
**Description**: Product update was not working properly due to add the product instead of modify.  


---

## API Endpoints

### Authentication
- **Register**: `POST /register`
- **Login**: `POST /login`

### Categories (Phase 1)
- **Retrieve all categories**: `GET /categories`
- **Retrieve a specific category**: `GET /categories/{id}`
- **Create a category**: `POST /categories`
- **Update a category**: `PUT /categories/{id}`
- **Delete a category**: `DELETE /categories/{id}`

### Products (Phase 2)
- **Retrieve all products**: `GET /products`
- **Retrieve a specific product**: `GET /products/{id}`
- **Create a product**: `POST /products`
- **Update a product**: `PUT /products/{id}`
- **Delete a product**: `DELETE /products/{id}`
- **Filter products**: `GET /products?cat={categoryId}&minPrice={min}&maxPrice={max}&color={color}`

### Shopping Cart (Phase 3)
- **Retrieve cart**: `GET /cart`
- **Add product to cart**: `POST /cart/products/{id}`
- **Update product quantity**: `PUT /cart/products/{id}`
- **Clear cart**: `DELETE /cart`

### Profiles (Phase 4)
- **Retrieve profile**: `GET /profile`
- **Update profile**: `PUT /profile`

### Orders (Phase 5)
- **Place an order**: `POST /orders`

---

## Setup Instructions

### Prerequisites
- Java 17+
- MySQL
- Maven
- IDE (e.g., IntelliJ IDEA)

## Technologies Used
- Backend: Java, Spring Boot
- Database: MySQL
- Build Tool: Maven
- Frontend: Mini.css (for UI)
   
## Author
- Yiming Gao


## License
- This project is licensed under the MIT License. See the LICENSE file for details.
