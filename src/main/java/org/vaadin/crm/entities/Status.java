package org.vaadin.crm.entities;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

@Entity
public class Status extends AbstractEntity {
    private String name;

    public Status(String name) {
        this.name = name;
    }
}
