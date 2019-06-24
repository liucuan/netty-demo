package com.tone.netty;

import com.sun.btrace.annotations.*;

import static com.sun.btrace.BTraceUtils.*;

import java.util.HashMap;

@BTrace
public class BTraceField {
    @OnMethod(clazz = "java.util.HashMap", method = "put")
    public static void m(@Self HashMap map, Object Key, Object value) { // all calls to the methods with signature "()"
        println("====================");
        print(Key);
        print(":");
        println(value);
    }

    @OnMethod(clazz = "com.tone.netty.TestHello", method = "test")
    public static void d(@Self Object obj, int age) {
        print("test method is called, input age=");
        println(age);
    }
}
