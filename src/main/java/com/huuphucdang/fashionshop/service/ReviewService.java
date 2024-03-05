package com.huuphucdang.fashionshop.service;

import com.huuphucdang.fashionshop.model.entity.CartItem;
import com.huuphucdang.fashionshop.model.entity.OrderLine;
import com.huuphucdang.fashionshop.model.entity.User;
import com.huuphucdang.fashionshop.model.entity.UserReview;
import com.huuphucdang.fashionshop.model.payload.request.ReviewRequest;
import com.huuphucdang.fashionshop.repository.OrderLineRepository;
import com.huuphucdang.fashionshop.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderLineRepository orderLineRepository;
    public UserReview saveReview(User user, ReviewRequest body) {
        UserReview review = new UserReview();
        review.setComment(body.getComment());
        review.setUser(user);
        review.setRatingValue(body.getRating());
        OrderLine orderLine = orderLineRepository.findById(body.getOrderLineId()).orElseThrow();
        review.setOrderLine(orderLine);
        UserReview userReview = null;
        if(body.getParentId() != null){
            userReview = reviewRepository.findById(body.getParentId()).orElseThrow();
        }
        review.setParent(userReview);
        return reviewRepository.save(review);
    }

    public List<UserReview> getAllReviewByProduct(UUID productId) {
        List<UserReview> reviewList = reviewRepository.findAllByProductId(productId);
        return reviewList;
    }

    public void deleteReview(UUID id) {
        reviewRepository.deleteById(id);
    }

    public List<UserReview> getAllReviewByUser(User user) {
        List<UserReview> reviewList = reviewRepository.findAllByUserId(user.getId());
        return reviewList;
    }

    public UserReview updateReview(ReviewRequest body, UUID id) {

        UserReview review = reviewRepository.findById(id).orElseThrow();

        review.setComment(body.getComment());
        review.setRatingValue(body.getRating());

        return reviewRepository.save(review);
    }
}
