package org.example.demo.Beans;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.example.demo.Services.AsyncCalculationService;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Named
@RequestScoped
@Getter
@Setter
public class AsyncDemoBean implements Serializable {

    @Inject
    private AsyncCalculationService asyncService;

}