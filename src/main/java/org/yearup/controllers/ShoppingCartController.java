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

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("cart")
@CrossOrigin
public class ShoppingCartController {

    private final ShoppingCartDao shoppingCartDao;
    private final UserDao userDao;
    private final ProductDao productDao;

    @Autowired
    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    // a helper method
    private int getUserIdFromPrincipal(Principal principal) {
        String username = principal.getName();
        User user = userDao.getByUserName(username);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        }
        System.out.println("User ID: " + user.getId());
        return user.getId();
    }

    @GetMapping
    public ShoppingCart getCart(Principal principal) {

        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = getUserIdFromPrincipal(principal);

            if (user == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
            }
            return shoppingCartDao.getByUserId(userId);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving cart.");

        }
    }

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

    // https://localhost:8080/cart/products/15 (15 is the productId to be updated)
    @PutMapping("/product/{productId}")
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
