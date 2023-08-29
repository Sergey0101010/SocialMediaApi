package com.sergey.socialmediaapi.controllers;

import com.sergey.socialmediaapi.business.ActivityFeedService;
import com.sergey.socialmediaapi.domain.dto.PostResponseDto;
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
@RequestMapping("/api/feed")
public class ActivityFeedController {
    private final ActivityFeedService feedService;
    @GetMapping
    public Page<PostResponseDto> getAllFeedPosts(@RequestParam(required = false, defaultValue = "true") boolean newToOld,
                                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                                  @RequestParam(value = "size", defaultValue = "15") int size,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        return feedService.getAllFeedPosts(newToOld, page, size, userDetails);
    }
}
