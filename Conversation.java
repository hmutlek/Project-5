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
    //member array should be indexes of the members
    public Conversation(String[] members) {
        StringBuilder groupName = new StringBuilder();
        CSVReadWrite users = new CSVReadWrite("users.csv");
        try {
            users.readFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        this.groupMembers = members;

        int i = 0;
        Account fakeTest = new Account("placeholder", "Placeholder", "90");
        for (String index : members) {
            if (i++ == members.length -1) {
                groupName.append(fakeTest.getUserFromIndex(index));
                break;
            }
            groupName.append(fakeTest.getUserFromIndex(index));
            groupName.append(" & ");
        }

        //automatically adds new conversation to csv
        this.name = groupName.toString();
        try {
            conversations.readFile();
            conversations.append(this.toString());
        } catch (IOException e) {
            e.printStackTrace();
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
        return String.format("%s,%s", members, this.name);

    }

    //gets conversation with a given index and returns a conversation object
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

    //gets all messages for a conversation, taking an index as input
    public String getMessages(String index) {
        StringBuilder messagesString = new StringBuilder();
        try {
            CSVReadWrite messages = new CSVReadWrite("messages.csv");
            messages.readFile();
            for (String line : messages.getLines()) {
                try {
                    if (line.split(",")[5].equals(index)) {
                        messagesString.append(String.format("%s: %s", line.split(",")[2], line.split(",")[3]));
                        messagesString.append("~");
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    continue;
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return messagesString.toString();

    }
    //gets all messages for a conversation but this one is just conversation.getMessages() with no input
    public String getMessages() {
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
        return messagesString.toString();

    }

    //Takes input of a list of conversation indexes and returns a string to print to print the names of them
    public String getConversations(ArrayList<String> conversationIndex) {
        try {
            conversations.readFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int counter = 1;
        String[] conversationArray = new String[conversationIndex.size()];
        conversationArray = conversationIndex.toArray(conversationArray);
        StringBuilder toReturn = new StringBuilder();
        for (String index : conversationArray) {

            for (String line : conversations.getLines()) {
                if (line.split(",")[0].equals(index)) {
                    toReturn.append(String.format("%d: %s~", counter, line.split(",")[2]));
                    counter++;
                }
            }
            //print conversation name if it works
        }
        toReturn.append(",");
        toReturn.append(counter);
        System.out.println(toReturn);
        return toReturn.toString();
        /*
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
            return conversationArray[choice - 1];
        }
         */
    }

    //makes a new conversation by taking inputs of who to add
    //if a user does not exist in users.csv then it says user does not exist
    public void newConversation(Account user) {
        Scanner scan = new Scanner(System.in);
        StringBuilder members = new StringBuilder();
        CSVReadWrite users = new CSVReadWrite("users.csv");
        try {

            users.readFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String toAdd = "";
        String goAgain = "";
        try {
            members.append(user.getIdentifier());
        } catch (IOException e) {
            e.printStackTrace();
        }
        do {
            System.out.println("Enter username of person to add to conversation");
            toAdd = scan.nextLine();
            boolean nameExist = false;
            for (String line : users.getLines()) {
                if (line.split(",")[1].equals(toAdd)) {
                    members.append("~");
                    members.append(line.split(",")[0]);
                    nameExist = true;
                }
            }

            if (nameExist) {
                System.out.println("Would you like to add another person y/n");
                goAgain = scan.nextLine();
            } else {
                goAgain = "y";
                System.out.println("That username does not exist please enter a new one");
            }
        } while (goAgain.equals("y"));
        String[] memberString = members.toString().split("~");
        Conversation tempName = new Conversation(memberString);
    }

    //used to get the conversations list that a user is in to be used in getConversations()
    public ArrayList<String> inConversations(Account user) {
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
        return conversationList;
    }

    //to run this all it needs is a user input
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

        if (conversationList.size() == 0) {
            System.out.println("You have no conversations");
            conversationList.add(conversations.getNextIndex());
            newConversation(user);
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
            System.out.println("What would you like to do?");
            //this do loop just makes it so that the choice is valid
            do {
                System.out.println("1. go back to conversations, 2. send message, 3. quit, 4 begin new conversation");

                choice = Integer.parseInt(scan.nextLine());
                if (choice != 1 && choice != 2 && choice != 3 && choice != 4) {
                    System.out.println("Please select 1, 2, 3, or 4");
                }
            } while (choice != 1 && choice != 2 && choice != 3 && choice != 4);


            //switch statement with options of what to do
            switch(choice) {
                case (1):
                    conversationList = inConversations(user);
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
                    //quits out, in a weird spot since this will be made into a gui
                    break;
                case(4):
                    //makes new conversation using method made earlier
                    newConversation(user);
            }
        } while (choice != 3);
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
