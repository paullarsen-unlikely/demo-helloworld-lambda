#!/usr/bin/env bash

java \
  --add-opens java.base/java.util=ALL-UNNAMED \
  -cp "app.jar" \
  com.amazonaws.services.lambda.runtime.api.client.AWSLambda \
  ai.unlikely.helloworld.HelloWorldService
