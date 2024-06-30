package com.huuphucdang.fashionshop.service;

import com.huuphucdang.fashionshop.model.entity.Product;
import com.huuphucdang.fashionshop.model.entity.User;
import com.huuphucdang.fashionshop.model.entity.UserReview;
import com.huuphucdang.fashionshop.model.payload.request.ReviewRequest;
import com.huuphucdang.fashionshop.model.payload.response.ReviewResponse;
import com.huuphucdang.fashionshop.repository.OrderLineRepository;
import com.huuphucdang.fashionshop.repository.ProductRepository;
import com.huuphucdang.fashionshop.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderLineRepository orderLineRepository;
    private final ProductRepository productRepository;

    public UserReview saveReview(User user, ReviewRequest body) {
        UserReview review = new UserReview();
        //comment
        review.setComment(body.getComment());
        //user
        review.setUser(user);
        //rate
        review.setRatingValue(body.getRating());
        //ParentId
        if(body.getParentId() != null){
            UserReview userReview = reviewRepository.findById(body.getParentId()).orElseThrow();
            review.setParent(userReview);
        }else{
            review.setParent(null);
        }
        //Active
        review.setActive(false);
        //Date
        Date date = new Date();
        review.setCommentDate(date);

        //product
        Product product = productRepository.findById(body.getProductId()).orElseThrow();
        review.setProduct(product);
        //information
        review.setAge(body.getAge());
        review.setHeight((body.getHeight()));
        review.setGender(body.getGender());
        review.setFit(body.getFit());
        review.setWeight(body.getWeight());
        review.setSize(body.getSize());

        return reviewRepository.save(review);
    }

//    public List<UserReview> getAllReviewByProduct(UUID productId) {
//        List<UserReview> reviewList = reviewRepository.findAllByProductId(productId);
//        return reviewList;
//    }

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

    public ReviewResponse getAll(int page, int size, String email, String start, String end, int status, int rating) {

        try{
            List<UserReview> reviewList;
            Specification<UserReview> spec = Specification.where(null);
            if(!email.isEmpty()){
                spec = spec.and((root, query, cb) -> cb.like(root.get("user").get("email"), "%"+email+"%"));
            }
            if(status == 1){
                spec = spec.and((root, query, cb) -> cb.equal(root.get("active"), true));
            }
            if(status == 2){
                spec = spec.and((root, query, cb) -> cb.equal(root.get("active"), false));
            }
            if(rating != 0){
                spec = spec.and((root, query, cb) -> cb.equal(root.get("ratingValue"), rating));
            }

            if(!start.isEmpty() && !end.isEmpty()){
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date date1 = format.parse(start+" 00:00:00");
                Date date2 = format.parse(end + " 23:59:59");
                spec = spec.and((root, query, cb) -> cb.between(root.get("commentDate"), date1,date2));

//                spec = spec.and((root, query, cb) -> cb.greaterThan(root.get("orderDate"), date1));
//                spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("orderDate"), date2));
            }

            Pageable paging = PageRequest.of(page, size, Sort.by("commentDate").descending());
            Page<UserReview> pageReviews = reviewRepository.findAll(spec,paging);
            reviewList = pageReviews.getContent();

            return ReviewResponse.builder()
                    .reviewList(reviewList)
                    .currentPage(pageReviews.getNumber())
                    .totalItems(pageReviews.getTotalElements())
                    .totalPages(pageReviews.getTotalPages())
                    .build();
        }catch (Exception e){
            System.out.println(e);
            return null;
        }

    }

    public void changeActive(UUID id) {

        UserReview review = reviewRepository.findById(id).orElseThrow();
        review.setActive(!review.isActive());
        reviewRepository.save(review);
    }

    public ReviewResponse getAllReviewByProduct(UUID productId, int page, int size, int rating) {
        try{
            List<UserReview> reviewList;
            Specification<UserReview> spec = Specification.where(null);
            spec = spec.and((root, query, cb) -> cb.equal(root.get("product").get("id"), productId));
            spec = spec.and((root, query, cb) -> cb.equal(root.get("active"), true));

            if(rating != 0){
                spec = spec.and((root, query, cb) -> cb.equal(root.get("ratingValue"), rating));
            }


            Pageable paging = PageRequest.of(page, size, Sort.by("commentDate").descending());
            Page<UserReview> pageReviews = reviewRepository.findAll(spec,paging);
            reviewList = pageReviews.getContent();

            return ReviewResponse.builder()
                    .reviewList(reviewList)
                    .currentPage(pageReviews.getNumber())
                    .totalItems(pageReviews.getTotalElements())
                    .totalPages(pageReviews.getTotalPages())
                    .build();
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    public List<UserReview> getAllProduct(UUID productId) {

        List<UserReview> reviewList = reviewRepository.findAllByProductId(productId);
        return reviewList;
    }
}
