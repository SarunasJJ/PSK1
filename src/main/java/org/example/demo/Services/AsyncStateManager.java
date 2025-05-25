package org.example.demo.Services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.Future;

@ApplicationScoped
@Named
@Getter
@Setter
public class AsyncStateManager {
    private boolean calculationInProgress = false;
    private String result = "";
    private String errorMessage = "";
    private Future<Double> futureValue;

    public void reset() {
        calculationInProgress = false;
        result = "";
        errorMessage = "";
        futureValue = null;
    }
}