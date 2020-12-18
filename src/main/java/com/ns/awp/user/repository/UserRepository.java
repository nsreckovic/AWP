package com.ns.awp.user.repository;

import com.ns.awp.user.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    boolean existsByUsername(String username);

    User findByUsername(String username);
}
