package com.Gen2Play.VideoService.services.playlists;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Gen2Play.VideoService.mappers.VideoMapper;
import com.Gen2Play.VideoService.model.dto.playlists.PlaylistResponse;
import com.Gen2Play.VideoService.model.dto.playlists.PlaylistsRequest;
import com.Gen2Play.VideoService.model.entity.Playlists;
import com.Gen2Play.VideoService.model.entity.Video;
import com.Gen2Play.VideoService.model.entity.VideoPlaylists;
import com.Gen2Play.VideoService.repositories.IVideoRepository;
import com.Gen2Play.VideoService.repositories.PlaylistsRepository;
import com.Gen2Play.VideoService.repositories.VideoPlaylistsRepository;
import com.Gen2Play.VideoService.specification.VideoPlaylistsSpecification;
import com.Gen2Play.VideoService.utils.Utils;

@Service
public class PlaylistsService implements IPlaylistsService{
    @Autowired
    private PlaylistsRepository playlistsRepository;
    @Autowired
    private VideoPlaylistsRepository videoPlaylistsRepository;
    @Autowired
    private IVideoRepository videoRepository;

    @Override
    public List<PlaylistResponse> getPlaylists() throws Exception {
        List<PlaylistResponse> result;
        try {
            //get owner
            UUID ownerId = Utils.getCurentUserID();
            List<Playlists> playlists = playlistsRepository.findByOwner(ownerId);
            if (playlists.isEmpty()) {
                throw new NoSuchElementException("No playlists of this user found");
            }
            result = VideoMapper.INSTANCE.listPlaylistsToListPlaylistResponses(playlists);
        } catch (Exception e) {
            throw e;
        }
        return result;
    }

    @Override
    @Transactional
    public String deletePlaylists(UUID id) throws Exception {
        String result = "Delete Playlist success";
        try {
            //check playlists
            Optional<Playlists> playlistsOtp = playlistsRepository.findById(id);
            if (!playlistsOtp.isPresent()) {
                throw new NoSuchElementException("Playlists not found");
            }
            Playlists playlists = playlistsOtp.get();
            //get cur user
            UUID ownerID = Utils.getCurentUserID();
            //check user
            if (!playlists.getOwner().equals(ownerID)) {
                throw new IllegalAccessError("Access Denied: Curent user is not the playlist owner");
            }
            //delete Video in playlists
            videoPlaylistsRepository.deleteByPlaylists_Id(id);
            //delete Playlists
            playlistsRepository.delete(playlists);
        } catch (Exception e) {
            throw e;
        }
        return result;
    }

    @Override
    @Transactional
    public PlaylistResponse addNewPlaylists(PlaylistsRequest request) throws Exception {
        PlaylistResponse result;
        try {
            //check valid
            if (request.getName().isEmpty()) {
                throw new IllegalArgumentException("Name can't be empty");
            }
            Playlists playlists = VideoMapper.INSTANCE.playlistsRequestToPlaylists(request);
            //get owner
            UUID ownerId = Utils.getCurentUserID();
            playlists.setOwner(ownerId);
            playlists.setCreateAt(LocalDateTime.now());
            playlists.setPublic(request.isPublic());
            playlistsRepository.save(playlists);
            result = VideoMapper.INSTANCE.playlistsToPlaylistResponses(playlists);
        } catch (Exception e) {
            throw e;
        }
        return result;
    }

    @Override
    @Transactional
    public PlaylistResponse updatePlaylists(UUID id, PlaylistsRequest request) throws Exception {
        PlaylistResponse result;
        try {
            //check valid
            if (request.getName().isEmpty()) {
                throw new IllegalArgumentException("Name can't be empty");
            }
            Optional<Playlists> playlistsOptional = playlistsRepository.findById(id);
            if (!playlistsOptional.isPresent()) {
                throw new NoSuchElementException("Playlist not found");
            }
            Playlists playlists = playlistsOptional.get();
            //get owner
            UUID ownerId = Utils.getCurentUserID();
            //check user accesss
            if (!playlists.getOwner().equals(ownerId)) {
                throw new IllegalAccessError("Access Denied: Curent user is not the playlist owner");
            }
            playlists.setName(request.getName());
            playlists.setPublic(request.isPublic());
            playlists.setCreateAt(LocalDateTime.now());
            playlistsRepository.save(playlists);
            result = VideoMapper.INSTANCE.playlistsToPlaylistResponses(playlists);
        } catch (Exception e) {
            throw e;
        }
        return result;
    }

    @Override
    public PlaylistResponse addVideoToPlaylists(UUID playlistsId, UUID videoId) throws Exception {
        PlaylistResponse result;
        try {
            //check valid video
            Optional<Video> videOptional = videoRepository.findById(videoId);
            if (!videOptional.isPresent()) {
                throw new NoSuchElementException("Video not found");
            }
            Optional<Playlists> playlistsOptional = playlistsRepository.findById(playlistsId);
            if (!playlistsOptional.isPresent()) {
                throw new NoSuchElementException("Playlist not found");
            }
            Playlists playlists = playlistsOptional.get();
            //get owner
            UUID ownerId = Utils.getCurentUserID();
            //check user accesss
            if (!playlists.getOwner().equals(ownerId)) {
                throw new IllegalAccessError("Access Denied: Curent user is not the playlist owner");
            }
            //check video in playlists
            Specification<VideoPlaylists> spec = Specification
                        .where(VideoPlaylistsSpecification.hasVideo(videoId,"video","id"))
                        .and(VideoPlaylistsSpecification.hasPlaylists(playlistsId,"playlists","id"));
            Optional<VideoPlaylists> videoPlaylistsOptional = videoPlaylistsRepository.findOne(spec);
            if (videoPlaylistsOptional.isPresent()) {
                throw new IllegalArgumentException("Video are already in playlist");
            }
            //create new video in playlists
            VideoPlaylists videoPlaylists = new VideoPlaylists();
            videoPlaylists.setVideo(videOptional.get());
            videoPlaylists.setPlaylists(playlists);
            videoPlaylists.setAddAt(LocalDateTime.now());
            videoPlaylistsRepository.save(videoPlaylists);
            result = VideoMapper.INSTANCE.playlistsToPlaylistResponses(playlists);
        } catch (Exception e) {
            throw e;
        }
        return result;
    }

    @Override
    @Transactional
    public PlaylistResponse deleteVideoInPlaylists(UUID playlistsId, UUID videoId) throws Exception {
        PlaylistResponse result;
        try {
            //check valid video
            Optional<Video> videOptional = videoRepository.findById(videoId);
            if (!videOptional.isPresent()) {
                throw new NoSuchElementException("Video not found");
            }
            Optional<Playlists> playlistsOptional = playlistsRepository.findById(playlistsId);
            if (!playlistsOptional.isPresent()) {
                throw new NoSuchElementException("Playlist not found");
            }
            Playlists playlists = playlistsOptional.get();
            //get owner
            UUID ownerId = Utils.getCurentUserID();
            //check user accesss
            if (!playlists.getOwner().equals(ownerId)) {
                throw new IllegalAccessError("Access Denied: Curent user is not the playlist owner");
            }
            Specification<VideoPlaylists> spec = Specification
                        .where(VideoPlaylistsSpecification.hasVideo(videoId,"video","id"))
                        .and(VideoPlaylistsSpecification.hasPlaylists(playlistsId,"playlists","id"));
            Optional<VideoPlaylists> videoPlaylistsOptional = videoPlaylistsRepository.findOne(spec);
            if (!videoPlaylistsOptional.isPresent()) {
                throw new NoSuchElementException("Video are not is this playlists");
            }
            videoPlaylistsRepository.delete(videoPlaylistsOptional.get());
            result = VideoMapper.INSTANCE.playlistsToPlaylistResponses(playlists);
        } catch (Exception e) {
            throw e;
        }
        return result;
    }

}
