package com.Gen2Play.VideoService.services.playlists;

import java.util.List;
import java.util.UUID;

import com.Gen2Play.VideoService.model.dto.playlists.PlaylistResponse;
import com.Gen2Play.VideoService.model.dto.playlists.PlaylistsRequest;

public interface IPlaylistsService {

    List<PlaylistResponse> getPlaylists() throws Exception;

    String deletePlaylists(UUID id) throws Exception;

    PlaylistResponse addNewPlaylists(PlaylistsRequest request) throws Exception;

    PlaylistResponse updatePlaylists(UUID id, PlaylistsRequest request) throws Exception;

    PlaylistResponse addVideoToPlaylists(UUID playlistsId, UUID videoId) throws Exception;

    PlaylistResponse deleteVideoInPlaylists(UUID playlistsId, UUID videoId) throws Exception;

}
