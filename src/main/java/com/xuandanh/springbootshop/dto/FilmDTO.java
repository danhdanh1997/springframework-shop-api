package com.xuandanh.springbootshop.dto;

import com.xuandanh.springbootshop.domain.Language;
import lombok.*;
import java.time.Instant;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilmDTO {
    private String filmId;
    private String title;
    private String description;
    private Instant releaseYear;
    private int rentalDuration;
    private Long rentalRate;
    private Long replacementCost;
    private int rating;
    private Instant lastUpdate;
    private String specialFeature;
    private String imageUrl;
    private Language language;
}
