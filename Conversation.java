import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Conversation {
    String[] groupMembers;
    private String index;
    static CSVReadWrite conversations = new CSVReadWrite("Conversations.csv");

    //makes a new conversation object and adds it to the list of conversations
    public Conversation(String[] members) {
        this.groupMembers = members;
        try {
            conversations.readFile();
            this.index = conversations.getNextIndex();
            conversations.append(this.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //makes a conversation object for a conversation that already exists in the csv file
    public Conversation(String[] members, String index) {
        this.groupMembers = members;
        this.index = index;
    }

    //sends message in the conversation
    public void sendMessage(String message, String from) {
        Message toSend = new Message(from, message, this.index);

    }

    //puts the information of a line into a string so it can be read into a csv file
    public String toString() {
        StringBuilder members = new StringBuilder();
        for (String member : this.groupMembers) {
            members.append(member);
            members.append("~");
        }
        return String.format("%s,%s", this.index, members);

    }

    //gets all messages from
    public StringBuilder getMessages() {
        StringBuilder messagesString = new StringBuilder();
        try {
            CSVReadWrite messages = new CSVReadWrite("messages.csv");
            messages.readFile();
            for (String line : messages.getLines()) {
                if (line.split(",")[5].equals(this.index)) {
                    messagesString.append(String.format("%s: %s", line.split(",")[2], line.split(",")[3]));
                    messagesString.append("\n");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return messagesString;

    }

    public static void main(String[] args) {
        //The members of this is stored as a String array for now until something better is figured out
        String[] testArray = new String[]{"paularoni", "paul"};
        //initializing a conversation that already exists on the csv
        Conversation newTest = new Conversation(testArray, "2");
        //sending a new message updates the messages in messages.csv
        newTest.sendMessage("check the csv file before you run this and this message should not be there", "2");
        //shows all messages in a conversation
        System.out.println(newTest.getMessages());

        //making a new conversation
        String[] arrayTwo = new String[]{"ricky", "bubbles"};
        //this conversation does not exist in the csv file currently
        Conversation testTwo = new Conversation(arrayTwo);
        //these messages do not exist in the csv file yet check to see that
        testTwo.sendMessage("This message does not exist until it is ran", "bubbles");
        testTwo.sendMessage("I do not understand what that means", "ricky");
        //displaying the messages in this conversation
        System.out.println(testTwo.getMessages());

    }

}
