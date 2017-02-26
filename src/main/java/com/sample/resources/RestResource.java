package com.sample.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

@Component
@Produces(MediaType.APPLICATION_JSON)
@Path("/api")
public class RestResource {

  @GET
  @Path("sqs")
  public String getUserProjects() {
    return "test";
  }
}
