package org.vaadin.crm.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.vaadin.crm.entities.Company;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    @Query("select c from Company c " +
            "where lower(c.companyName) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(c.email) like lower(concat('%', :searchTerm, '%'))")
    List<Company> search(@Param("searchTerm") String searchTerm);
}
