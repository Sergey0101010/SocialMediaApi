package com.sergey.socialmediaapi.controllers;

import com.sergey.socialmediaapi.business.ActivityFeedService;
import com.sergey.socialmediaapi.domain.dto.AuthenticationResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@SecurityRequirement(name = "socialMediaAuth")
@RequestMapping("/api/feed")
@Tag(name = "User feed", description = "Everything about user feed actions")
public class ActivityFeedController {
    private final ActivityFeedService feedService;

    @Operation(summary = "Get user feed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponseDto.class))
            }),
            @ApiResponse(responseCode = "400", description = "Requesting user don't exist", content = @Content),
    })
    @GetMapping
    public Page<PostResponseDto> getAllFeedPosts(@Parameter(name = "Descending order")
                                                     @RequestParam(required = false, defaultValue = "true") boolean newToOld,
                                                 @Parameter(name = "Page number")
                                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                                 @Parameter(name = "Elements quantity")
                                                     @RequestParam(value = "size", defaultValue = "15") int size,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        return feedService.getAllFeedPosts(newToOld, page, size, userDetails);
    }
}
