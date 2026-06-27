package com.pfa.app.repository;

import com.pfa.app.model.PlayerProfile;
import org.springframework.data.jpa.domain.Specification;

public class PlayerProfileSpecification {

//    public static Specification<PlayerProfile> hasHealthStatus(Boolean healthy) {
//        return (root, query, cb) -> healthy == null ? cb.conjunction() : cb.equal(root.get("healthy"), healthy);
//    }
//
//    public static Specification<PlayerProfile> searchByName(String searchTerm) {
//        return (root, query, cb) -> {
//            if (searchTerm == null || searchTerm.trim().isEmpty()) {
//                return cb.conjunction();
//            }
//            String likePattern = "%" + searchTerm.toLowerCase() + "%";
//            return cb.or(
//                    cb.like(cb.lower(root.get("firstName")), likePattern),
//                    cb.like(cb.lower(root.get("lastName")), likePattern)
//            );
//        };
//    }


    public static Specification<PlayerProfile> searchByCategory(String searchTerm) {
        return (root, query, cb) -> {
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                return cb.conjunction();
            }
            String likePattern = "%" + searchTerm.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("category")), likePattern)
            );
        };
    }
}
