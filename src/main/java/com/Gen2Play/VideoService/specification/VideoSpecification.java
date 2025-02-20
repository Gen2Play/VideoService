package com.Gen2Play.VideoService.specification;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.Gen2Play.VideoService.model.entity.Video;
import com.Gen2Play.VideoService.model.entity.VideoHashtag;

import jakarta.persistence.criteria.Join;

public class VideoSpecification {
    public static Specification<Video> hasForeignKey(UUID hashtagId, String attributeColumeName, String attributeKeyName) {
        return (root, query, cb) -> {
            Join<Video, VideoHashtag> join = root.join(attributeColumeName);
            return cb.equal(join.get(attributeKeyName).get("id"), hashtagId);
        };
    }

    public static Specification<Video> isTrue(String attributeKeyName) {
        return (root, query, cb) -> cb.isTrue(root.get(attributeKeyName));
    }
}
