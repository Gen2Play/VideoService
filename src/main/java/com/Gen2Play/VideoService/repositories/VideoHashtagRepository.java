package com.Gen2Play.VideoService.repositories;

import java.util.List;
import java.util.UUID;

import com.Gen2Play.VideoService.model.entity.VideoHashtag;

public interface VideoHashtagRepository extends GenericRepository<VideoHashtag, UUID>{
    // Tìm danh sách hashtag theo videoId
    List<VideoHashtag> findByVideoId(UUID videoId);
}
