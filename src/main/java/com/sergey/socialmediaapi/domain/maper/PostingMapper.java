package com.sergey.socialmediaapi.domain.maper;

import com.sergey.socialmediaapi.domain.dto.NewPostId;
import com.sergey.socialmediaapi.domain.dto.PostRequest;
import com.sergey.socialmediaapi.domain.dto.PostResponseDto;
import com.sergey.socialmediaapi.domain.model.Post;
import com.sergey.socialmediaapi.domain.util.ImageUtil;

import java.io.IOException;


public class PostingMapper {
    public static NewPostId postToNewPostId(Post post) {
        if (post == null) {
            return null;
        }
        var newPostId = new NewPostId();
        newPostId.setId(post.getUuid());
        return newPostId;
    }

    public static Post postRequestToPost(PostRequest postRequest)  {
        if (postRequest == null) {
            return null;
        }
        Post post = new Post();
        post.setHeader(postRequest.getHeader());
        post.setText(postRequest.getText());

        try {
            post.setPostImage(ImageUtil.compressImage(postRequest.getPhotoData().getBytes()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return post;
    }

    public static PostResponseDto postToPostResponseDto(Post post) {
        if (post == null) {
            return null;
        }
        var postResponseDto = new PostResponseDto();
        postResponseDto.setId(post.getUuid());
        postResponseDto.setHeader(post.getHeader());
        postResponseDto.setText(post.getText());
        postResponseDto.setPostImage(ImageUtil.decompressImage(post.getPostImage()));
        postResponseDto.setCreationDate(post.getCreationDate());
        return postResponseDto;
    }



}
