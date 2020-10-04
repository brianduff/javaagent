Example of a simple java agent.

## Building the code

The example uses [bazel](https://bazel.build) to build. Install it first. On Mac with [homebrew](https://brew.sh/), you can just `brew install bazel`.

You can compile the two deploy jars using:

```
bazel build src/main/java/org/dubh/examples/agent/target/Target_deploy.jar \
  src/main/java/org/dubh/examples/agent:agent_deploy.jar
```

Then you can run just the program without the agent via:

```
java -jar bazel-bin/src/main/java/org/dubh/examples/agent/target/Target_deploy.jar
```

Or with the agent, via:

```
java -javaagent:bazel-bin/src/main/java/org/dubh/examples/agent/agent_deploy.jar \
  -jar bazel-bin/src/main/java/org/dubh/examples/agent/target/Target_deploy.jar
```

Hope this is useful :)
