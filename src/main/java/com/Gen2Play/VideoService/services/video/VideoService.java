package com.Gen2Play.VideoService.services.video;

import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.Gen2Play.VideoService.mappers.VideoMapper;
import com.Gen2Play.VideoService.model.dto.video.VideoDownloadResponse;
import com.Gen2Play.VideoService.model.dto.video.VideoGetRequest;
import com.Gen2Play.VideoService.model.dto.video.VideoResponse;
import com.Gen2Play.VideoService.model.dto.video.VideoUploadRequest;
import com.Gen2Play.VideoService.model.entity.Hashtag;
import com.Gen2Play.VideoService.model.entity.HashtagRelation;
import com.Gen2Play.VideoService.model.entity.Playlists;
import com.Gen2Play.VideoService.model.entity.Save;
import com.Gen2Play.VideoService.model.entity.Video;
import com.Gen2Play.VideoService.model.entity.VideoHashtag;
import com.Gen2Play.VideoService.model.entity.VideoPlaylists;
import com.Gen2Play.VideoService.repositories.HashtagRelationRepository;
import com.Gen2Play.VideoService.repositories.HashtagRepository;
import com.Gen2Play.VideoService.repositories.IVideoRepository;
import com.Gen2Play.VideoService.repositories.PlaylistsRepository;
import com.Gen2Play.VideoService.repositories.SaveRepository;
import com.Gen2Play.VideoService.repositories.VideoHashtagRepository;
import com.Gen2Play.VideoService.repositories.VideoPlaylistsRepository;
import com.Gen2Play.VideoService.specification.SaveSpecification;
import com.Gen2Play.VideoService.specification.VideoSpecification;
import com.Gen2Play.VideoService.utils.Utils;

@Service
public class VideoService implements IVideoService{
    @Autowired
    private IVideoRepository videoRepository;
    @Autowired
    private HashtagRepository tagsRepository;
    @Autowired
    private VideoHashtagRepository videoHashtagRepository;
    @Autowired
    private HashtagRelationRepository tagRelationRepository;
    @Autowired
    private PlaylistsRepository playlistsRepository;
    @Autowired
    private VideoPlaylistsRepository videoPlaylistsRepository;
    @Autowired
    private SaveRepository saveRepository;

    @Override
    public VideoResponse uploadVideo(MultipartFile file) {
        VideoResponse result = new VideoResponse();
        return result;
    }

    @Override
    @Transactional
    public VideoResponse uploadVideoData(VideoUploadRequest request) throws Exception {
        VideoResponse result = new VideoResponse();
        try {
            //check valid request
            checkValidVideoUploadRequest(request);
            //check owner
            UUID ownerID = Utils.getCurentUserID();
            //create new Video entity, save
            Video video = VideoMapper.INSTANCE.videoUploadRequestToVideo(request);
            //set side info
            video.setOwner(ownerID);
            video.setCreateAt(LocalDateTime.now());
            if(video.isPublic()){
                video.setPublishAt(LocalDateTime.now());;
            }
            videoRepository.save(video);
            result = VideoMapper.INSTANCE.videoToVideoResponse(video);
            //hagtashs after save
            List<String> tags = addHashtagsToVideo(request.getHashtags(), video);
            addTagRelationShip(tags);
            result.setHashtags(tags);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (Exception e){
            throw e;
        }
        return result;
    }

    @Override
    public Page<VideoResponse> getVideo(VideoGetRequest request) throws Exception {
        Page<VideoResponse> result = null;
        Pageable pageable = PageRequest.of(request.getPageIndex(), request.getPageSize());
        try {
            Page<Video> videoPage;
            if (request.getTag().isEmpty() || request.getTag().isBlank()) {
                videoPage = videoRepository.findByIsPublic(pageable, true);
                //get videos in db and attach result value
                List<VideoResponse> videoResponseList = VideoMapper.INSTANCE.videoListToVideoResponseList(videoPage.getContent());
                result = new PageImpl<>(videoResponseList, pageable, videoPage.getTotalElements());
            } else {
                Optional<Hashtag> tagOptional = tagsRepository.findByName(request.getTag());
                if (tagOptional.isPresent()) {
                    Hashtag tag = tagOptional.get();
                    Specification<Video> spec = Specification
                        .where(VideoSpecification.hasForeignKey(tag.getId(),"videoHashtags","hashtag"))
                        .and(VideoSpecification.isTrue("isPublic"));
                    videoPage = videoRepository.findAll(spec, pageable);
                    List<VideoResponse> videoResponseList = VideoMapper.INSTANCE.videoListToVideoResponseList(videoPage.getContent());
                    result = new PageImpl<>(videoResponseList, pageable, videoPage.getTotalElements());
                } else {
                    throw new NoSuchElementException("No videos found");
                }
            }
            //no videos found in db
            if(result.isEmpty()){
                throw new NoSuchElementException("No videos found");
            }
            for (VideoResponse video : result) {
                Save save = checkSaveVideo(video.getId());
                if (save == null) {
                    video.setSave(false);
                }else{
                    video.setSave(true);
                }
            }
            //return
            return result;
        } catch (Exception e) {
            throw e;
        } 
    }

    @Override
    public Page<VideoResponse> getVideoUser(VideoGetRequest request) throws Exception {
        Page<VideoResponse> result = null;
        Pageable pageable = PageRequest.of(request.getPageIndex(), request.getPageSize());
        try {
            //get uuid of user through jwt
            UUID ownerId = Utils.getCurentUserID();
            Page<Video> videoPage = videoRepository.findByOwner(ownerId, pageable);
            List<VideoResponse> videoResponseList = VideoMapper.INSTANCE.videoListToVideoResponseList(videoPage.getContent());
            result = new PageImpl<>(videoResponseList, pageable, videoPage.getTotalElements());
            //check save video
            for (VideoResponse video : result) {
                Save save = checkSaveVideo(video.getId());
                if (save == null) {
                    video.setSave(false);
                }else{
                    video.setSave(true);
                }
            }
            //return result
            return result;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public VideoDownloadResponse getDownloadResponse(UUID videoId) throws Exception {
        VideoDownloadResponse result = new VideoDownloadResponse();
        try {
            //get video and check
            Optional<Video> videoOptional = videoRepository.findById(videoId);
            if (!videoOptional.isPresent()) {
                throw new NoSuchElementException("Video with id not found");
            }
            Video video = videoOptional.get();
            // Tải file từ Cloudinary
            @SuppressWarnings("deprecation")
            URL url = new URL(video.getSourceLink());
            InputStream in = url.openStream();
            byte[] videoBytes = in.readAllBytes();
            in.close();
            int download = video.getDownloads();
            video.setDownloads(download++);
            videoRepository.save(video);
            String name = video.getName() + "." + video.getMediaType().toString().toLowerCase();
            //fill infi to result
            result.setVideo(videoBytes);
            result.setName(name);
            return result;
        } catch (NoSuchElementException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public VideoResponse getVideoToView(UUID id) throws Exception {
        VideoResponse result = null;
        try {
            //check video existed
            Optional<Video> videoOptional = videoRepository.findById(id);
            if (!videoOptional.isPresent()) {
                throw new NoSuchElementException("Video with ID not found");
            }
            Video video = videoOptional.get();
            //get video tags
            List<VideoHashtag> videoHashtags = videoHashtagRepository.findByVideoId(video.getId());
            List<String> tags = new ArrayList<>();
            for (VideoHashtag videoHashtag : videoHashtags) {
                tags.add(videoHashtag.getHashtag().getName());
            }
            result = VideoMapper.INSTANCE.videoToVideoResponse(video);
            result.setHashtags(tags);
            result.setSave(false);
            //up view and save
            video.setViews(video.getViews() + 1);
            videoRepository.save(video);
        } catch (Exception e) {
            throw e;
        }
        return result;
    }

    @Override
    public List<VideoResponse> getVideoOfPlaylist(UUID playlistsId) throws Exception {
        List<VideoResponse> result = new ArrayList<>();
        try {
            //check playlists existed
            Optional<Playlists> playlistsOptional = playlistsRepository.findById(playlistsId);
            if (!playlistsOptional.isPresent()) {
                throw new NoSuchElementException("Playlist not found");
            }
            Playlists playlists = playlistsOptional.get();
            //get uuid of user through jwt
            UUID ownerId = Utils.getCurentUserID();
            //check is public playlists
            if (!playlists.isPublic()) {
                //check owner
                if (!playlists.getOwner().equals(ownerId)) {
                    throw new IllegalAccessError("Owner not match with the curent user to access the non-public playlists");
                }
            }
            //get list Video to response
            List<VideoPlaylists> videoPlaylists = videoPlaylistsRepository.findByPlaylists_Id(playlistsId);
            for (VideoPlaylists entity : videoPlaylists) {
                VideoResponse videoResponse = VideoMapper.INSTANCE.videoToVideoResponse(entity.getVideo());
                result.add(videoResponse);
            }
            //check save
            for (VideoResponse video : result) {
                Save save = checkSaveVideo(video.getId());
                if (save == null) {
                    video.setSave(false);
                }else{
                    video.setSave(true);
                }
            }
            //return result
            return result;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<VideoResponse> getListSaveVideo() throws Exception {
        List<VideoResponse> result = new ArrayList<>();
        try {
            UUID ownerId = Utils.getCurentUserID();
            List<Save> saves = saveRepository.findByUserId(ownerId);
            if (saves.isEmpty()) {
                throw new NoSuchElementException("No save video found");
            }
            for (Save save : saves) {
                VideoResponse vr = VideoMapper.INSTANCE.videoToVideoResponse(save.getVideo());
                vr.setSave(true);
                result.add(vr);
            }
        } catch (Exception e) {
            throw e;
        }
        return result;
    }

    @Override
    public String saveVideo(UUID id) throws Exception {
        String result = "";
        try {
            //check video
            Optional<Video> videoOtp = videoRepository.findById(id);
            if (!videoOtp.isPresent()) {
                throw new IllegalAccessError("Video not found");
            }
            UUID ownerId = Utils.getCurentUserID();
            Specification<Save> spec = Specification
                                    .where(SaveSpecification.hasVideo(id, "video", "id"))
                                    .and(SaveSpecification.hasUUIDFeild(ownerId, "userId"));
            Optional<Save> saveOtp = saveRepository.findOne(spec);
            Save save = saveOtp.get();
            if (saveOtp.isPresent()) {
                saveRepository.delete(save);
                result = "Save video successfull";
            }else{
                saveRepository.save(save);
                result = "Unsave video successfull";
            }
        } catch (Exception e) {
            throw e;
        }
        return result;
    }

    private Save checkSaveVideo(UUID videoId){
        UUID ownerId = Utils.getCurentUserID();
        Specification<Save> spec = Specification
                                    .where(SaveSpecification.hasVideo(videoId, "video", "id"))
                                    .and(SaveSpecification.hasUUIDFeild(ownerId, "userId"));
        Optional<Save> saveOtp = saveRepository.findOne(spec);
        if (saveOtp.isPresent()) {
            return saveOtp.get();
        }
        return null;
    }

    private void checkValidVideoUploadRequest(VideoUploadRequest request) throws Exception{
        if(request.getName().isEmpty() || request.getName() == null){
            throw new IllegalArgumentException("Name is empty!");
        }
        if(request.getSourceLink().isEmpty() || request.getSourceLink() == null){
            throw new IllegalArgumentException("Source link video is empty!");
        }
        if(request.getViewLink().isEmpty() || request.getViewLink() == null){
            throw new IllegalArgumentException("View link video is empty!");
        }
        // if (MediaTypeEnum.isValidFormat(request.getMediaType().toString())) {
        //     throw new IllegalArgumentException("Media type is not valid!");
        // }
    }
    
    private List<String> addHashtagsToVideo(List<String> tags, Video video){
        UUID curentUser = Utils.getCurentUserID();
        for (String tag : tags) {
            //hashtag
            Optional<Hashtag> hashtagOptional = tagsRepository.findByName(tag);
            if (hashtagOptional.isPresent()) {
                VideoHashtag videoHashtag = new VideoHashtag();
                videoHashtag.setVideo(video);
                videoHashtag.setHashtag(hashtagOptional.get());
                videoHashtagRepository.save(videoHashtag);
            }else{
                Hashtag newTag = new Hashtag();
                newTag.setName(tag);
                newTag.setCreateBy(curentUser);
                tagsRepository.save(newTag);
                VideoHashtag videoHashtag = new VideoHashtag();
                videoHashtag.setVideo(video);
                videoHashtag.setHashtag(newTag);
                videoHashtagRepository.save(videoHashtag);
            }
        }
        return tags;
    }

    private void addTagRelationShip(List<String> tags){
        for (String tag : tags) {
            Optional<Hashtag> hashtagOptional = tagsRepository.findByName(tag);
            Hashtag originTag = hashtagOptional.get();
            for (String relationTag : tags) {
                if (!relationTag.equals(tag)) {
                    Optional<Hashtag> relationTagOptional = tagsRepository.findByName(relationTag);
                    Hashtag relationHashtag = relationTagOptional.get();
                    //use example to get
                    HashtagRelation probe = new HashtagRelation();
                    probe.setHashtag(originTag);
                    probe.setRelatedHashtag(relationHashtag);
                    ExampleMatcher matcher = ExampleMatcher.matchingAll();
                    Example<HashtagRelation> example = Example.of(probe, matcher);
                    // Optional<HashtagRelation> hashtagRelationOptional = tagRelationRepository.findByHashtagIdAndRelatedHashtagId(originTag.getId(), relationHashtag.getId());
                    Optional<HashtagRelation> hashtagRelationOptional = tagRelationRepository.findOne(example);
                    if (hashtagRelationOptional.isPresent()) {
                        HashtagRelation hashtagRelation = hashtagRelationOptional.get();
                        int count = hashtagRelation.getCount();
                        hashtagRelation.setCount(count + 1);
                        tagRelationRepository.save(hashtagRelation);
                    } else {
                        HashtagRelation hashtagRelation = new HashtagRelation();
                        hashtagRelation.setHashtag(originTag);
                        hashtagRelation.setRelatedHashtag(relationHashtag);
                        hashtagRelation.setCount(1);
                        tagRelationRepository.save(hashtagRelation);
                    }
                }
            }
        }
    }
}
