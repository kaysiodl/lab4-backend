package com.kaysiodl.rest;

import com.kaysiodl.database.Result;
import com.kaysiodl.database.User;
import com.kaysiodl.dto.ResultsRequestDTO;
import com.kaysiodl.dto.ResultsResponseDTO;
import com.kaysiodl.service.AuthService;
import com.kaysiodl.service.ResultsService;
import com.kaysiodl.utils.ValidationUtil;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/check")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ResultsResource {
    @Inject
    private ResultsService resultsService;

    @Inject
    private AuthService authService;

    @POST
    public ResultsResponseDTO add(
            ResultsRequestDTO dto,
            @HeaderParam("X-Session-Id") String sessionId
    ) {
        if (sessionId == null || sessionId.isBlank()) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        try {
            ValidationUtil.validatePoint(dto);
        } catch (IllegalArgumentException e) {
            throw new WebApplicationException(e.getMessage(), Response.Status.BAD_REQUEST);
        }
        User user;
        try {
            user = authService.getUserBySession(sessionId);
        } catch (Exception e) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        Result result = resultsService.save(
                dto.getX(), dto.getY(), dto.getR(), user
        );
        return toDto(result);
    }


    @GET
    public List<ResultsResponseDTO> getUserResults(
            @HeaderParam("X-Session-Id") String sessionId
    ) {
        User user = authService.getUserBySession(sessionId);
        return resultsService.findByUser(user)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @DELETE
    public void clearUserPoints(@HeaderParam("X-Session-Id") String sessionId) {
        User user = authService.getUserBySession(sessionId);
        resultsService.deleteByUser(user);
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
