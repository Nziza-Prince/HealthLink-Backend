package com.healthlinkteam.healthlink.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "managers")
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("MANAGER")
public class Manager extends User{

    @ManyToOne
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
