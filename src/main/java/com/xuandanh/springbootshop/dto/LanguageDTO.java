package com.xuandanh.springbootshop.dto;

import lombok.*;
import java.time.Instant;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LanguageDTO {
    private int languageId;
    private String languageName;
    private Instant lastUpdate;
}
