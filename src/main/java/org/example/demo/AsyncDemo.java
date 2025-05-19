package org.example.demo;

import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.Services.AsyncCalculationService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@WebServlet(name = "asyncDemo", value = "/async-demo")
public class AsyncDemo extends HttpServlet {

    @Inject
    private AsyncCalculationService asyncService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        try {
            Future<Double> futureValue = asyncService.calculateTotalInventoryValue();

            System.out.println("DEMO: Asynchronous calculation started.");

            if (futureValue != null && futureValue.isDone()) {
                System.out.println("DEMO: Asynchronous calculation completed immediately.");
                out.println("<p>Total inventory value: $" + futureValue.get() + "</p>");
            } else {
                System.out.println("DEMO: Asynchronous calculation is still running.");

                try {
                    Double value = futureValue.get(10, TimeUnit.SECONDS);
                    System.out.println("DEMO: Asynchronous calculation completed within 10 seconds.");
                    out.println("<p>Total inventory value: $" + value + "</p>");
                } catch (TimeoutException e) {
                    System.out.println("DEMO: Asynchronous calculation timed out.");
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            out.println("<p>Error during asynchronous operation: " + e.getMessage() + "</p>");
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            out.println("<p>Unexpected error: " + e.getMessage() + "</p>");
            e.printStackTrace();
        }
        out.println("</body></html>");
    }
}