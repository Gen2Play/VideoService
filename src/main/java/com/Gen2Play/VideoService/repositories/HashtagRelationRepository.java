package com.Gen2Play.VideoService.repositories;

import java.util.Optional;
import java.util.UUID;

import com.Gen2Play.VideoService.model.entity.HashtagRelation;

public interface HashtagRelationRepository extends GenericRepository<HashtagRelation, UUID>{
    Optional<HashtagRelation> findByHashtagIdAndRelatedHashtagId(UUID hashtagId, UUID relatedHashtagId);
}
