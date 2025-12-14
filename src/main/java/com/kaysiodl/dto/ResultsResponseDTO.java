package com.kaysiodl.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResultsResponseDTO {
    double x;
    double y;
    double r;
    boolean hit;
    private String currentTime;
    private String executionTime;
}
