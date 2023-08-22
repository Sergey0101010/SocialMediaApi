package com.sergey.socialmediaapi.domain.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class UserRelationId implements Serializable {


    @ManyToOne
    @JoinColumn(name = "my_uuid", insertable=false, updatable = false)
    private User relatingUser;


    @ManyToOne
    @JoinColumn(name = "related_uuid", insertable=false, updatable = false)
    private User relatedUser;

}
