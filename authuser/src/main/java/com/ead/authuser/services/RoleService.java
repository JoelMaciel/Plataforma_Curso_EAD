package com.ead.authuser.services;

import com.ead.authuser.enums.RoleType;
import com.ead.authuser.models.RoleModel;
import com.ead.authuser.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public interface RoleService {

    Optional<RoleModel> findByRoleName(RoleType roleType);

}
