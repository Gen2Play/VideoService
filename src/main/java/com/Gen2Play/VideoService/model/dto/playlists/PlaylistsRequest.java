 package com.Gen2Play.VideoService.model.dto.playlists;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistsRequest {
    private String name;
    private boolean isPublic;
}
