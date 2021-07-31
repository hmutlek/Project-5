import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Conversation {
    String[] groupMembers;
    private String index;
    private String name;
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
        StringBuilder groupName = new StringBuilder();
        int i = 0;
        for (String name : members) {
            if (i++ == members.length - 1) {
                groupName.append(name);
                break;
            }
            groupName.append(name);
            groupName.append(" & ");
        }
    }
    //makes a conversation object for a conversation that already exists in the csv file
    public Conversation(String[] members, String index, String name) {
        this.groupMembers = members;
        this.index = index;
        this.name = name;
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
        return String.format("%s,%s,%s", this.index, members, this.name);

    }

    public Conversation getConversation(String index) {
        String tempLine = "";
        try {
            conversations.readFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (String line : conversations.getLines()) {
            if (line.split(",")[0].equals(index)) {
                tempLine = line;
                break;
            }
        }
        String[] lineSplit = tempLine.split(",");
        String[] members = lineSplit[1].split("~");
        Conversation tempConversation = new Conversation(members, lineSplit[0], lineSplit[2]);
        return tempConversation;
    }

    //gets all messages from
    public StringBuilder getMessages(String index) {
        StringBuilder messagesString = new StringBuilder();
        try {
            CSVReadWrite messages = new CSVReadWrite("messages.csv");
            messages.readFile();
            for (String line : messages.getLines()) {
                if (line.split(",")[5].equals(index)) {
                    messagesString.append(String.format("%s: %s", line.split(",")[2], line.split(",")[3]));
                    messagesString.append("\n");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return messagesString;

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

    public String getConversations(ArrayList<String> conversationIndex) {
        try {
            conversations.readFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int counter = 1;
        String[] conversationArray = new String[conversationIndex.size()];
        conversationArray = conversationIndex.toArray(conversationArray);

        for (String index : conversationIndex) {
            for (String line : conversations.getLines()) {
                if (line.split(",")[0].equals(index)) {
                    System.out.printf("%d: %s%n", counter, line.split(",")[2]);
                    counter++;
                }
            }
            //print conversation name if it works
        }


        System.out.println("input a number of which conversation you want to view");
        //take input from user of which one they want to select
        //0 is quit
        Scanner scan = new Scanner(System.in);
        int choice;
        choice = Integer.parseInt(scan.nextLine());

        if (choice < 0 || choice > counter) {
            do {
                System.out.printf("Please enter a number between 0 and %d%n", counter);
                choice = scan.nextInt();
            } while (choice < 0 || choice > counter);
        }

        if (choice == 0) {
            return "QUIT";
        } else {
            //returns the index of the selected array
            for (String line : conversationArray) {
                System.out.println(line);
            }
            System.out.println("end of conversation array");
            System.out.println(conversationArray[choice - 1]);
            return conversationArray[choice - 1];
        }
    }

    public void runConversation(Account user) {
        try {
            conversations.readFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Scanner scan = new Scanner(System.in);
        //getting conversations the user is in.
        ArrayList<String> conversationList = new ArrayList<>();
        String users;
        for (String line : conversations.getLines()) {
            users = line.split(",")[1];
            String[] userArray = users.split("~");
            for (String index : userArray) {
                if (index.equals(user.identifier)) {
                    //add conversation to list of conversations
                    conversationList.add(line.split(",")[0]);
                    break;
                }
            }
        }
        //here is the error does not print the things I want
        String result = getConversations(conversationList);
        if (result.equals("QUIT")) {
            //break this in some way
        }


        Conversation activeConversation = getConversation(result);
        System.out.println(activeConversation.getMessages(result));
        int choice;
        //first do loops the whole thing
        do {
            System.out.println("WHat would you like to do?");
            //this do loop just makes it so that the choice is valid
            do {
                System.out.println("1. go back to conversations, 2. send message, 3. quit");

                choice = Integer.parseInt(scan.nextLine());
                if (choice != 1 && choice != 2 && choice != 3) {
                    System.out.println("Please select 1, 2, or 3");
                }
            } while (choice != 1 && choice != 2 && choice != 3);


            //switch statement with options of what to do
            switch(choice) {
                case (1):
                    result = getConversations(conversationList);
                    if (result.equals("QUIT")) {
                        break;
                    }
                    activeConversation = getConversation(result);
                    System.out.println(activeConversation.getMessages(result));
                    break;
                //breaks out of switch loop here

                case (2):
                    String message = "";
                    System.out.println("Type what you would like to send");
                    message = scan.nextLine();
                    activeConversation.sendMessage(message, user.getUserName());
                    break;

                case (3):
                    break;
                }


        } while (choice != 3);
        //gets messages here and shows them
        //after showing ask the user what they would like to do
        //user either quits, looks at other conversations, send message
        //loop that part until they quit.
    }

    public static void main(String[] args) {
        /**
        //The members of this is stored as a String array for now until something better is figured out
        String[] testArray = new String[]{"paularoni", "paul"};
        //initializing a conversation that already exists on the csv
        Conversation newTest = new Conversation(testArray, "2", "paularoni & paul");
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
        */
        String[] tempStrings = new String[]{"one", "two"};
        Conversation test = new Conversation(tempStrings, "0", "test");
        Account paul = new Account("paularoni", "poop", "3");
        test.runConversation(paul);
    }

}
