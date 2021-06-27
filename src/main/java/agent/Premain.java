package agent;

import java.lang.instrument.Instrumentation;

public class Premain {
    public static void premain(String args, Instrumentation instrumentation) {
        instrumentation.addTransformer(new ClassTransformer());
    }
}
