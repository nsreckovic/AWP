package com.ns.awp_h3.repository;

import com.ns.awp_h3.models.UserType;
import org.springframework.data.repository.CrudRepository;

public interface UserTypeRepository extends CrudRepository<UserType, Integer> {
    boolean existsByName(String name);
}
