package com.ns.awp.userType.repository;

import com.ns.awp.userType.models.UserType;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserTypeRepository extends CrudRepository<UserType, Integer> {
    boolean existsByName(String name);
    Optional<UserType> findByName(String name);
}
