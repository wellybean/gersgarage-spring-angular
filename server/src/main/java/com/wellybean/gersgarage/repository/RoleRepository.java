package com.wellybean.gersgarage.repository;

import java.util.Optional;
import com.wellybean.gersgarage.model.ERole;
import com.wellybean.gersgarage.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long>{
    Optional<Role> findByName(ERole name);
}
