# demo-helloworld-lambda

Demo AWS lambda using a java17.

This is broken on ARM64 and opened up for debugging.

### Steps to reproduce breakage on ARM64:

1. Build `jar` files

```
mvn package -DskipTests
```

1. Build container

```bash
docker build --tag hello-world-lambda --file deployment/Dockerfile .
```

1. Install AWS lambda RIE. If running on amd64 processor, change the ARCH var appropriately

```bash
mkdir -p ~/.aws-lambda-rie && curl -Lo ~/.aws-lambda-rie/aws-lambda-rie \
https://github.com/aws/aws-lambda-runtime-interface-emulator/releases/latest/download/aws-lambda-rie-arm64 \
&& chmod +x ~/.aws-lambda-rie/aws-lambda-rie
```

1. Run container as lambda (will hold terminal)

```bash
docker run -v ~/.aws-lambda-rie:/aws-lambda -p 9000:8080 \
  --entrypoint /aws-lambda/aws-lambda-rie \
  hello-world-lambda:latest \
  ./entrypoint.sh
```

1. Sample curl request (in another terminal)

```bash
curl -XPOST "http://localhost:9000/2015-03-31/functions/function/invocations" -d '{}'
```

Expected output: "Hello world". Actual output: <nothing>.

docker logs show:

```
25 Feb 2022 10:24:09,764 [INFO] (rapid) extensionsDisabledByLayer(/opt/disable-extensions-jwigqn8j) -> stat /opt/disable-extensions-jwigqn8j: no such file or directory
25 Feb 2022 10:24:09,764 [WARNING] (rapid) Cannot list external agents error=open /opt/extensions: no such file or directory
Failed to load the native runtime interface client library aws-lambda-runtime-interface-client.glibc.so. Exception: /tmp/.aws-lambda-runtime-interface-client: /tmp/.aws-lambda-runtime-interface-client: cannot open shared object file: No such file or directory (Possible cause: can't load AMD 64 .so on a AARCH64 platform)
Failed to load the native runtime interface client library aws-lambda-runtime-interface-client.musl.so. Exception: /tmp/.aws-lambda-runtime-interface-client: /tmp/.aws-lambda-runtime-interface-client: cannot open shared object file: No such file or directory (Possible cause: can't load AMD 64 .so on a AARCH64 platform)
25 Feb 2022 10:24:09,934 [WARNING] (rapid) First fatal error stored in appctx: Runtime.ExitError
25 Feb 2022 10:24:09,934 [WARNING] (rapid) Process 15(entrypoint.sh) exited: Runtime exited with error: exit status 255
25 Feb 2022 10:24:09,934 [ERROR] (rapid) Init failed error=Runtime exited with error: exit status 255 InvokeID=
25 Feb 2022 10:24:09,934 [WARNING] (rapid) Reset initiated: ReserveFail
25 Feb 2022 10:24:09,934 [WARNING] (rapid) Cannot list external agents error=open /opt/extensions: no such file or directory
Failed to load the native runtime interface client library aws-lambda-runtime-interface-client.glibc.so. Exception: /tmp/.aws-lambda-runtime-interface-client: /tmp/.aws-lambda-runtime-interface-client: cannot open shared object file: No such file or directory (Possible cause: can't load AMD 64 .so on a AARCH64 platform)
Failed to load the native runtime interface client library aws-lambda-runtime-interface-client.musl.so. Exception: /tmp/.aws-lambda-runtime-interface-client: /tmp/.aws-lambda-runtime-interface-client: cannot open shared object file: No such file or directory (Possible cause: can't load AMD 64 .so on a AARCH64 platform)
25 Feb 2022 10:24:10,035 [WARNING] (rapid) First fatal error stored in appctx: Runtime.ExitError
25 Feb 2022 10:24:10,035 [WARNING] (rapid) Process 36(entrypoint.sh) exited: Runtime exited with error: exit status 255
```

None of the log output from the lambda itself are shown anywhere, leading to think it isnt actually run.
