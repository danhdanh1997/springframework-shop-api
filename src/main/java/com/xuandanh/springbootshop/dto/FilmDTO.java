package com.xuandanh.springbootshop.dto;

import lombok.*;

import java.time.Instant;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilmDTO {
    private String filmId;
    private String filmName;
    private String title;
    private String description;
    private Instant releaseYear;
    private String rentalDuration;
    private String rentalRate;
    private String replacementCost;
    private String rating;
    private Instant lastUpdate;
    private String specialFeature;
    private String imageUrl;
    private int language_id;
}
