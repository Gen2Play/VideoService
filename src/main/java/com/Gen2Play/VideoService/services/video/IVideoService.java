package com.Gen2Play.VideoService.services.video;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.Gen2Play.VideoService.model.dto.video.VideoDownloadResponse;
import com.Gen2Play.VideoService.model.dto.video.VideoGetRequest;
import com.Gen2Play.VideoService.model.dto.video.VideoResponse;
import com.Gen2Play.VideoService.model.dto.video.VideoUploadRequest;

public interface IVideoService {

    VideoResponse uploadVideo(MultipartFile file);

    VideoResponse uploadVideoData(VideoUploadRequest request) throws Exception;

    Page<VideoResponse> getVideo(VideoGetRequest request) throws Exception;

    Page<VideoResponse> getVideoUser(VideoGetRequest request) throws Exception;

    VideoDownloadResponse getDownloadResponse(UUID videoId) throws Exception;

    VideoResponse getVideoToView(UUID id) throws Exception;

    List<VideoResponse> getVideoOfPlaylist(UUID playlistsId) throws Exception;

    List<VideoResponse> getListSaveVideo() throws Exception;

    String saveVideo(UUID id) throws Exception;

}
