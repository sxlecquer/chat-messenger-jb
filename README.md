# Socket-Based Chat Messenger

A simple Java-based chat application using sockets, enabling multiple clients to connect and chat through different port groups.
This project demonstrates server-side tracking of active port connections and dynamic client handling, making it suitable for learning socket programming and exploring multi-client communication over ports.

## Features
- **Multi-client support**: Connect multiple clients to the server, with each client assigned to a specific port group for organized communication.
- **Dynamic port tracking**: The server dynamically tracks active ports and updates connected clients, allowing users to join existing chat groups.
- **Synchronized message broadcasting**: Messages are broadcasted only to clients in the same port group, ensuring smooth communication within each group.
- **User-defined port selection**: Clients can either start a new chat group or join an existing one by selecting a port from the active ports list.
- **Simple UI**: Clients interact through a command-line interface with real-time message updates.

## Getting Started
#### 1. Clone the repository:
```bash
git clone https://github.com/sxlecquer/chat-messenger-jb.git
cd chat-messenger-jb
```

#### 2. Compile the Java files:
```bash
javac ChatServer.java ChatClient.java Message.java
```

#### 3. Run the ChatServer:
```bash
java ChatServer
```

#### 4. Run the ChatClient (in a separate terminal or console for each client instance):
```bash
java ChatClient
```

## Usage
1. Start the server by running `ChatServer`. The server initializes on a default port (9000).
2. Launch a client by running `ChatClient` and enter your username.
3. Select a port:
    - Choose to start a new chat group with a unique port.
    - Or join an existing chat group by selecting an available port.

## Code Structure
- **ChatServer**: Manages client connections, port tracking, and broadcasting messages within port groups.
- **ChatClient**: Establishes client connection, prompts for a username, and provides an interface for sending and receiving messages.
- **Message**: Encapsulates message details, including timestamp, username, and message content.

## Demo
The screenshots below demonstrate the functionality of the chat messenger application, where multiple clients connect to the server on different port groups to engage in chat sessions.

Clients connect to different ports (`8080` and `8081`), forming separate chat groups. Messages sent by users in one port group are only broadcasted to other users within the same group.

In this demo, `ChatClient1` and `ChatClient3` are connected to port `8080`, while `ChatClient2` connects to port `8081`.

#### _ChatServer_
![ChatServer](https://github.com/user-attachments/assets/516db515-eb8c-47c6-bf57-bb2789e9da39)

#

#### _ChatClient1_
![ChatClient 1](https://github.com/user-attachments/assets/d6a0eb9c-1e05-4f3b-a289-45bdcc1bfc49)

#

#### _ChatClient2_
![ChatClient 2](https://github.com/user-attachments/assets/6160920c-f7de-42f3-9dc7-77381856a7e7)

#

#### _ChatClient3_
![ChatClient 3](https://github.com/user-attachments/assets/34be3f9a-c75b-429a-af3f-629cbc42deac)

These interactions illustrate the application’s main feature: segmented chat sessions based on port groups, with the server dynamically managing client connections and message broadcasts per group.

#
