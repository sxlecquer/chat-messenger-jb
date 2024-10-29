import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message implements Serializable {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    private final String username;
    private final String message;
    private final LocalDateTime timestamp;

    public Message(String username, String message) {
        this.username = username;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "[" + timestamp.format(FORMATTER) + "] " + username + ": " + message;
    }
}
