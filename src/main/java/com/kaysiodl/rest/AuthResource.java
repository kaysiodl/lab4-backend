package com.kaysiodl.rest;

import com.kaysiodl.dto.AuthRequest;
import com.kaysiodl.service.AuthService;
import com.kaysiodl.utils.ValidationUtil;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.java.Log;

import java.util.Map;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Log
public class AuthResource {

    @Inject
    AuthService authService;

    @POST
    @Path("/register")
    public Response register(AuthRequest request) {
        try {
            authService.register(request.getLogin(), request.getPassword());
            String sessionId = generateSessionId(request);
            return Response
                    .status(Response.Status.CREATED)
                    .entity(Map.of("sessionId", sessionId))
                    .build();
        } catch (RuntimeException e) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
    }

    @POST
    @Path("/login")
    public Response login(AuthRequest request) {
        try {
            ValidationUtil.validateUser(
                    request.getLogin(),
                    request.getPassword()
            );
            String sessionId = generateSessionId(request);
            return Response.ok()
                    .entity(Map.of("sessionId", sessionId))
                    .build();
        } catch (RuntimeException e) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
    }

    @POST
    @Path("/logout")
    public Response logout(
            @HeaderParam("X-Session-Id") String sessionId
    ) {
        authService.logout(sessionId);
        return Response.noContent().build();
    }

    private String generateSessionId(AuthRequest request) {
        return authService.login(
                request.getLogin(),
                request.getPassword()
        );
    }
}
