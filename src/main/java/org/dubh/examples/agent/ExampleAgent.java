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
    // ClassReader knows how to grok the buffer of bytes as a Java class.
    ClassReader reader = new ClassReader(classfileBuffer);

    // ClassNode is a visitor over the things in the classfile that collects
    // them into an in-memory data structure that we can easily traverse. You
    // can also avoid creating a separate in-memory representation by just
    // implementing a simple ClassVisitor, but it often requires more code.
    ClassNode classNode = new ClassNode();
    reader.accept(classNode, Opcodes.ASM8);

    // Now ClassNode contains a data strcuture with all the things in the
    // class, and we can look through the methods for the one we care about.
    for (MethodNode method : classNode.methods) {
      // You'd maybe want to check the signature also in a real program.
      if ("getName".equals(method.name)) {
        // Method bodies contain instruction lists. Here, we create a simple
        // instruction list with two instructions - one to call a static 
        // method, and another to return whatever that static method returned.
        InsnList instructions = new InsnList();
        instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, 
            "org/dubh/examples/agent/NewGreeter", "getNewName",
            "()Ljava/lang/String;"));
        instructions.add(new InsnNode(Opcodes.ARETURN));

        // This replaces the existing instruction list of the method with our
        // new instruction list.
        method.instructions = instructions;
      }
    }

    // ClassWriter is a visitor that knows how to traverse the data structure,
    // and write back out the bytes of a class.
    ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
    classNode.accept(writer);

    return writer.toByteArray();
  }

}