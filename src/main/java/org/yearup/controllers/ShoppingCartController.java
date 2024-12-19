package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.security.Principal;

// convert this class to a REST controller
@RestController
// only logged in users should have access to these actions
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("cart")
public class ShoppingCartController {
    // a shopping cart requires
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;
    private ProductDao productDao;

    @Autowired
    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }


    // each method in this controller requires a Principal object as a parameter
    private int getUserIdFromPrincipal(Principal principal) {
        String username = principal.getName();
        User user = userDao.getByUserName(username);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        }
        return user.getId();
    }


    @GetMapping
    public ShoppingCart getCart(Principal principal) {
        try {
            // get the currently logged-in username
            // String userName = principal.getName();
            // find database user by userId
            // User user = userDao.getByUserName(userName);
            //  int userId = user.getId();
            int userId = getUserIdFromPrincipal(principal);

//            if (user == null) {
//                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
//            }

            // use the shoppingCartDao to get all items in the cart and return the cart
            return shoppingCartDao.getByUserId(userId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }


    // add a POST method to add a product to the cart - the url should be
    // @PostMapping("/product/{productId}")
    @PostMapping("/product/{productId}")

    // https://localhost:8080/cart/products/15 (15 is the productId to be added
    public void addProductToCart(@PathVariable("productId") int productId, Principal principal) {
        try {
            int userId = getUserIdFromPrincipal(principal);
            shoppingCartDao.addProductToCart(userId, productId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not add product to cart.");
        }
    }

    // add a PUT method to update an existing product in the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be updated)
    @PutMapping("/product/{productId}")
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated
    public void updateProductInCart(@PathVariable("productId") int productId,
                                    @RequestBody ShoppingCartItem updatedItem,
                                    Principal principal) {
        try {
            int userId = getUserIdFromPrincipal(principal);

            if (productDao.getById(productId) == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found.");
            }
            if (updatedItem.getQuantity() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be greater then 0.");
            }
            shoppingCartDao.updateProductQuantity(userId, productId, updatedItem.getQuantity());

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not update in cart.");
        }
    }

    // add a DELETE method to clear all products from the current users cart
    // https://localhost:8080/cart

    @DeleteMapping
    public void clearCart(Principal principal) {
        try {
            int userId = getUserIdFromPrincipal(principal);
            shoppingCartDao.clearCart(userId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not clear the cart.");
        }
    }
}
