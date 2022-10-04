package com.mathffreitas.app.appws.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "authorities")
public class AuthorityEntity implements Serializable {


    private static final long serialVersionUID = 1941589331329909250L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    
}
