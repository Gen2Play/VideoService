package com.Gen2Play.VideoService.repositories;

import java.util.List;
import java.util.UUID;

import com.Gen2Play.VideoService.model.entity.VideoPlaylists;

public interface VideoPlaylistsRepository extends GenericRepository<VideoPlaylists,UUID>{
    List<VideoPlaylists> findByPlaylists_Id(UUID playlistId);
    void deleteByPlaylists_Id(UUID playlistId);
}
