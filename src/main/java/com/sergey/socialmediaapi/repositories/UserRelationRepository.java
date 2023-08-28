package com.sergey.socialmediaapi.repositories;

import com.sergey.socialmediaapi.domain.model.UserRelation;
import com.sergey.socialmediaapi.domain.model.UserRelationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRelationRepository extends JpaRepository<UserRelation, UserRelationId> {
}
