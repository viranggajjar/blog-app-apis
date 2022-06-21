package com.blogapi.blogappapis.repositories;

import com.blogapi.blogappapis.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Integer> {
}
