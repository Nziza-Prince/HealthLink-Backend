package com.healthlinkteam.healthlink.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity(name = "patients")
@DiscriminatorValue("PATIENT")
public class Patient extends User{
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
