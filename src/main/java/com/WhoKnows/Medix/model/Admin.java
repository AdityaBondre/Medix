package com.WhoKnows.Medix.model;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class Admin extends User{
    private String passkey;
}
