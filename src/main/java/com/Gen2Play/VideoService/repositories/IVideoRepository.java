package com.Gen2Play.VideoService.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Repository;

import com.Gen2Play.VideoService.model.entity.Video;

@Repository
public interface IVideoRepository extends GenericRepository<Video, UUID> {
    Page<Video> findAll(Pageable pageable);
    Page<Video> findByIsPublic(Pageable pageable, boolean status);
    Page<Video> findByOwner(UUID owner, Pageable pageable);
    // Tìm tất cả video có hashtag chứa hashtagId
    Page<Video> findByVideoHashtags_IdAndIsPublicTrue(UUID hashtagId, Pageable pageable);
}
