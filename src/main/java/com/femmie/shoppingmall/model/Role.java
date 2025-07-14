package com.femmie.shoppingmall.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Role {
    @Id
    private Long id;

    private String name; // e.g. "ROLE_USER"
}