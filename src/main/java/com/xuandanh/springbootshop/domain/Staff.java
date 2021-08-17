package com.xuandanh.springbootshop.domain;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "staff")
public class Staff {
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "CHAR(32)",name = "staff_id")
    @Id
    private String staffId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "active")
    private boolean active;

    @Column(name = "last_update")
    private Instant lastUpdate = Instant.now();

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @NotNull
    @JoinColumn(name = "store_id",referencedColumnName = "store_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "address_id",referencedColumnName = "address_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Address address;

    @OneToMany(mappedBy="staff",cascade={CascadeType.ALL,CascadeType.REMOVE})
    private List<Rental> rentals;

    @OneToMany(mappedBy="staff",cascade={CascadeType.ALL,CascadeType.REMOVE})
    private List<Payment>payments ;
}
