package com.Gen2Play.VideoService.repositories;

import java.util.Optional;
import java.util.UUID;

import com.Gen2Play.VideoService.model.entity.Hashtag;

public interface HashtagRepository extends GenericRepository<Hashtag, UUID>{
    // Tìm hashtag theo tên (không phân biệt hoa thường)
    Optional<Hashtag> findByName(String name);
}
