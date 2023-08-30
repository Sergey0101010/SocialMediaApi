package com.sergey.socialmediaapi.business;

import com.sergey.socialmediaapi.domain.dto.NewPostId;
import com.sergey.socialmediaapi.domain.dto.PostRequest;
import com.sergey.socialmediaapi.domain.dto.PostResponseDto;
import com.sergey.socialmediaapi.domain.maper.PostingMapper;
import com.sergey.socialmediaapi.domain.model.Post;
import com.sergey.socialmediaapi.domain.model.User;
import com.sergey.socialmediaapi.domain.util.ImageUtil;
import com.sergey.socialmediaapi.repositories.PostRepository;
import com.sergey.socialmediaapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostingServiceImpl implements PostingService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public NewPostId newPost(PostRequest newPostRequest, UserDetails userDetails) {
        Post newPost = PostingMapper.postRequestToPost(newPostRequest);
        User postingUser = userRepository
                .findUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        newPost.setCreator(postingUser);
        Post savedPost = postRepository.save(newPost);
        return PostingMapper.postToNewPostId(savedPost);
    }

    @Override
    public PostResponseDto getPost(UUID id) {
        Optional<Post> foundPost = postRepository.findById(id);
        Post post = foundPost.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return PostingMapper.postToPostResponseDto(post);
    }

    @Override
    public void updatePost(UUID id, PostRequest updatePostRequest, UserDetails userDetails) {
        if (isAllowedToChangePost(userDetails, id)) {
            Optional<Post> byId = postRepository.findById(id);
            Post updatedPost = byId.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such post"));
            updatedPost.setText(updatePostRequest.getText());
            try {
                updatedPost.setPostImage(ImageUtil.compressImage(updatePostRequest.getPhotoData().getBytes()));
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            updatedPost.setHeader(updatePostRequest.getHeader());
            postRepository.save(updatedPost);
        } else {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    @Override
    public void deletePost(UUID id, UserDetails userDetails) {
        if (isAllowedToChangePost(userDetails, id)) {
            postRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    private boolean isAllowedToChangePost(UserDetails userDetails, UUID id) {
        return userDetails.getUsername()
                .equals(postRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "This post is not in db"))
                        .getCreator()
                        .getUsername()
                );
    }
}
