package com.ns.awp_h3.repository;

import com.ns.awp_h3.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
}
