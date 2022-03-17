FROM public.ecr.aws/lambda/java:11

WORKDIR /app

COPY target/classes /var/task/
COPY target/dependency/* /var/task/lib/

CMD ["com.example.helloworld.HelloWorldLambda::handleRequest"]
