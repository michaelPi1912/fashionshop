package com.huuphucdang.fashionshop.service;

import com.huuphucdang.fashionshop.model.entity.Order;
import com.huuphucdang.fashionshop.model.entity.Product;
import com.huuphucdang.fashionshop.model.entity.Role;
import com.huuphucdang.fashionshop.model.entity.User;
import com.huuphucdang.fashionshop.model.payload.request.ChangePasswordRequest;
import com.huuphucdang.fashionshop.model.payload.request.RegisterRequest;
import com.huuphucdang.fashionshop.model.payload.response.UsersResponse;
import com.huuphucdang.fashionshop.repository.ProductRepository;
import com.huuphucdang.fashionshop.repository.TokenRepository;
import com.huuphucdang.fashionshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final ProductRepository productRepository;
    public User changePassword(ChangePasswordRequest request, User user) {
        System.out.println(request.getNewPassword());
//        var user = (User)((UsernamePasswordAuthenticationToken) connectedUser).getCredentials();

        if(!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())){
            return new User();
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        return userRepository.save(user);
    }

    public void deleteUser(UUID id) {
        tokenRepository.deleteAllTokenByUser(id);
        userRepository.deleteById(id);
    }

    public UsersResponse findAllUser(int page, int size, String email, String name, int active, int role) {
        try{
            List<User> users;
            Specification<User> spec = Specification.where(null);

            if(!email.isEmpty()){
                spec = spec.and((root, query, cb) -> cb.like(root.get("email"), "%"+email+"%"));
            }

            if(active == 1){
                spec = spec.and((root, query, cb) -> cb.equal(root.get("isActive"), false));
            }
            if(active == 2){
                spec = spec.and((root, query, cb) -> cb.equal(root.get("isActive"), true));
            }
            if(role == 1){
                spec = spec.and((root, query, cb) -> cb.equal(root.get("role"), "ADMIN"));
            }
            if(role == 2){
                spec = spec.and((root, query, cb) -> cb.equal(root.get("role"), "MANAGER"));
            }
            if(role == 3){
                spec = spec.and((root, query, cb) -> cb.equal(root.get("role"), "USER"));
            }


            if(!name.isEmpty()){
                spec = spec.and((root, query, cb) -> cb.like(root.get("firstname"), "%"+name+"%")).or((root, query, cb) -> cb.like(root.get("lastname"), "%"+name+"%"));
            }
            Pageable paging = PageRequest.of(page, size);
            Page<User> pageUsers = userRepository.findAll(spec,paging);
            users = pageUsers.getContent();

            return UsersResponse.builder()
                    .users(users)
                    .currentPage(pageUsers.getNumber())
                    .totalItems(pageUsers.getTotalElements())
                    .totalPages(pageUsers.getTotalPages())
                    .build();
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    public void blockUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setActive(false);
        userRepository.save(user);
    }

    public void unBlockUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setActive(true);
        userRepository.save(user);
    }

    public Product addProduct(UUID userId, Product productRequest) {
        User user = userRepository.findById(userId).orElseThrow();
        Product product = productRepository.findById(productRequest.getId()).orElseThrow();
        user.getProducts().add(product);

        return productRepository.save(product);
    }

    public void deleteProduct(UUID userId, UUID productId) {
        productRepository.deleteProductWishList(userId, productId);
    }

    public Product checkWishList(User user, UUID id) {
        List<Product> products = productRepository.checkWishList(user.getId(),id);

        return products.isEmpty() ? new Product() : products.getFirst();
    }

    public User updateProfile(RegisterRequest body, User user) {
        user.setFirstname(body.getFirstname());
        user.setLastname(body.getLastname());
        user.setPhone(body.getPhone());
        user.setGender(body.getGender());


        return userRepository.save(user);
    }
}
