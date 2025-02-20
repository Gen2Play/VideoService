package com.Gen2Play.VideoService.repositories;

import java.util.List;
import java.util.UUID;

import com.Gen2Play.VideoService.model.entity.Playlists;

public interface PlaylistsRepository extends GenericRepository<Playlists, UUID>{
    List<Playlists> findByOwner (UUID owner);
}
