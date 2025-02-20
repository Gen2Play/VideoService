package com.Gen2Play.VideoService.model.dto.playlists;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistResponse {
    private UUID id;
    private UUID owner;
    private String name;
    private boolean isPublic;
    private LocalDateTime createAt;
}
