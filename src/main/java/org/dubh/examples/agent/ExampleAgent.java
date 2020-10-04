package org.dubh.examples.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class ExampleAgent {

  public static void premain(String agentArgs, Instrumentation inst) {
    if (!inst.isRedefineClassesSupported()) {
      System.err.println("ExampleAgent: not allowed to redefine classes. Bailing!");
      return;
    }

    inst.addTransformer(new ClassFileTransformer() {
      @Override
      public byte[] transform(ClassLoader loader, String className, Class<?> oldClazz, ProtectionDomain domain,
          byte[] classfileBuffer) {

        if ("org/dubh/examples/agent/target/Greeter".equals(className)) {
          return transformClass(classfileBuffer);
        }

        return null;
      }
    });
  }

  private static byte[] transformClass(byte[] classfileBuffer) {
    ClassReader reader = new ClassReader(classfileBuffer);
    ClassNode classNode = new ClassNode();
    reader.accept(classNode, Opcodes.ASM8);
    for (MethodNode method : classNode.methods) {
      if ("getName".equals(method.name)) {
        InsnList instructions = new InsnList();
        instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "org/dubh/examples/agent/NewGreeter", "getName",
            "()Ljava/lang/String;"));
        instructions.add(new InsnNode(Opcodes.ARETURN));
        method.instructions = instructions;
      }
    }

    ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
    classNode.accept(writer);

    return writer.toByteArray();
  }

}