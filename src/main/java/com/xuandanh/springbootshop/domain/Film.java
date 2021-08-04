package com.xuandanh.springbootshop.domain;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "film")
public class Film {
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "CHAR(32)",name = "film_id")
    @Id
    private String filmId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "release_year")
    private Instant releaseYear;

    @Column(name = "rental_duration")
    private int rentalDuration;

    @Column(name = "rental_rate")
    private Long rentalRate;

    @Column(name = "replacement_cost")
    private Long replacementCost;

    @Column(name = "rating")
    private int rating;

    @Column(name = "last_update")
    private Instant lastUpdate;

    @Column(name = "special_feature")
    private String specialFeature;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "language_id",referencedColumnName = "language_id")
    private Language language;

    @OneToMany(mappedBy="film",cascade=CascadeType.ALL)
    private List<Inventory> inventories;

}
