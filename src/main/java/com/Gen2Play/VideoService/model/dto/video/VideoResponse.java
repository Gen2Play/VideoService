package com.Gen2Play.VideoService.model.dto.video;

import java.util.List;
import java.util.UUID;

import com.Gen2Play.VideoService.model.entity.enumeration.MediaTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VideoResponse {
    private UUID id = null;
    private String name;
    private int views = 0;
    private int downloads = 0;
    private MediaTypeEnum mediaType;//enum
    private int FPS;
    private boolean isPublic;
    private UUID owner;
    private String sourceLink;
    private String viewLink;
    private List<String> hashtags;
    private String resolution;
    private boolean isSave;
}
