package com.huuphucdang.fashionshop.service;

import com.huuphucdang.fashionshop.model.entity.Cart;
import com.huuphucdang.fashionshop.model.entity.CartItem;
import com.huuphucdang.fashionshop.model.entity.User;
import com.huuphucdang.fashionshop.model.payload.request.CartRequest;
import com.huuphucdang.fashionshop.repository.CartItemRepository;
import com.huuphucdang.fashionshop.repository.CartRepository;
import com.huuphucdang.fashionshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    public Cart saveCart(User user) {
        cartRepository.deleteAllByUser(user.getId());
        System.out.println();
        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    public Cart getCartByUser(User user) {
        List<Cart>  cartList= cartRepository.findCartByUser(user.getId());
        return cartList.getLast();
    }

    public void deleteCart(UUID id) {
        cartRepository.deleteById(id);
    }
}
