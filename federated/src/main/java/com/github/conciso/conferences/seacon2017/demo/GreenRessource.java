package com.github.conciso.conferences.seacon2017.demo;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;

import java.io.IOException;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

@Path("/green")
@ApplicationScoped
public class GreenRessource {

  private static final Logger LOG = Logger.getAnonymousLogger();

  @Context
  SecurityContext sc;

  @HeaderParam(AUTHORIZATION)
  String authHeader;

  @GET
  @Produces("text/plain")
  public String get() throws IOException {
    String red = call("red");
    String blue = call("blue");
    LOG.info("green");
    return "green " + red + "|" + blue;
  }

  private String call(String color) throws IOException {
    try(CloseableHttpClient httpclient = HttpClients.createDefault()) {
      HttpGet get = new HttpGet("http://federated:8080/" + color);
      get.addHeader(AUTHORIZATION, authHeader);
      try(CloseableHttpResponse response = httpclient.execute(get)) {
        return EntityUtils.toString(response.getEntity());
      }
    }
  }

}
