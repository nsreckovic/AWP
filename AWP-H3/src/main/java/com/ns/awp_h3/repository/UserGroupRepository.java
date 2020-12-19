package com.ns.awp_h3.repository;

import com.ns.awp_h3.models.UserGroup;
import org.springframework.data.repository.CrudRepository;

public interface UserGroupRepository extends CrudRepository<UserGroup, Integer> {
    boolean existsByName(String name);
}
