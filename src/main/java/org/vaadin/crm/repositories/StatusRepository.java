package org.vaadin.crm.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.crm.entities.Status;

public interface StatusRepository extends JpaRepository<Status, Long> {

}
