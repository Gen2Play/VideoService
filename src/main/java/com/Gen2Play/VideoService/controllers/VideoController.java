package com.Gen2Play.VideoService.controllers;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Gen2Play.VideoService.model.dto.ResponseDTO;
import com.Gen2Play.VideoService.model.dto.cloudinary.CloudinaryUploadResponse;
import com.Gen2Play.VideoService.model.dto.video.VideoDownloadResponse;
import com.Gen2Play.VideoService.model.dto.video.VideoGetRequest;
import com.Gen2Play.VideoService.model.dto.video.VideoResponse;
import com.Gen2Play.VideoService.model.dto.video.VideoUploadRequest;
import com.Gen2Play.VideoService.services.cloudinary.CloudinaryService;
import com.Gen2Play.VideoService.services.video.IVideoService;
import com.google.api.gax.rpc.NotFoundException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/videos")
public class VideoController {
    @Autowired
    private IVideoService videoService;
    private final CloudinaryService cloudinaryService;

    public VideoController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping(value = "")
    public ResponseDTO<VideoResponse> uploadVideo(
        @RequestBody VideoUploadRequest request) {
        ResponseDTO<VideoResponse> result = new ResponseDTO<>();
        try {
            VideoResponse response = videoService.uploadVideoData(request);
            result.setData(response);
            result.setMessage("Create new Video succesfully");
            result.setStatus(201);
        } catch (Exception e) {
            result.setData(null);
            result.setMessage("Upload video fail cuz server Error: " + e.getMessage());
            result.setStatus(500);
            return result;
        }

        return result;
    }

    @PostMapping(value = "/cloud", consumes = {"multipart/form-data"})
    public ResponseDTO<?> uploadVideoCloundinary(@RequestParam("file") MultipartFile file) {
        ResponseDTO<CloudinaryUploadResponse> result = new ResponseDTO<>();
        try {
            CloudinaryUploadResponse cloundResponse = cloudinaryService.uploadVideo(file);
            result.setData(cloundResponse);
            result.setMessage("Upload video to clound successfull");
            result.setStatus(201);
            return result;
        } catch (Exception e) {
            result.setData(null);
            result.setMessage("Upload video to clound fail. Error: " + e.getMessage());
            result.setStatus(500);
            return result;
        }
    }

    @GetMapping("")
    public ResponseDTO<?> getVideo(VideoGetRequest request) {
        ResponseDTO<Page<VideoResponse>> result = new ResponseDTO<>();
        Page<VideoResponse> videos; 
        try {
            videos = videoService.getVideo(request);
            result.setData(videos);
            result.setMessage("Get Video successfull");
            result.setStatus(200);
        } catch (NoSuchElementException e) {
            result.setData(null);
            result.setMessage("No video found");
            result.setStatus(204);
        } catch (Exception e) {
            result.setData(null);
            result.setMessage("Get Video fail cuz Server Error: " + e.getMessage());
            result.setStatus(500);
        }
        return result;
    }
    
    @GetMapping("/user")
    public ResponseDTO<?> getVideoUser(@RequestBody VideoGetRequest request){
        ResponseDTO<Page<VideoResponse>> result = new ResponseDTO<>();
        try {
            Page<VideoResponse> videos = videoService.getVideoUser(request); 
            result.setData(videos);
            result.setMessage("Get Video successfull");
            result.setStatus(200);
        } catch (NoSuchElementException e) {
            result.setData(null);
            result.setMessage("No video found of this user");
            result.setStatus(204);
        } catch (Exception e) {
            result.setData(null);
            result.setMessage("Server error when get video: "+ e.getMessage());
            result.setStatus(500);
        }
        return result;
    }

    // @GetMapping("/download/{id}")
    // public ResponseEntity<?> downloadVideo(@PathVariable UUID videoId) {
    //     ResponseEntity<Object> result = new ResponseEntity<>(null);
    //     try {
    //         String downloadResponse = videoService.getDownloadLink(videoId); // Lấy link từ DB
    //         return ResponseEntity.status(HttpStatus.FOUND)
    //             .header(HttpHeaders.LOCATION, downloadResponse.getName())
    //             .build();
    //     } catch (Exception e) {

    //     }
    //     return result;
    // }

    @GetMapping("/proxy-download/{videoId}")
    public ResponseEntity<?> proxyDownload(@PathVariable UUID videoId){
        try {
            VideoDownloadResponse downloadEntity = videoService.getDownloadResponse(videoId);
            return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + downloadEntity.getName())
            .body(downloadEntity.getVideo());
        } catch (Exception e) {
            return new ResponseEntity<>("Error : "+ e.getMessage() ,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseDTO<?> getVideoToView (@PathVariable UUID id) {
        ResponseDTO<VideoResponse> result = new ResponseDTO<>();
        try {
            VideoResponse videoResponse = videoService.getVideoToView(id);
            result.setData(videoResponse);
            result.setMessage("Get video to view succees");
            result.setStatus(200);
        } catch (NoSuchElementException e) {
            result.setData(null);
            result.setMessage("Error when get video: "+ e.getMessage());
            result.setStatus(204);
        } catch (NotFoundException | IllegalArgumentException e) {
            result.setData(null);
            result.setMessage(e.getMessage());
            result.setStatus(400);
        } catch (Exception e) {
            result.setData(null);
            result.setMessage("Server error when get video: "+ e.getMessage());
            result.setStatus(500);
        }
        return result;
    }

    @GetMapping("/playlists")
    public ResponseDTO<?> getVideoOfPlaylist(@RequestParam UUID playlistsId) {
        ResponseDTO<List<VideoResponse>> result = new ResponseDTO<>();
        List<VideoResponse> videos; 
        try {
            videos = videoService.getVideoOfPlaylist(playlistsId);
            result.setData(videos);
            result.setMessage("Get Video successfull");
            result.setStatus(200);
        } catch (NoSuchElementException e) {
            result.setData(null);
            result.setMessage("No video found");
            result.setStatus(204);
        } catch (NotFoundException | IllegalArgumentException e) {
            result.setData(null);
            result.setMessage(e.getMessage());
            result.setStatus(400);
        } catch (IllegalAccessError e) {
            result.setData(null);
            result.setMessage(e.getMessage());
            result.setStatus(403);
        } catch (Exception e) {
            result.setData(null);
            result.setMessage("Get Video fail cuz Server Error: " + e.getMessage());
            result.setStatus(500);
        }
        return result;
    }
    
    @GetMapping("/save")
    public ResponseDTO<?> getSaveVideo() {
        ResponseDTO<List<VideoResponse>> result = new ResponseDTO<>();
        List<VideoResponse> videos; 
        try {
            videos = videoService.getListSaveVideo();
            result.setData(videos);
            result.setMessage("Get Video successfull");
            result.setStatus(200);
        } catch (NoSuchElementException e) {
            result.setData(null);
            result.setMessage("No video found");
            result.setStatus(204);
        } catch (NotFoundException | IllegalArgumentException e) {
            result.setData(null);
            result.setMessage(e.getMessage());
            result.setStatus(400);
        } catch (Exception e) {
            result.setData(null);
            result.setMessage("Get Video fail cuz Server Error: " + e.getMessage());
            result.setStatus(500);
        }
        return result;
    }
    
    @GetMapping("/save/{id}")
    public ResponseDTO<?> saveVideo(@PathVariable UUID id) {
        ResponseDTO<String> result = new ResponseDTO<>();
        result.setData(null);
        String message; 
        try {
            message = videoService.saveVideo(id);
            result.setData(null);
            result.setMessage(message);
            result.setStatus(200);
        } catch (NoSuchElementException e) {
            result.setMessage(e.getMessage());
            result.setStatus(204);
        } catch (NotFoundException | IllegalArgumentException e) {
            result.setData(null);
            result.setMessage(e.getMessage());
            result.setStatus(400);
        } catch (Exception e) {
            result.setMessage("Server Error: " + e.getMessage());
            result.setStatus(500);
        }
        return result;
    }

}
