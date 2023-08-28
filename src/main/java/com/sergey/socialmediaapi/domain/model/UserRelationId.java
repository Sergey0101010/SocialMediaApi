package com.sergey.socialmediaapi.domain.model;


import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Embeddable
public class UserRelationId implements Serializable {
    @ManyToOne
    @JoinColumn(name = "my_uuid", insertable = false, updatable = false)
    private User relatingUser;


    @ManyToOne
    @JoinColumn(name = "related_uuid", insertable = false, updatable = false)
    private User relatedUser;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRelationId that = (UserRelationId) o;
        return Objects.equals(relatingUser, that.relatingUser) && Objects.equals(relatedUser, that.relatedUser);
    }

    @Override
    public String toString() {
        return "UserRelationId{\n" +
                "relatingUser=" + relatingUser.getUuid() + "\n" +
                ", relatedUser=" + relatedUser.getUuid() + "\n" +
                '}';
    }



    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $relatingUser = this.relatingUser;
        result = result * PRIME + ($relatingUser == null ? 43 : $relatingUser.hashCode());
        final Object $relatedUser = this.relatedUser;
        result = result * PRIME + ($relatedUser == null ? 43 : $relatedUser.hashCode());
        return result;
    }
}
