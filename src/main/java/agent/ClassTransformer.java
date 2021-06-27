package agent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import jmx.Input;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class ClassTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {

        System.out.println(className);
        try {
            if (className.equals("jmx/Tasks") && Input.isProfiling) {
                ClassPool classPool = new ClassPool();
                classPool.appendClassPath(new LoaderClassPath(loader));
                CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));
                CtMethod[] methods = ctClass.getMethods();

                for (CtMethod method : methods) {
                    if (method.getName().equals("run")) {
                        System.out.println("Entering " + method.getName() + " of " + className);
                        method.addLocalVariable("elapsedTime", CtClass.longType);
                        method.insertBefore("elapsedTime = System.currentTimeMillis();");
                        method.insertAfter("{elapsedTime = System.currentTimeMillis() - elapsedTime;"
                                + "System.out.println(\"[profiling]Execution time was: \" + elapsedTime);}");
                    }
                }
                return ctClass.toBytecode();
            } else {
                return classfileBuffer;
            }
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
