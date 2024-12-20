# EasyShop - Shopping Cart Management System

## Overview
EasyShop is a robust shopping cart management system designed to enhance the shopping experience on the EasyShop website. It features user authentication and role-based access control, ensuring that only authenticated users can manage their carts while administrators can efficiently add, update, and manage categories and products. The application leverages MySQL for data persistence and is designed to be modular, maintainable, and extensible.
---
## Progress In Capstone
Some of phase 3, wasn't able to test out my controller or dao for shopping cart. 

## Features

- **User Authentication**: Only logged-in users can access and manage their shopping carts.
- **Error Handling**: Detailed error messages for better debugging and user feedback.
- **Create a Category or Product**:Admin can create new category or product.
- **Update Categories or Products**:Admin can update an existing category or product.
- **Delete Categories or Products**:Admin can delete an existing categories or products.
- **Price Range Search Feature**:The user can search products by a min and max price range.
---
## Project Layout
### Database
- **Schema File**:
    - The `create_database.sql` file contains the database schema used by the application. It defines the structure of tables, relationships, and constraints.
    - Includes:
        - **User Table**: Stores user information such as usernames, passwords, and roles.
        - **Product Table**: Contains details about products such as name, description, price, and stock levels.
        - **Shopping Cart Table**: Maps users to their selected products with quantities.
        - **Category Table**: Organizes products into categories for better management.

### Controllers
- **Purpose**:
    - Controllers are responsible for handling HTTP requests and mapping them to appropriate service or DAO methods.
- **Key Files**:
    - `ShoppingCartController.java`: Manages shopping cart operations such as adding items, viewing the cart, and updating quantities.
    - `CategoryController.java`: Provides endpoints for managing product categories, including retrieving, adding, and updating categories.
    - `ProductController.java`: Handles product-related operations such as fetching product details, adding new products, and updating product data.
  - **Structure**: Implements RESTful APIs with appropriate HTTP methods (`GET`, `POST`, `PUT`, `DELETE`).

### Data
- **Purpose**:
    - The `data` package contains DAO (Data Access Object) classes that abstract the interaction with the MySQL database.
- **Key Files**:
    - **MySQL DAO Classes**:
        - `MySqlShoppingCartDao.java`: Handles database queries for shopping cart operations.
        - `MySqlProductDao.java`: Manages product-related database queries.
        - `MySqlCategoryDao.java`: Handles category-related database queries.
        - `MySqlUserDao.java`: Retrieves and manages user-related data.
    - **DAO Interfaces**:
        - `ShoppingCartDao.java`: Defines the contract for shopping cart operations.
        - `ProductDao.java`: Outlines methods for product data access.
        - `CategoryDao.java`: Specifies methods for managing category data.
        - `UserDao.java`: Specifies user data operations.

### Models
- **Purpose**:
    - The `models` package contains plain Java objects representing the application's core entities.
- **Key Classes**:
    - `Product.java`: Represents a product with attributes such as `name`, `price`, `description`, and `stock`.
    - `ShoppingCart.java`: Models the user's shopping cart as a collection of `ShoppingCartItem` objects.
    - `ShoppingCartItem.java`: Represents an individual item in the shopping cart, including the product and its quantity.
    - `Category.java`: Defines the structure for product categories with fields such as `name` and `description`.
    - `User.java`: Defines user-related data such as `username`, `password`, and `role`.
- **Structure**:This structure ensures a clear separation of concerns and facilitates easy debugging, testing, and future enhancements. It reflects best practices for a modern Spring Boot application.
---
# Screenshots

### Categories
**Get All Categories**
![getallcategories](images%2Fgetallcategories.jpg)

**Get Category By ID**
![getcategorybyid](images%2Fgetcategorybyid.jpg)

**Create New Category**
![postcategory](images%2Fpostcategory.jpg)

**Update Category**
![putcategory](images%2Fputcategory.jpg)

**Delete Category**
- Before
![beforeDeletingCategories](images%2FbeforeDeletingCategories.jpg)
- After
![afterDeletingCategories](images%2FafterDeletingCategories.jpg)

### Products
**Get All Products**
![getallproducts](images%2Fgetallproducts.jpg)

**Get Product By Id** 
![getproductbyid](images%2Fgetproductbyid.jpg)

**Create New Product**
![postproduct](images%2Fpostproduct.jpg)

**Update Product**
![putproduct](images%2FafterUpdatingProduct94.jpg)

**Delete Product**
![deletedproduct](images%2Fdeletedproduct94.jpg)

**Search Products By Price Range**
- Showing All Three Laptops
![searchbypricerangeinproducts](images%2Fsearchbypricerangeinproducts.jpg)
-  After Deleting The Two Cloned Laptops
![newsearchbypricerangeafterdeleting2laptops](images%2Fnewsearchbypricerangeafterdeleting2laptops.jpg)

### Website
**My Art and Craft Category**
![myart&craftcategory](images%2Fmyart%26craftcategory.jpg)

**Filtered Products By Category And Price Range**
![filtersearchbycategory&pricerange.jpg](images%2Ffiltersearchbycategory%26pricerange.jpg)
---
### Interesting Piece Of Code
I got to thinking that if there was already an item in the user's shopping cart, would an error occur? Then somewhere I read that it would and that I could fix it by using duplicate key query.
[Geeks for geeks on duplicate key errors](https://www.geeksforgeeks.org/how-to-handle-duplicate-key-violations-in-jdbc/)
[Using duplicate keys update](https://stackoverflow.com/questions/73557454/performatic-insert-into-database-to-catch-duplicate-key-error)
![duplicatekeyupdatequery](images%2Fduplicatekeyupdatequery.jpg)
So instead of throwing up an error, that query would kick in by updating the quantity column in that row by adding 1 to the current value (quantity = quantity + 1).
---

### What I Would Add To This Application
While I was creating the search list for my product in `MySqlProductDao.java`, I started adjusting it by adding a search by name of the product. Cause in my head most that would be what I would search something by first. So crating the front end portion, I would imagine a search bar. 
![additiontoproject](images%2Fadditiontoproject.jpg)

