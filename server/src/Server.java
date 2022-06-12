import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) {
        startServer();
    }

    /**
     * Start the server creating a socket on the port 1234. One instanced it waits for new
     * clients to connect to the server and sent the request of them to a new thread created automatically by
     * the system. It next starts a conversation with the client since he wants to left the server.
     */
    private static void startServer() {
        try (ServerSocket server = new ServerSocket(1234)) {
            System.out.println("Waiting for clients...");
            ExecutorService service = Executors.newCachedThreadPool();

            do {
                Socket serverToClient = server.accept();
                System.out.println("New client connected to the server: " + serverToClient.getInetAddress() + ":" + serverToClient.getPort());
                Protocol clientProtocol = new Protocol(serverToClient);
                service.submit(clientProtocol);
            } while (true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
