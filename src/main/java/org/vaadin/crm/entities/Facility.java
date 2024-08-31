package org.vaadin.crm.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor

@Entity
public class Facility implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgenerator")
    // The initial value is to account for data.sql demo data ids
    //@SequenceGenerator(name = "idgenerator", initialValue = 1000)
    private Long id;

    private LocalDateTime dateTime;

    @NotEmpty
    private String facilityName = "";

    private String area;

    private String address;

    private String price;

    private String owner;

    private String facilityReadiness;

    @NotNull
    @ManyToOne
    private Status status;

    //@Size(max = 5000)
    @Column(length = 5000)
    private String description = "";

    //@NotEmpty
    private String contactLinks = "";

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
}