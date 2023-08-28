package com.sergey.socialmediaapi.domain.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserRelation {

    @EmbeddedId
    private UserRelationId relationId;

    @Enumerated(EnumType.STRING)
    @Column
    private RelationState relationState;

    @Enumerated(EnumType.STRING)
    @Column(length = 32, columnDefinition = "varchar(32) default 'NOT_ALLOWED'")
    private MessagingState messagingState = MessagingState.NOT_ALLOWED;

    public UserRelationId getRelationId() {
        return this.relationId;
    }

    public RelationState getRelationState() {
        return this.relationState;
    }

    public MessagingState getMessagingState() {
        return this.messagingState;
    }

    public void setRelationId(UserRelationId relationId) {
        this.relationId = relationId;
    }

    public void setRelationState(RelationState relationState) {
        this.relationState = relationState;
    }

    public void setMessagingState(MessagingState messagingState) {
        this.messagingState = messagingState;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof UserRelation)) return false;
        final UserRelation other = (UserRelation) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$relationId = this.getRelationId();
        final Object other$relationId = other.getRelationId();
        if (this$relationId == null ? other$relationId != null : !this$relationId.equals(other$relationId))
            return false;
        final Object this$relationState = this.getRelationState();
        final Object other$relationState = other.getRelationState();
        if (this$relationState == null ? other$relationState != null : !this$relationState.equals(other$relationState))
            return false;
        final Object this$messagingState = this.getMessagingState();
        final Object other$messagingState = other.getMessagingState();
        if (this$messagingState == null ? other$messagingState != null : !this$messagingState.equals(other$messagingState))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof UserRelation;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $relationId = this.getRelationId();
        result = result * PRIME + ($relationId == null ? 43 : $relationId.hashCode());
        final Object $relationState = this.getRelationState();
        result = result * PRIME + ($relationState == null ? 43 : $relationState.hashCode());
        final Object $messagingState = this.getMessagingState();
        result = result * PRIME + ($messagingState == null ? 43 : $messagingState.hashCode());
        return result;
    }

    public String toString() {
        return "UserRelation(relationId=" + this.getRelationId() + ", relationState=" + this.getRelationState() + ", messagingState=" + this.getMessagingState() + ")";
    }
}
