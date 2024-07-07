package org.vaadin.crm.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.crm.entities.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
