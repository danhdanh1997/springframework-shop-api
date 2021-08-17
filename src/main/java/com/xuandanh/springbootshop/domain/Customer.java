package com.xuandanh.springbootshop.domain;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer")
public class Customer  implements Serializable {
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "CHAR(32)",name = "customer_id")
    @Id
    private String customerId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "active")
    private boolean active;

    @CreatedDate
    @Column(name = "create_date")
    private Instant createDate  = Instant.now();

    @Column(name = "last_update")
    private Instant lastUpdate  = Instant.now();

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "address_id",referencedColumnName = "address_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "store_id",referencedColumnName = "store_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Store store;

    @OneToMany(mappedBy="customer")
    private List<Payment> payments ;

    @OneToOne(fetch = FetchType.LAZY,
            cascade={CascadeType.ALL,CascadeType.REMOVE},
            mappedBy = "customer")
    private Rental rental;
}
