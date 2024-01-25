package agb.stream;

import io.obswebsocket.community.client.OBSRemoteController;
import spark.Spark;

import java.util.HashMap;
import java.util.Map;

public class DynamicEndpointServer {
    private static final Map<String, Runnable> endpoints = new HashMap<>();

    public static void main(String[] args) {
        try {

            String obsPassword = args[0];

            if(obsPassword == null || obsPassword.isEmpty()) {
                throw new IllegalArgumentException("OBS Password must be provided as the first argument.");
            }

            OBSRemoteController controller = OBSRemoteController.builder()
                    .host("localhost")                  // Default host
                    .port(4455)                         // Default port
                    .password(obsPassword)   // Provide your password here
                    .connectionTimeout(3)               // Seconds the client will wait for OBS to respond
                    .build();

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

            addEndpoint("desktop_start", () -> {
                System.out.println("Desktop has connected. Switching to Desktop Scene.");

                controller.connect();
                controller.setCurrentProgramScene("DESKTOP", 100);
                controller.disconnect();

            });

            addEndpoint("desktop_disconnect", () -> {
                System.out.println("Desktop has disconnected. Switching to Desktop Disconnected Scene.");

                controller.connect();
                controller.setCurrentProgramScene("DESKTOP - DISCONNECTED", 100);
                controller.disconnect();

            });


            addEndpoint("irl_start", () -> {
                System.out.println("IRL Camera has connected. Switching to IRL Scene.");

                controller.connect();
                controller.setCurrentProgramScene("IRL", 100);
                controller.disconnect();

            });

            addEndpoint("irl_disconnect", () -> {
                System.out.println("IRL Camera has disconnected. Switching to IRL Disconnected Scene.");

                controller.connect();
                controller.setCurrentProgramScene("IRL - DISCONNECTED", 100);
                controller.disconnect();

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
