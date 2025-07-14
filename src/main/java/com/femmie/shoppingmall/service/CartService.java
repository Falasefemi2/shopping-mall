package com.femmie.shoppingmall.service;

import com.femmie.shoppingmall.model.AppUser;
import com.femmie.shoppingmall.model.Cart;
import com.femmie.shoppingmall.model.CartItem;
import com.femmie.shoppingmall.model.Product;
import com.femmie.shoppingmall.repository.CartRepository;
import com.femmie.shoppingmall.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public Cart addToCart(Long userId, Long productId, int quantity) {
        Optional<Cart> optionalCart = cartRepository.findById(userId);
        Cart cart =  optionalCart.orElseGet(() -> {
            Cart newCart = new Cart();
            AppUser user = new AppUser();
            user.setId(userId);
            newCart.setUser(user);
            return newCart;
        });

        Optional<Product> optionalProduct = productRepository.findById(productId);
        if(optionalProduct.isPresent() && optionalProduct.get().getStock() >= quantity) {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(optionalProduct.get());
            cartItem.setQuantity(quantity);
            cart.getCartItems().add(cartItem);
            return cartRepository.save(cart);
        }
        throw new RuntimeException("Product not found or insufficient stock");
    }

    public Cart getCart(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }
}
