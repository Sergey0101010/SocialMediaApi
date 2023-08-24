package com.sergey.socialmediaapi.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {
    private UUID id;
    private String header;
    private String text;

    @JsonProperty("post_image")
    private byte[] postImage;

    @JsonProperty("creation_date")
    private LocalDateTime creationDate;
}
