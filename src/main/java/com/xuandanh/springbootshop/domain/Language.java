package com.xuandanh.springbootshop.domain;

import lombok.*;
import javax.persistence.*;
import java.time.Instant;
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
    private Instant lastUpdate;

}
