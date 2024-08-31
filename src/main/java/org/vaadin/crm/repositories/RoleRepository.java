package org.vaadin.crm.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.crm.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
