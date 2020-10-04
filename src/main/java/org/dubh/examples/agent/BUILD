java_library(
    name = "agent_lib",
    srcs = glob(["*.java"]),
    deps = [
        "@maven//:org_ow2_asm_asm",
        "@maven//:org_ow2_asm_asm_tree"
    ]
)

java_binary(
    name ="agent",
    runtime_deps = [ ":agent_lib" ],
    main_class = "org.dubh.examples.agent.ExampleAgent",
    deploy_manifest_lines = [
        "Premain-Class: org.dubh.examples.agent.ExampleAgent",
        "Agent-Class: org.dubh.examples.agent.ExampleAgent",
        "Can-Redefine-Classes: true",
        "Can-Retransform-Classes: true",
    ]
)

