package com.xuandanh.springbootshop.dto;

import lombok.*;
import java.time.Instant;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActorDTO {
    private String actorId;
    private String firstName;
    private String lastName;
    private Instant lastUpdate;
}
