package com.devglan.rolebasedoauth2.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "ROLES")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "NAME")
    private RoleType name;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "CREATED_ON")
    private Long createdOn;
    @Column(name = "MODIFIED_ON")
    private Long modifiedOn;

}
