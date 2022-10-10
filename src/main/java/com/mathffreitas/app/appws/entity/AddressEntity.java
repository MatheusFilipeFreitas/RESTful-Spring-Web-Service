package com.mathffreitas.app.appws.entity;

import com.mathffreitas.app.appws.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

@Entity(name="addresses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 7809200551672852690L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false)
    private String addressId;

    @Column(length = 30, nullable = false)
    private String city;

    @Column(length = 25, nullable = false)
    private String country;

    @Column(length = 100, nullable = false)
    private String streetName;

    @Column(length = 8, nullable = false)
    private String postalCode;

    @Column(length = 25, nullable = false)
    private String type;

    // (parent -> child) PERSIST = save; MERGE = update; REMOVE = delete; REFRESH = refresh from database; DETACH = remove from persistent context;
    @ManyToOne
    @JoinColumn(name = "users_id")
    private UserEntity userDetails;

}
