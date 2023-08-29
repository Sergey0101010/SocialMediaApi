package com.sergey.socialmediaapi.business;

import com.sergey.socialmediaapi.domain.dto.PostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

public interface ActivityFeedService {
    Page<PostResponseDto> getAllFeedPosts(boolean newToOld, int page, int size, UserDetails userDetails);
}
