package com.troblecodings.signals.cctweaked;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface Driver {
    
    public List<LuaFunction> functions = new ArrayList<>();
    public List<String> functionNames = new ArrayList<>();

    default void initDriver() {
        for(Method m: this.getClass().getDeclaredMethods()) {
            if(!m.isAnnotationPresent(CCFunction.class))
                continue;

            CCFunction annotation = m.getAnnotationsByType(CCFunction.class)[0];

            List<Class> parameterTypes = Arrays.asList(annotation.parameters());

            functions.add(new LuaFunction(m, parameterTypes));
            functionNames.add(m.getName());
        }
    }
}
