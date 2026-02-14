package com.resources;

import com.domain.usecase.categories.ICategoryListUseCase;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/categories")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Categories {
  @Inject
  private ICategoryListUseCase categoryListUseCase;

  @GET
  public Response listCategories(@QueryParam("conceptId") Long conceptId,
      @QueryParam("page") Integer page,
      @QueryParam("pageSize") Integer pageSize) {
    int p = page != null ? page : 1;
    int ps = pageSize != null ? pageSize : 10;
    return Response.ok(categoryListUseCase.listCategories(conceptId, p, ps)).build();
  }
}
