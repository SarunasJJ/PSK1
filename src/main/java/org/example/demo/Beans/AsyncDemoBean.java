package org.example.demo.Beans;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.example.demo.Services.AsyncCalculationService;
import org.example.demo.Services.AsyncStateManager;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Named("AsyncDemoBean")
@RequestScoped
@Getter
@Setter
public class AsyncDemoBean implements Serializable {

    @EJB
    private AsyncCalculationService asyncService;

    @Inject
    private AsyncStateManager stateManager;

    public String startCalculation() {
        try {
            stateManager.setCalculationInProgress(true);
            stateManager.setResult("");
            stateManager.setErrorMessage("");

            System.out.println("BEAN: Starting async calculation");
            Future<Double> futureValue = asyncService.calculateTotalInventoryValue();
            stateManager.setFutureValue(futureValue);

            if (futureValue != null) {
                System.out.println("BEAN: Async calculation started successfully");
                return null;
            } else {
                stateManager.setErrorMessage("Failed to start calculation");
                stateManager.setCalculationInProgress(false);
            }
        } catch (Exception e) {
            System.err.println("BEAN: Error starting calculation: " + e.getMessage());
            e.printStackTrace();
            stateManager.setErrorMessage("Error starting calculation: " + e.getMessage());
            stateManager.setCalculationInProgress(false);
        }
        return null;
    }

    public String checkResult() {
        Future<Double> futureValue = stateManager.getFutureValue();
        if (futureValue != null) {
            try {
                if (futureValue.isDone()) {
                    Double value = futureValue.get();
                    stateManager.setResult("Total inventory value: $" + String.format("%.2f", value));
                    stateManager.setCalculationInProgress(false);
                    stateManager.setFutureValue(null);
                    System.out.println("BEAN: Calculation completed with result: " + value);
                } else {
                    System.out.println("BEAN: Calculation still in progress...");
                    stateManager.setResult("Still calculating...");
                }
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("BEAN: Error during calculation: " + e.getMessage());
                e.printStackTrace();
                stateManager.setErrorMessage("Error during calculation: " + e.getMessage());
                stateManager.setCalculationInProgress(false);
                stateManager.setFutureValue(null);
                Thread.currentThread().interrupt();
            }
        }
        return null;
    }

    public String getCalculationStatus() {
        if (stateManager.isCalculationInProgress()) {
            Future<Double> futureValue = stateManager.getFutureValue();
            if (futureValue != null && !futureValue.isDone()) {
                return "Calculating total inventory value... Please wait.";
            }
        }
        return "";
    }

    public boolean isCalculationInProgress() {
        return stateManager.isCalculationInProgress();
    }

    public String getResult() {
        return stateManager.getResult();
    }

    public String getErrorMessage() {
        return stateManager.getErrorMessage();
    }

    public String resetCalculation() {
        stateManager.reset();
        return null;
    }
}