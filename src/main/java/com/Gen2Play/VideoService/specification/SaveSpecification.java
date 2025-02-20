package com.Gen2Play.VideoService.specification;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.Gen2Play.VideoService.model.entity.Save;
import com.Gen2Play.VideoService.model.entity.Video;
import jakarta.persistence.criteria.Join;

public class SaveSpecification {
    public static Specification<Save> hasVideo(UUID id, String attributeColumeName, String attributeKeyName) {
        return (root, query, cb) -> {
            Join<Save, Video> join = root.join(attributeColumeName);
            return cb.equal(join.get(attributeKeyName), id);
        };
    }

    public static Specification<Save> hasUUIDFeild(UUID value, String feildName) {
        return (root, query, criteriaBuilder) -> {
            if (value == null) {
                return criteriaBuilder.conjunction(); // Không lọc nếu userId null
            }
            return criteriaBuilder.equal(root.get(feildName), value);
        };
    }
}
