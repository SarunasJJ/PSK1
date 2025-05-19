package org.example.demo.Interceptors;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import java.util.Arrays;

@Loggable
@Interceptor
public class LoggingInterceptor {
    @AroundInvoke
    public Object logMethodCall(InvocationContext context) throws Exception {
        System.out.println("Method invoked: " + context.getMethod().getName() + " Class: " + context.getMethod().getDeclaringClass().getName());

        if (context.getParameters().length > 0) {
            System.out.println("Method parameters: " + Arrays.toString(context.getParameters()));
        }

        long startTime = System.currentTimeMillis();

        try {
            return context.proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            System.out.println("Exiting method: " + context.getMethod().getName() +
                    " - Execution time: " + (endTime - startTime) + " ms");
        }
    }
}
