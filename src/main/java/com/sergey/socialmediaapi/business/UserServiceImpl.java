package com.sergey.socialmediaapi.business;

import com.sergey.socialmediaapi.domain.model.*;
import com.sergey.socialmediaapi.repositories.UserRelationRepository;
import com.sergey.socialmediaapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserRelationRepository userRelationRepository;

    @Override
    public void friendRequest(UUID id, UserDetails userDetails) {
        User requestedUser = getRequestedUserFromRepo(id);
        User requestingUser = getRequestingUserFromRepoByUserDetails(userDetails);

        UserRelationId newUserRelationId = new UserRelationId(requestingUser, requestedUser);
        log.info("request friend - UserRelationId: " + newUserRelationId);
        boolean isRelationExist = userRelationRepository.existsById(newUserRelationId);

        if (isRelationExist) {
            log.error("You already have relationship with: " + requestingUser.getEmail());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You already have relationship with this " + id + " user");
        }
        UserRelation userRelation = new UserRelation();
        userRelation.setRelationId(newUserRelationId);
        userRelation.setRelationState(RelationState.PENDING_FIRST_SECOND);
        userRelationRepository.save(userRelation);

    }

    private User getRequestingUserFromRepoByUserDetails(UserDetails userService) {
        return userRepository
                .findUserByEmail(userService.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
    }

    private User getRequestedUserFromRepo(UUID id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "requested user" + id + " doesn't exist"));
    }


    @Override
    public void deleteFriend(UUID id, UserDetails userDetails) {
        User requestingUser = getRequestingUserFromRepoByUserDetails(userDetails);
        User requestedUser = getRequestedUserFromRepo(id);
        UserRelationId userRelationId = new UserRelationId(requestingUser, requestedUser);
        log.info("delete friend - UserRelationId: " + userRelationId);
        UserRelation userRelation = getRelationship(userRelationId);
        if (userRelation.getRelationState().equals(RelationState.FRIENDS)) {
            if (userRelation.getRelationId().getRelatingUser().equals(requestingUser)) {
                userRelation.setRelationState(RelationState.SUBSCRIBE_SECOND_FIRST);
            } else {
                userRelation.setRelationState(RelationState.SUBSCRIBE_FIRST_SECOND);
            }
            userRelationRepository.save(userRelation);
            log.info("Friend deleted, user relation: ");
        } else {
            log.info("You don't have this friend");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "you don't have this friend");
        }

    }


    private UserRelation getRelationship(UserRelationId userRelationId) {
        Optional<UserRelation> userRelationById = userRelationRepository.findById(userRelationId);
        if (userRelationById.isPresent()) {
            log.info("User relation" + userRelationById.get());
            return userRelationById.get();
        } else {
            UserRelationId userRelationIdReversed = new UserRelationId(userRelationId.getRelatedUser(), userRelationId.getRelatingUser());
            return userRelationRepository.findById(userRelationIdReversed)
                    .orElseThrow(() -> {
                        log.error("no such relationship");
                        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "no such relationship");

                    });
        }

    }
    //    todo add ability to decline
    @Override
    public void acceptFriend(UUID id, UserDetails userDetails) {
        User requestingUser = getRequestingUserFromRepoByUserDetails(userDetails);
        User requestedUser = getRequestedUserFromRepo(id);

        UserRelationId userRelationId = new UserRelationId(requestedUser, requestingUser);
        log.info("accept friend - UserRelationId: " + userRelationId);
        UserRelation userRelation = getRelationship(userRelationId);
        if (userRelation.getRelationState().equals(RelationState.PENDING_FIRST_SECOND) ||
        userRelation.getRelationState().equals(RelationState.SUBSCRIBE_FIRST_SECOND)) {
            userRelation.setRelationState(RelationState.FRIENDS);
            userRelationRepository.save(userRelation);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void messageFriend(UUID id, UserDetails userDetails) {
        User requestingUser = getRequestingUserFromRepoByUserDetails(userDetails);
        User requestedUser = getRequestedUserFromRepo(id);
        UserRelationId userRelationId = new UserRelationId(requestedUser, requestingUser);
        UserRelation userRelation = getRelationship(userRelationId);
        if (userRelation.getRelationState().equals(RelationState.FRIENDS) &&
        userRelation.getMessagingState().equals(MessagingState.NOT_ALLOWED)) {
            userRelation.setMessagingState(MessagingState.PENDING_FIRST_SECOND);
            userRelationRepository.save(userRelation);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "messaging not allowed");
        }

    }
}
