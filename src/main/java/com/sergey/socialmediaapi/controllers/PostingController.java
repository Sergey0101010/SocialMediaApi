package com.sergey.socialmediaapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sergey.socialmediaapi.business.PostingService;
import com.sergey.socialmediaapi.domain.dto.PostRequest;
import com.sergey.socialmediaapi.domain.dto.NewPostId;
import com.sergey.socialmediaapi.domain.dto.PostResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/post")
@RequiredArgsConstructor
@Slf4j
public class PostingController {
    private final PostingService postingService;

    @PostMapping("/new")
    public ResponseEntity<NewPostId> newPost(@RequestParam("json") String json,
                                             @RequestParam("file") MultipartFile file,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        PostRequest newPostRequest = mapPostRequest(json, file);
        return ResponseEntity.ok(postingService.newPost(newPostRequest, userDetails));
    }

    private static PostRequest mapPostRequest(String json, MultipartFile file) {
        Optional<String> contentTypeOptional = Optional.ofNullable(file.getContentType());
        String contentType = contentTypeOptional
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No content type"));
        if (contentType.equals("image/png") || contentType.equals("image/jpeg")) {


            log.info(contentType);
            ObjectMapper mapper = new ObjectMapper();
            PostRequest newPostRequest = null;
            try {
                newPostRequest = mapper.readValue(json, PostRequest.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            newPostRequest.setPhotoData(file);
            return newPostRequest;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong image format, provide png or jpeg");
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable UUID id) {
        return ResponseEntity.ok(postingService.getPost(id));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePost(@PathVariable UUID id,
                           @RequestParam("json") String json,
                           @RequestParam("file") MultipartFile file,
                           @AuthenticationPrincipal UserDetails userDetails) {
        PostRequest updatePostRequest = mapPostRequest(json, file);
        postingService.updatePost(id, updatePostRequest, userDetails);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipe(@PathVariable UUID id,
                             @AuthenticationPrincipal UserDetails userDetails) {
        postingService.deletePost(id, userDetails);
    }

}
