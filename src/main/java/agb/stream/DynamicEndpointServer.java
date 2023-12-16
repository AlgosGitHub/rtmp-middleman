package agb.stream;

import spark.Spark;

import java.util.HashMap;
import java.util.Map;

public class DynamicEndpointServer {
    private static final Map<String, Runnable> endpoints = new HashMap<>();

    public static void main(String[] args) {
        try {

            // Create a Spark instance
            Spark.port(8080); // You can change the port as needed

            // Define a dynamic endpoint that executes the associated runnable
            Spark.get("/:endpointName", (request, response) -> {
                String endpointName = request.params(":endpointName");
                Runnable runnable = endpoints.get(endpointName);

                if (runnable != null) {
                    runnable.run();
                    return "Endpoint executed: " + endpointName;
                } else {
                    response.status(404);
                    return "Endpoint not found: " + endpointName;
                }
            });

            // Example: Adding an endpoint with a single line of code
            addEndpoint("desktop_start", () -> {
                System.out.println("desktop_start");
                // Add your custom logic here
            });
            addEndpoint("desktop_stop", () -> {
                System.out.println("desktop_stop");
                // Add your custom logic here
            });
            addEndpoint("desktop_disconnect", () -> {
                System.out.println("desktop_disconnect");
                // Add your custom logic here
            });

            System.out.println("System Running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to add an endpoint and its corresponding runnable
    public static void addEndpoint(String endpointName, Runnable runnable) {
        endpoints.put(endpointName, runnable);
    }
}
