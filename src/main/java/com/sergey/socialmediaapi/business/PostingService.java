package com.sergey.socialmediaapi.business;

import com.sergey.socialmediaapi.domain.dto.NewPostId;
import com.sergey.socialmediaapi.domain.dto.PostRequest;
import com.sergey.socialmediaapi.domain.dto.PostResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface PostingService {
    NewPostId newPost(PostRequest newPostRequest, UserDetails userDetails);

    PostResponseDto getPost(UUID id);

    void updatePost(UUID id, PostRequest updatePostRequest, UserDetails userDetails);

    void deletePost(UUID id, UserDetails userDetails);
}
