package com.Gen2Play.VideoService.model.dto.cloudinary;

import com.Gen2Play.VideoService.model.entity.enumeration.MediaTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CloudinaryUploadResponse {
    private String name;
    private int FPS = 60;
    private String resolution;
    private String sourceLink;
    private String viewLink;
    private MediaTypeEnum format;
}
