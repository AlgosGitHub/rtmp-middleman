package agb.stream;

import io.obswebsocket.community.client.OBSRemoteController;

public class OBSWebSocketExample {
    public static void main(String[] args) throws InterruptedException {
        OBSRemoteController controller = OBSRemoteController.builder()
                .host("localhost")                  // Default host
                .port(4456)                         // Default port
                .password("OBS_PASSWORD")   // Provide your password here
                .connectionTimeout(3)               // Seconds the client will wait for OBS to respond
                .build();

        controller.connect();

        controller.setCurrentProgramScene("IDE - Project", 100);

        Thread.sleep(5000);

    }
}
