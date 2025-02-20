package com.Gen2Play.VideoService.specification;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.Gen2Play.VideoService.model.entity.Hashtag;
import com.Gen2Play.VideoService.model.entity.HashtagRelation;

import jakarta.persistence.criteria.Join;

public class HashtagSpecification {
    public static Specification<Hashtag> hasForeignKey(UUID id, String joinAttribute, String keyAttribute) {
        return (root, query, cb) -> {
            Join<Hashtag, HashtagRelation> join = root.join(joinAttribute);
            return cb.equal(join.get(keyAttribute).get("id"), id);
        };
    }
}
