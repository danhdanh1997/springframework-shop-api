package com.xuandanh.springbootshop.domain;

import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Data
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "film_actor")
public class Film_Actor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "film_id", referencedColumnName = "film_id")
    private Film film;

    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "actor_id", referencedColumnName = "actor_id")
    private Actor actor;

    @Column(name = "last_update")
    private Instant lastUpdate;

}
