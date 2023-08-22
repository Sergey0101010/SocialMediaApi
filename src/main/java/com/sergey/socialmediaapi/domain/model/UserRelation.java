package com.sergey.socialmediaapi.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserRelation {

    @EmbeddedId
    private UserRelationId relationId;


    @Enumerated(EnumType.STRING)
    @Column
    private RelationState relationState;
}
