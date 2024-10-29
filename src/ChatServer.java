import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("InfiniteLoopStatement")
public class ChatServer {
    public static final int SERVER_PORT = 9000;
    private static final Logger LOGGER = Logger.getLogger(ChatServer.class.getName());
    private static final Map<Integer, Integer> PORT_MAP = Collections.synchronizedMap(new HashMap<>());
    private static final Map<ObjectOutputStream, Integer> CLIENT_OUTPUTS = Collections.synchronizedMap(new HashMap<>());

    public void start() {
        System.out.println("Chat server started on port " + SERVER_PORT);

        try(ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            while(true) {
                Socket clientSocket = serverSocket.accept();
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                PORT_MAP.put(clientSocket.getPort(), in.readInt());
                System.out.printf("New client connected to %s:%d\n", clientSocket.getInetAddress().getHostAddress(),
                        PORT_MAP.get(clientSocket.getPort()));
                new Thread(new ClientHandler(clientSocket, in)).start();
            }
        } catch(IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;
        private final int port;
        private final ObjectOutputStream out;
        private final ObjectInputStream in;

        public ClientHandler(Socket socket, ObjectInputStream in) throws IOException {
            this.socket = socket;
            this.port = PORT_MAP.get(socket.getPort());
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = in;
        }

        @Override
        public void run() {
            try(socket; in; out) {
                CLIENT_OUTPUTS.put(out, port);

                Message message;
                while((message = (Message) in.readObject()) != null) {
                    broadcastMessage(message);
                }
            } catch(IOException | ClassNotFoundException e) {
                CLIENT_OUTPUTS.remove(out);
                System.out.println("Client disconnected from the server on port " + PORT_MAP.remove(socket.getPort()));
            }
        }

        private void broadcastMessage(Message message) throws IOException {
            System.out.printf("Broadcasting on port %d: %s\n", port, message);

            synchronized(CLIENT_OUTPUTS) {
                for(ObjectOutputStream clientOut : CLIENT_OUTPUTS.keySet()) {
                    if(CLIENT_OUTPUTS.get(clientOut) == port) {
                        clientOut.writeObject(message);
                        clientOut.flush();
                    }
                }
            }
        }
    }

    public static Set<Integer> getPorts() {
        return new HashSet<>(PORT_MAP.values());
    }

    public static void main(String[] args) {
        new ChatServer().start();
    }
}
