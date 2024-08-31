package org.vaadin.crm.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.vaadin.crm.entities.User;


import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u " +
            "where lower(u.userName) like lower(concat('%', :searchTerm, '%')) ")
    List<User> search(@Param("searchTerm") String searchTerm);
}
