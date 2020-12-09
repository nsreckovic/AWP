package com.ns.awp_h3.repository;

import com.ns.awp_h3.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, Integer> {
    boolean existsByUsername(String username);

    @Query("SELECT u FROM User u " +
            "JOIN u.userType ut " +
            "JOIN u.userGroup ug " +
            "WHERE " +
            "(:username IS NULL OR u.username LIKE CONCAT('%',:username,'%')) AND " +
            "(:name IS NULL OR u.name LIKE CONCAT('%',:name,'%')) AND " +
            "(:lastName IS NULL OR u.lastName LIKE CONCAT('%',:lastName,'%')) AND " +
            "(:userType IS NULL OR ut.name LIKE CONCAT('%',:userType,'%')) AND " +
            "(:userGroup IS NULL OR ug.name LIKE CONCAT('%',:userGroup,'%'))")
    Iterable<User> searchUsers(@Param("username") String username,
                     @Param("name") String name,
                     @Param("lastName") String lastName,
                     @Param("userType") String userType,
                     @Param("userGroup") String userGroup);

    User findByUsername(String username);
}
