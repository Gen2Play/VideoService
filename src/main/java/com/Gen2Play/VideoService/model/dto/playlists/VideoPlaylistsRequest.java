package com.Gen2Play.VideoService.model.dto.playlists;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoPlaylistsRequest {
    List<UUID> videosId;
}
