package org.vaadin.crm.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.vaadin.crm.entities.Facility;

import java.util.List;

public interface FacilityRepository extends JpaRepository<Facility, Long> {

    @Query("select f from Facility f " +
            "where lower(f.facilityName) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(f.owner) like lower(concat('%', :searchTerm, '%'))")
    List<Facility> search(@Param("searchTerm") String searchTerm);
}
