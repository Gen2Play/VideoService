package com.Gen2Play.VideoService.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.Gen2Play.VideoService.model.dto.playlists.PlaylistResponse;
import com.Gen2Play.VideoService.model.dto.playlists.PlaylistsRequest;
import com.Gen2Play.VideoService.model.dto.tags.HashtagResponse;
import com.Gen2Play.VideoService.model.dto.video.VideoResponse;
import com.Gen2Play.VideoService.model.dto.video.VideoUploadRequest;
import com.Gen2Play.VideoService.model.entity.Hashtag;
import com.Gen2Play.VideoService.model.entity.Playlists;
import com.Gen2Play.VideoService.model.entity.Video;

@Mapper
public interface VideoMapper {
    VideoMapper INSTANCE = Mappers.getMapper(VideoMapper.class);

    Video videoUploadRequestToVideo(VideoUploadRequest request);
    VideoResponse videoToVideoResponse(Video video);
    List<VideoResponse> videoListToVideoResponseList(List<Video> videos);
    //Hashtag mapping
    List<HashtagResponse> hashtagListToHashtagResponsesList(List<Hashtag> list);
    //Playlist mapping
    List<PlaylistResponse> listPlaylistsToListPlaylistResponses(List<Playlists> playlists);
    PlaylistResponse playlistsToPlaylistResponses(Playlists playlist);
    Playlists playlistsRequestToPlaylists(PlaylistsRequest request);
}
