package com.kaysiodl.rest;

import com.kaysiodl.database.Result;
import com.kaysiodl.database.User;
import com.kaysiodl.dto.ResultsRequestDTO;
import com.kaysiodl.dto.ResultsResponseDTO;
import com.kaysiodl.service.AuthService;
import com.kaysiodl.service.ResultsService;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.stream.Collectors;

@Path("/check")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ResultsResource {
    @EJB
    private ResultsService resultsService;

    @EJB
    private AuthService authService;

    @POST
    public ResultsResponseDTO add(
            ResultsRequestDTO dto,
            @HeaderParam("X-Session-Id") String sessionId
    ) {
        User user = authService.getUserBySession(sessionId);
        Result result = resultsService.save(
                dto.getX(), dto.getY(), dto.getR(), user
        );
        return toDto(result);
    }

    @GET
    public List<ResultsResponseDTO> getAll(
            @HeaderParam("X-Session-Id") String sessionId
    ) {
        User user = authService.getUserBySession(sessionId);
        return resultsService.findByUser(user)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private ResultsResponseDTO toDto(Result result) {
        return ResultsResponseDTO
                .builder()
                .x(result.getX())
                .y(result.getY())
                .r(result.getR())
                .hit(result.isHit())
                .currentTime(result.getCurrentTime())
                .executionTime(result.getExecutionTime())
                .build();
    }


}
