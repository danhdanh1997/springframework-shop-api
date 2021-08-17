package com.xuandanh.springbootshop.domain;

import lombok.*;
import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "language")
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="language_id")
    private int languageId;

    @Column(name = "language_name")
    private String languageName;

    @Column(name = "last_update")
    private Instant lastUpdate  = Instant.now();

    @OneToMany(mappedBy="language",cascade={CascadeType.ALL,CascadeType.REMOVE})
    private List<Film> films ;

}
