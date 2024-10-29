import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("InfiniteLoopStatement")
public class ChatClient {
    private static final Logger LOGGER = Logger.getLogger(ChatClient.class.getName());
    private static final String ADDRESS = "localhost";
    private final String username;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ChatClient(String username) {
        this.username = username;
    }

    private void start(int port) {
        try(Socket socket = new Socket(ADDRESS, ChatServer.SERVER_PORT)) {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeInt(port);
            out.flush();

            in = new ObjectInputStream(socket.getInputStream());
            new Thread(new ServerListener()).start();
            sendMessages();
        } catch(IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private void sendMessages() throws IOException {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            String message = scanner.nextLine();
            out.writeObject(new Message(username, message));
            out.flush();
        }
    }

    private class ServerListener implements Runnable {
        @Override
        public void run() {
            try {
                Message message;
                while((message = (Message) in.readObject()) != null) {
                    System.out.println(message);
                }
            } catch(IOException | ClassNotFoundException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Set<Integer> serverPorts = ChatServer.getPorts();

        int port;
        if(!serverPorts.isEmpty()) {
            System.out.println("Would you like to start a new chat or join an existing one? (0/1)");
            System.out.println("0 - Start a new chat");
            System.out.println("1 - Join an existing chat");

            int choice = getChoice(scanner);
            port = (choice == 0) ? getPort(scanner) : getExistingPort(scanner, serverPorts);
        } else {
            System.out.println("You don't have any chats yet. Let's create one!");
            port = getPort(scanner);
        }

        String username = getUsername(scanner);

        new ChatClient(username).start(port);
    }

    private static int getPort(Scanner scanner) {
        int port;
        while(true) {
            System.out.println("Enter port number (0-65535):");
            if(scanner.hasNextInt()) {
                port = scanner.nextInt();
                scanner.nextLine();
                if(ChatServer.getPorts().contains(port)) {
                    System.out.println("This port is already in use. Try another one.");
                    continue;
                }
                if(port >= 0 && port <= 65535) break;
            } else {
                scanner.nextLine();
            }
            System.out.println("Incorrect port. Please try again.");
        }
        return port;
    }

    private static int getExistingPort(Scanner scanner, Set<Integer> serverPorts) {
        int port;
        while(true) {
            System.out.println("Chat ports available:");
            serverPorts.forEach(System.out::println);

            System.out.println("Enter a port number from the list to join an existing chat:");
            if(scanner.hasNextInt()) {
                port = scanner.nextInt();
                scanner.nextLine();
                if(serverPorts.contains(port)) break;
            } else {
                scanner.nextLine();
            }
            System.out.println("Incorrect port. Please choose it from the list.");
        }
        return port;
    }

    private static int getChoice(Scanner scanner) {
        while(true) {
            if(scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine();
                if(choice == 0 || choice == 1) return choice;
            } else {
                scanner.nextLine();
            }
            System.out.println("Incorrect choice. Please enter 0 or 1.");
        }
    }

    private static String getUsername(Scanner scanner) {
        String username;
        while(true) {
            System.out.println("Enter your username:");
            username = scanner.nextLine();
            if(!username.isEmpty() && username.length() <= 30) break;
            System.out.println("Invalid username. Must be between 1 and 30 characters long.");
        }
        return username;
    }
}
