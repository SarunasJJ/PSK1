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

            out.println("<p>Asynchronous calculation started.</p>");
            out.println("<p>The servlet continues processing while calculation is running...</p>");

            out.println("<p>Doing other work in the servlet...</p>");

            if (futureValue.isDone()) {
                out.println("<p>Calculation finished very quickly!</p>");
                out.println("<p>Total inventory value: $" + futureValue.get() + "</p>");
            } else {
                out.println("<p>Calculation is still in progress...</p>");

                try {
                    Double value = futureValue.get(2, TimeUnit.SECONDS);
                    out.println("<p>Calculation completed within our wait time.</p>");
                    out.println("<p>Total inventory value: $" + value + "</p>");
                } catch (TimeoutException e) {
                    out.println("<p>Calculation is taking longer than our wait time.</p>");
                    out.println("<p>In a real application, we would handle this with:</p>");
                    out.println("<ul>");
                    out.println("<li>JavaScript polling from the client</li>");
                    out.println("<li>WebSockets for server push</li>");
                    out.println("<li>Server-Sent Events</li>");
                    out.println("<li>A callback mechanism</li>");
                    out.println("</ul>");
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            out.println("<p>Error during asynchronous operation: " + e.getMessage() + "</p>");
            Thread.currentThread().interrupt();
        }
        out.println("</body></html>");
    }
}