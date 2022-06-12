import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class Protocol implements Closeable, Runnable {
    private static final ArrayList<Protocol> protocolList = new ArrayList<>();

    private final GregorianCalendar time = new GregorianCalendar();

    private BufferedReader in;
    private File log;
    private PrintWriter out;
    private final Socket serverToClient;
    private String userName;

    public Protocol(@NotNull Socket serverToClient) {
        try {
            log = new File("log.txt");
            if (!log.exists()) {
                log.createNewFile();
            }
            this.in = new BufferedReader(new InputStreamReader(serverToClient.getInputStream()));
            this.out = new PrintWriter(serverToClient.getOutputStream(), true);
            selectUsername();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.serverToClient = serverToClient;
        protocolList.add(this);
    }

    private void selectUsername() {
        String name = "";

        while (name.equals("")) {
            this.out.println("Insert username: ");
            try {
                name = this.in.readLine();
                for (Protocol protocol : protocolList) {
                    if (protocol.getUserName().equals(name)) {
                        this.out.println("Username already taken.");
                        name = "";
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.userName = name;
    }

    @Override
    public void close() throws IOException {
        this.in.close();
        this.out.close();
        this.serverToClient.close();
        protocolList.remove(this);
    }

    @Override
    public void run() {
        try {
            this.out.println("Insert text. Insert 'quit' to exit.");
            String request;

            while ((request = this.in.readLine()) != null) {
                //this.clientMessage(request);
                saveRequest(request);
                String reply = request.toUpperCase();
                this.out.println(reply);
                if (reply.equals("QUIT")) {
                    break;
                }
            }

            this.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveRequest(String request) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(log, true));
            writer.println("<" + time.getTime() + " - " + this.serverToClient.getInetAddress() + ">:<" + request + ">");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUserName() {
        return this.userName;
    }
}
