package com.Gen2Play.VideoService.model.dto.video;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoGetRequest {
    private int pageSize = 10;
    private int pageIndex = 0;
    private String tag = "";
}
