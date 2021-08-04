package com.xuandanh.springbootshop.dto;

import com.xuandanh.springbootshop.domain.Actor;
import com.xuandanh.springbootshop.domain.Film;
import lombok.*;
import java.time.Instant;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilmActorDTO {
    private Film film;
    private Actor actor;
    private Instant lastUpdate;
}
