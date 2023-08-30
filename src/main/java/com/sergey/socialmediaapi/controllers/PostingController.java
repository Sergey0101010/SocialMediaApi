package com.sergey.socialmediaapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sergey.socialmediaapi.business.PostingService;
import com.sergey.socialmediaapi.domain.dto.PostRequest;
import com.sergey.socialmediaapi.domain.dto.NewPostId;
import com.sergey.socialmediaapi.domain.dto.PostResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@SecurityRequirement(name = "socialMediaAuth")
@Tag(name = "Posting", description = "Everything about posting")
@Slf4j
public class PostingController {
    private final PostingService postingService;

    @Operation(summary = "New post creation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "posted successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = NewPostId.class))
            }),
            @ApiResponse(responseCode = "400", description = "Requester doesn't exist", content = @Content)
    })
    @PostMapping("/new")
    public ResponseEntity<NewPostId> newPost(
            @Parameter(name = "Post json", description = "posting data json", content = @Content)
            @RequestParam("json") String json,
            @Parameter(name = "Image data", description = "image data", content = @Content)
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

    @Operation(summary = "Get post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully retrieved post", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponseDto.class))
            }),
            @ApiResponse(responseCode = "404", description = "Requested post not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPost(@Parameter(name = "Post id", description = "id of requested post")
                                                       @PathVariable UUID id) {
        return ResponseEntity.ok(postingService.getPost(id));
    }

    @Operation(summary = "Update post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "successfully updated post", content = @Content),
            @ApiResponse(responseCode = "400", description = "Image compressing failed", content = @Content),
            @ApiResponse(responseCode = "404", description = "Requested post not found", content = @Content),
            @ApiResponse(responseCode = "405", description = "Can't update other's post", content = @Content)

    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePost(@PathVariable UUID id,
                           @RequestParam("json") String json,
                           @RequestParam("file") MultipartFile file,
                           @AuthenticationPrincipal UserDetails userDetails) {
        PostRequest updatePostRequest = mapPostRequest(json, file);
        postingService.updatePost(id, updatePostRequest, userDetails);
    }

    @Operation(summary = "Delete post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "successfully updated post", content = @Content),
            @ApiResponse(responseCode = "404", description = "Requested post not found", content = @Content),
            @ApiResponse(responseCode = "405", description = "Can't delete other's post", content = @Content)

    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipe(@PathVariable UUID id,
                             @AuthenticationPrincipal UserDetails userDetails) {
        postingService.deletePost(id, userDetails);
    }

}
