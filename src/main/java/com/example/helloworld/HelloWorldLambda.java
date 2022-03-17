package com.example.helloworld;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.vavr.gson.VavrGson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorldLambda implements RequestStreamHandler {
  private static final Logger logger = LoggerFactory.getLogger(HelloWorldLambda.class);

  private final Gson gson;

  public static void main(String[] args) {
    new HelloWorldLambda();
  }

  public HelloWorldLambda() {
    logger.info("starting lambda");

    GsonBuilder gsonBuilder = new GsonBuilder();
    VavrGson.registerAll(gsonBuilder);
    this.gson = gsonBuilder.create();
  }

  @Override
  public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
      throws IOException {
    try (BufferedReader reader =
            new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.US_ASCII));
         PrintWriter writer =
            new PrintWriter(
                new BufferedWriter(
                    new OutputStreamWriter(outputStream, StandardCharsets.US_ASCII)))) {

      writer.write(gson.toJson("Hello world"));
      if (writer.checkError()) {
        logger.warn("Warning: Writer encountered an error");
      }
    }
  }
}
