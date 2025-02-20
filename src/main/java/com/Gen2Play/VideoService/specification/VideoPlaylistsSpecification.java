package com.Gen2Play.VideoService.specification;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.Gen2Play.VideoService.model.entity.Playlists;
import com.Gen2Play.VideoService.model.entity.Video;
import com.Gen2Play.VideoService.model.entity.VideoPlaylists;

import jakarta.persistence.criteria.Join;

public class VideoPlaylistsSpecification {
    public static Specification<VideoPlaylists> hasVideo(UUID id, String attributeColumeName, String attributeKeyName) {
        return (root, query, cb) -> {
            Join<VideoPlaylists, Video> join = root.join(attributeColumeName);
            return cb.equal(join.get(attributeKeyName), id);
        };
    }

    public static Specification<VideoPlaylists> hasPlaylists(UUID id, String attributeColumeName, String attributeKeyName) {
        return (root, query, cb) -> {
            Join<VideoPlaylists, Playlists> join = root.join(attributeColumeName);
            return cb.equal(join.get(attributeKeyName), id);
        };
    }
}
