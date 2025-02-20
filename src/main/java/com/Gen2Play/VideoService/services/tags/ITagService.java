package com.Gen2Play.VideoService.services.tags;

import java.util.List;

import com.Gen2Play.VideoService.model.dto.tags.HashtagResponse;

public interface ITagService {

    List<HashtagResponse> getRelatedTags(String tag) throws Exception;

}
