package com.troblecodings.signals.cctweaked;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class LuaFunction {

    String name = null;
    Method method = null;
    List<Class> parameterTypes = new ArrayList<>();
    
    public LuaFunction(Method method, List<Class> parameterTypes) {
        this.method = method;
        this.name = method.getName();
        this.parameterTypes = parameterTypes;
    }
}
