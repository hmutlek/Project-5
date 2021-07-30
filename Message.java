import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Message
 *
 * This class creates message objects contains information such as timestamp, sender, and the content
 *
 * @author Hao Zhou
 *
 * @version July 18 2021
 *
 */
public class Message {
    static CSVReadWrite messages = new CSVReadWrite("messages.csv");
    private String timeStamp;
    private String sender;
    private String content;
    private boolean edited;
    private String groupIndex;



    public Message(String sender, String content, String groupIndex) {
        this.timeStamp = getTimeStamp();
        this.sender = sender;
        this.content = content;
        this.edited = false;
        this.groupIndex = groupIndex;
        try {
            messages.readFile();
            messages.append(this.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Message(String timeStamp, String sender, String content, boolean edited, String groupIndex) {
        this.timeStamp = timeStamp;
        this.sender = sender;
        this.content = content;
        this.edited = edited;
        this.groupIndex = groupIndex;
    }

    /**
     * rewrite the content of the message, and updates the edited to be true and the timestamp to current time
     *
     * @param content, a String that is meant to replace the old message content
     */
    public void editMessage(String content) {
        this.content = content;
        this.timeStamp = getTimeStamp();
        this.edited = true;
    }
    public String getContent() {
        return this.content;
    }

    /**
     * the time of when the message was sent
     *
     * @return A String representing the time
     */
    public static String getTimeStamp() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm");
        return dateFormat.format(date);
    }

    /**
     * the string that will be printed out
     *
     * @return A String representing fields separated by commas,
     *the order goes like: timeStamp, sender, content, edited(edited is a boolean value
     */
    public String toString() {
        return String.format("%s,%s,%s,%s,%s", timeStamp, sender, content, edited, groupIndex);
    }


}
