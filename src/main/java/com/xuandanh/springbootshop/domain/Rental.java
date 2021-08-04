package com.xuandanh.springbootshop.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "rental")
public class Rental {
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "CHAR(32)",name = "rental_id")
    @Id
    private String rentalId;

    @CreatedDate
    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "return_date")
    private Instant returnDate;

    @Column(name = "last_update")
    private Instant lastUpdate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "inventories_id", referencedColumnName = "inventories_id")
    private Inventory inventory;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private Customer customer;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "staff_id",referencedColumnName = "staff_id")
    private Staff staff;

    @OneToMany(mappedBy="rental",cascade=CascadeType.ALL)
    private List<Payment> payments ;
}
