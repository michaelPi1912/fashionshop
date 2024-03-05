package com.huuphucdang.fashionshop.service;

import com.huuphucdang.fashionshop.model.entity.Cart;
import com.huuphucdang.fashionshop.model.entity.CartItem;
import com.huuphucdang.fashionshop.model.entity.Product;
import com.huuphucdang.fashionshop.model.payload.request.CartItemRequest;
import com.huuphucdang.fashionshop.repository.CartItemRepository;
import com.huuphucdang.fashionshop.repository.CartRepository;
import com.huuphucdang.fashionshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository itemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    public CartItem saveCartItem(CartItemRequest body) {

        Cart cart = cartRepository.findById(body.getCartId()).orElseThrow();
        Product product = productRepository.findById(body.getProductId()).orElseThrow();

        CartItem checkItem = itemRepository.findByCartIdNProductId(cart.getId(), product.getId());
        if(checkItem != null){
            int qty = checkItem.getQuantity() + body.getQuantity();
            checkItem.setQuantity(qty);
            return itemRepository.save(checkItem);
        }
        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(body.getQuantity());

        return itemRepository.save(item);
    }

    public CartItem updateCartItem(CartItemRequest body, UUID id) {
        CartItem item = itemRepository.findById(id).orElseThrow();

        item.setQuantity(body.getQuantity());

        return itemRepository.save(item);
    }

    public void deleteCartItem(UUID id) {
        itemRepository.deleteById(id);
    }

    public List<CartItem> getAllItemByCartId(UUID cartId) {
        List<CartItem> itemList = itemRepository.findAllByCartId(cartId);
        return itemList;
    }
}
