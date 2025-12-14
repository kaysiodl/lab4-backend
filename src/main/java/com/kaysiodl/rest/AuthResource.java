package com.kaysiodl.rest;

import com.kaysiodl.dto.AuthRequest;
import com.kaysiodl.service.AuthService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    AuthService authService;

    @POST
    @Path("/register")
    public Response register(AuthRequest request) {
        authService.register(request.getLogin(), request.getPassword());
        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Path("/login")
    public Response login(AuthRequest request) {
        String sessionId = authService.login(
                request.getLogin(),
                request.getPassword()
        );

        return Response.ok()
                .entity(Map.of("sessionId", sessionId))
                .build();
    }

    @POST
    @Path("/logout")
    public Response logout(
            @HeaderParam("X-Session-Id") String sessionId
    ) {
        authService.logout(sessionId);
        return Response.noContent().build();
    }
}
