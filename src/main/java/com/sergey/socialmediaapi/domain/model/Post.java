package com.sergey.socialmediaapi.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name="creator_uuid", nullable=false)
    private User creator;

    private String header;

    private String text;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @JsonProperty("post_image")
    private byte[] postImage;
    @PrePersist
    protected void onCreate() {
        creationDate = LocalDateTime.now();
    }
    @JsonProperty("creation_date")
    private LocalDateTime creationDate;

}
