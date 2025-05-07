package com.example.twitter.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetweetRequestDTO {
    @NotNull(message = "Original tweet ID is required")
    private Long originalTweetId;

    private String additionalContent;
}