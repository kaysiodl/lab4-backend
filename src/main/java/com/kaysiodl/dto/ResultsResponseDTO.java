package com.kaysiodl.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResultsResponseDTO {
    private Long id;
    private double x;
    private double y;
    private double r;
    private boolean hit;
    private String currentTime;
    private String executionTime;
}
