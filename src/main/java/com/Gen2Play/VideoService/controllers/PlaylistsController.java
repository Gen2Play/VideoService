package com.Gen2Play.VideoService.controllers;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.Gen2Play.VideoService.model.dto.ResponseDTO;
import com.Gen2Play.VideoService.model.dto.playlists.PlaylistResponse;
import com.Gen2Play.VideoService.model.dto.playlists.PlaylistsRequest;
import com.Gen2Play.VideoService.services.playlists.IPlaylistsService;
import com.google.api.gax.rpc.NotFoundException;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api/playlists")
public class PlaylistsController {
    @Autowired
    private IPlaylistsService playlistsService;

    @GetMapping("")
    public ResponseDTO<?> getPlaylistsOfUser() {
        ResponseDTO<List<PlaylistResponse>> result = new ResponseDTO<>();
        try {
            List<PlaylistResponse> listPlaylists = playlistsService.getPlaylists();
            result.setData(listPlaylists);
            result.setMessage("Get playlists success");
            result.setStatus(200);
        } catch (NoSuchElementException e) {
            result.setData(null);
            result.setMessage(e.getMessage());
            result.setStatus(204);
        } catch (Exception e) {
            result.setData(null);
            result.setMessage("Get playlists fail Server Error: " + e.getMessage());
            result.setStatus(500);
        }
        return result;
    }

    @PostMapping("")
    public ResponseDTO<?> addNewPlaylists(@RequestBody PlaylistsRequest request) {
        ResponseDTO<PlaylistResponse> result = new ResponseDTO<>();
        try {
            PlaylistResponse playlist = playlistsService.addNewPlaylists(request);
            result.setData(playlist);
            result.setMessage("Create playlists success");
            result.setStatus(201);
        } catch (Exception e) {
            result.setData(null);
            result.setMessage("Get playlists fail Server Error: " + e.getMessage());
            result.setStatus(500);
        }
        return result;
    }
    
    @DeleteMapping("/{id}")
    public ResponseDTO<?> deletePlaylists(@PathVariable UUID id){
        ResponseDTO<String> result = new ResponseDTO<>();
        try {
            String message = playlistsService.deletePlaylists(id);
            result.setData(message);
            result.setMessage(message);
            result.setStatus(200);
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
            result.setMessage("Delete playlists fail Server Error: " + e.getMessage());
            result.setStatus(500);
        }
        return result;
    }

    @PutMapping("/{id}")
    public ResponseDTO<?> updatePlaylists(@PathVariable UUID id, @RequestBody PlaylistsRequest request) {
        ResponseDTO<PlaylistResponse> result = new ResponseDTO<>();
        try {
            PlaylistResponse playlist = playlistsService.updatePlaylists(id, request);
            result.setData(playlist);
            result.setMessage("Update playlists success");
            result.setStatus(200);
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
            result.setMessage("Update playlists fail Server Error: " + e.getMessage());
            result.setStatus(500);
        }
        return result;
    }

    @DeleteMapping("/video/{playlistsId}")
    public ResponseDTO<?> deleteVideoInPlaylists(@PathVariable UUID playlistsId,@RequestParam UUID videoId){
        ResponseDTO<PlaylistResponse> result = new ResponseDTO<>();
        try {
            PlaylistResponse message = playlistsService.deleteVideoInPlaylists(playlistsId, videoId);
            result.setData(message);
            result.setMessage("Delete video in playlists success");
            result.setStatus(200);
        } catch (IllegalAccessError e) {
            result.setData(null);
            result.setMessage(e.getMessage());
            result.setStatus(403);
        } catch (NotFoundException | IllegalArgumentException e) {
            result.setData(null);
            result.setMessage(e.getMessage());
            result.setStatus(400);
        } catch (Exception e) {
            result.setData(null);
            result.setMessage("Delete video in playlists fail Server Error: " + e.getMessage());
            result.setStatus(500);
        }
        return result;
    }

    @PutMapping("/video/{playlistsId}")
    public ResponseDTO<?> addVideoToPlaylists(@PathVariable UUID playlistsId, @RequestParam UUID videoId) {
        ResponseDTO<PlaylistResponse> result = new ResponseDTO<>();
        try {
            PlaylistResponse playlist = playlistsService.addVideoToPlaylists(playlistsId, videoId);
            result.setData(playlist);
            result.setMessage("Add video to playlists success");
            result.setStatus(204);
        } catch (IllegalAccessError e) {
            result.setData(null);
            result.setMessage(e.getMessage());
            result.setStatus(403);
        } catch (NotFoundException | IllegalArgumentException e) {
            result.setData(null);
            result.setMessage(e.getMessage());
            result.setStatus(400);
        } catch (Exception e) {
            result.setData(null);
            result.setMessage("Add video to playlists fail Server Error: " + e.getMessage());
            result.setStatus(500);
        }
        return result;
    }
    
}
