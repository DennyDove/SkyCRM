package org.vaadin.crm.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.crm.entities.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {

}
