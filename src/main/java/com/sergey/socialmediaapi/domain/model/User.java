package com.sergey.socialmediaapi.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID uuid;

    @NotBlank
    @Pattern(regexp = "[^@]+@[^@]+\\.[^@.]+")
    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "creator")
    private List<Post> userPosts;


    @OneToMany(mappedBy = "relationId.relatingUser")
    private List<UserRelation> userRelations;







}
