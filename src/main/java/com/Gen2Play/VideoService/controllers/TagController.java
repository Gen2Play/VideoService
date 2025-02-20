package com.Gen2Play.VideoService.controllers;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Gen2Play.VideoService.model.dto.ResponseDTO;
import com.Gen2Play.VideoService.model.dto.tags.HashtagResponse;
import com.Gen2Play.VideoService.services.tags.ITagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/tags")
public class TagController {
    @Autowired
    private ITagService tagService;

    @GetMapping("")
    public ResponseDTO<?> getRelatedTags(@RequestParam String tag) {
        ResponseDTO<List<HashtagResponse>> result = new ResponseDTO<>();
        try {
            List<HashtagResponse> listHashtags = tagService.getRelatedTags(tag);
            result.setData(listHashtags);
            result.setMessage("Get tag success");
            result.setStatus(200);
        } catch (NoSuchElementException e) {
            result.setData(null);
            result.setMessage("No tag related found");
            result.setStatus(204);
        } catch (Exception e) {
            result.setData(null);
            result.setMessage("Get tag fail cuz Server Error: " + e.getMessage());
            result.setStatus(500);
        }
        return result;
    }
    

}
