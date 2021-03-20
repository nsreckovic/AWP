package com.ns.awp.user.repository;

import com.ns.awp.user.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    @Query("SELECT u FROM User u ORDER BY u.username ASC")
    Iterable<User> findAllSorted();

    boolean existsByUsername(String username);

    User findByUsername(String username);
}
