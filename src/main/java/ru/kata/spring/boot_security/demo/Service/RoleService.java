package ru.kata.spring.boot_security.demo.Service;

import ru.kata.spring.boot_security.demo.model.Role;


public interface RoleService {

    void add(Role role);

    Role findByRoleName(String name);


}
