package com.puppy.pawpaw_project_be.domain.user.domain.repository;

import com.puppy.pawpaw_project_be.domain.auth.domain.OAuth2Provider;
import com.puppy.pawpaw_project_be.domain.user.domain.Role;
import com.puppy.pawpaw_project_be.domain.user.domain.User;
import com.puppy.pawpaw_project_be.domain.user.domain.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, UserId> {
    boolean existsById(final String id);
    boolean existsByUserIdAndRole(final UserId userId, final Role role);
    Optional<User> findById(final String id);

    Optional<User> findByIdAndProvider(
        final String id,
        final OAuth2Provider provider
    );
}
