package com.secondhand.specification;

import com.secondhand.dto.AdvertisementFilterRequest;
import com.secondhand.entity.Advertisement;
import com.secondhand.entity.AdvertisementStatus;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class AdvertisementSpecification {

    public static Specification<Advertisement> filter(AdvertisementFilterRequest request) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("status"),AdvertisementStatus.ACTIVE));
            if (request.getKeyword() != null && !request.getKeyword().isBlank()) {
                String keyword = "%" + request.getKeyword().toLowerCase() + "%";   
                predicates.add(criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")),keyword), criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),keyword)));
            }
            if (request.getCategory() != null) {
                predicates.add(criteriaBuilder.equal(root.get("category"),request.getCategory()));
            }
            if (request.getCity() != null && !request.getCity().isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("city")),"%" + request.getCity().toLowerCase() + "%"));
            }
            if (request.getMinPrice() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"),request.getMinPrice()));
            }
            if (request.getMaxPrice() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"),request.getMaxPrice()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

    }

}