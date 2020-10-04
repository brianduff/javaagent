java_library(
    name = "lib",
    srcs = glob(["*.java"]),
)

java_binary(
    name ="Target",
    runtime_deps = [ ":lib" ],
    main_class = "org.dubh.examples.agent.target.Main",
)
