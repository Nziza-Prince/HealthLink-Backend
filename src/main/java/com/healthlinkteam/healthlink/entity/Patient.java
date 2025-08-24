package com.healthlinkteam.healthlink.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
@Entity
@DiscriminatorValue("Patient")
public class Patient extends User{

}
