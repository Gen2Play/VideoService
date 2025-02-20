package com.Gen2Play.VideoService.model.dto.video;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDownloadResponse {
    private byte[] video;
    private String name;
}
