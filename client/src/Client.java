import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        PrintStream output = System.out;

        try (Socket client = new Socket("127.0.0.1", 1234)) {
            output.println("Connection to server: done");
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String request;
            String reply = "";
            String instruction;

            if ((instruction = in.readLine()) != null) {
                output.println(instruction);
            }

            while (!reply.equals("QUIT")) {
                if ((request = reader.readLine()) != null) {
                    out.println(request);
                    reply = in.readLine();
                    output.println("Server:$ " + reply);
                }
            }

            in.close();
            out.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
