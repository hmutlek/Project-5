import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Server
 *
 * The server that take care of all inputs
 *
 * @author Group 03
 *
 * @version July 30 2021
 *
 */
public class Server {
    private Socket socket = null;
    public static String choice;
    private static int port;
    private static Object gateKeeper = new Object();
    private static ArrayList<Functions> functions = new ArrayList<>();
    public static Server instance;
    public static Boolean running = true;


    public Server(Socket socket) {
        this.socket = socket;
    }

    public Server(int port) {
        this.port = port;
    }

    public static boolean testIfExit() {
        if (choice.equals("exit")) {
            return true;
        }
        return false;
    }

    public static Boolean stopServer(int choice) {
        if (choice == 0) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) throws IOException {
        port = 1212;
        ServerSocket serverSocket = new ServerSocket(port);
        while (running) {
            Socket socket = null;
            try {
                System.out.println("Waiting for the client to connect...");
                socket = serverSocket.accept();
                System.out.println("Client connected!");
                Functions functionThread = new Functions(socket);
                functionThread.start();
                functions.add(functionThread);
            } catch (Exception ie) {
                socket.close();
                ie.printStackTrace();
            }
        }
    }
}

class Functions extends Thread {
    private Socket socket;
    String choice = "0";
    Boolean ifContinue = true;
    Account user = new Account();
    String username;
    String password;
    private BufferedReader reader;
    private PrintWriter writer;

    private Object gateKeeper = new Object();

    public Functions(Socket socket) throws IOException {
        this.socket = socket;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);
    }

    public void run() {
        synchronized (gateKeeper) {

            while (true) {
                try {
                    user = new Account();
                    //the following codes are used to test login and signup options
                    do {
                        try {
                            choice = reader.readLine();
                            writer.write("continue");
                            writer.println();
                            writer.flush();
                            switch (choice) {
                                case "1" :
                                    boolean isGood = false;
                                    do {


                                    username = reader.readLine();
                                    password = reader.readLine();

                                        if (user.logIn(username, password)) {
                                            writer.write("true");
                                            writer.println();
                                            writer.flush();
                                            ifContinue = false;
                                            isGood = true;
                                        } else {
                                            writer.write("Log in failed, please check your username or password");
                                            writer.println();
                                            writer.flush();
                                            System.out.println("BAD");
                                            //choice = reader.readLine();
                                        }
                                    } while (!isGood);
                                    break;



                                case "2" :
                                    username = reader.readLine();
                                    password = reader.readLine();
                                    if (username.equals("deletedAccount")) {
                                        writer.write("Sorry, it is a reserved word. Please use another username!");
                                        writer.println();
                                        writer.flush();
                                        break;
                                    }
                                    user = new Account(username, password);
                                    if (user.signUp()) {
                                        writer.write("true");
                                        writer.println();
                                        writer.flush();
                                        user.setIdentifier(user.getIdentifier());
                                        ifContinue = false;
                                    } else {
                                        writer.write("Sign up failed, the username is already in use");
                                        writer.println();
                                        writer.flush();
                                        choice = reader.readLine();
                                    }
                                    break;

                                case "exit" :
                                    break;

                                default :
                                    writer.write("please enter a valid input");
                                    writer.println();
                                    writer.flush();
                                    choice = reader.readLine();
                                    break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println("oops, IOException occurred.");
                        }
                    } while (ifContinue && !this.choice.equalsIgnoreCase("exit"));
                    writer.write("continue");
                    writer.println();
                    writer.flush();
                    //the following do while loop test account modification
                    ifContinue = true;
                    Boolean ifDeleted = false;
                    do {
                        if (ifDeleted) {
                            break;
                        }
                        try {
                            choice = reader.readLine();
                            String temp = "";
                            switch (choice) {
                                case "1" :

                                    writer.write("1");
                                    writer.println();
                                    writer.flush();
                                    boolean usernameCheck = false;
                                    do {
                                        temp = reader.readLine();
                                        System.out.println(temp);
                                        if (user.changeUserName(temp)) {
                                            writer.write("worked");
                                            writer.println();
                                            writer.flush();
                                            usernameCheck = true;
                                        } else {
                                            writer.write("username is already been used");
                                            writer.println();
                                            writer.flush();
                                        }
                                    } while (!usernameCheck);
                                    break;

                                case "2" :
                                    writer.write("2");
                                    writer.println();
                                    writer.flush();

                                    boolean passwordCheck = false;
                                    do {
                                        temp = reader.readLine();
                                        if (user.changePassword(temp)) {
                                            writer.write("worked");
                                            writer.println();
                                            writer.flush();
                                            passwordCheck = true;
                                        } else {
                                            writer.write("failed to change password");
                                            writer.println();
                                            writer.flush();
                                        }
                                    } while (!passwordCheck);
                                    break;

                                case "3" :
                                    writer.write("3");
                                    writer.println();
                                    writer.flush();
                                    temp = reader.readLine();
                                    //if the user clicks all the needed button to delete client sends server "yes"
                                    if (temp.equalsIgnoreCase("yes")) {
                                        user.deleteAccount();
                                        writer.write("account deleted");
                                        writer.println();
                                        writer.flush();
                                        ifDeleted = true;
                                    } else {
                                        writer.write("going back...");
                                        writer.println();
                                        writer.flush();
                                    }
                                    break;

                                case "4":
                                    writer.write("4");
                                    writer.println();
                                    writer.flush();
                                    //method can only run if on a conversation object
                                    //so I make a conversation object that does not get added on to conversations.csv
                                    //the only thing it does is exist so runConversation can happen
                                    String[] tempStrings = new String[]{"me"};
                                    Conversation tempConvo = new Conversation(tempStrings, "0", "doesn't matter");
                                    //set the identifier of the user if it was not already in the user object
                                    user.setIdentifier(user.getIdentifier());
                                    //gets an ArrayList of all the conversations that the user's index is in
                                    /**NEEDS LOGIC HERE TO GO START A CONVERSATION  IF INDEX LENGTH IS 0*/
                                    ArrayList<String> indexes = tempConvo.inConversations(user);
                                    //gets a string of the names of the conversations to send to the client
                                    String toSend = tempConvo.getConversations(indexes);
                                    System.out.println(tempConvo.getConversations(indexes));

                                    writer.write(toSend);
                                    writer.println();
                                    writer.flush();

                                    //choice is what the user chooses
                                    int choice = Integer.parseInt(reader.readLine()) - 1;
                                    //makes an active conversation here, used in some spots later
                                    Conversation activeConvo = tempConvo.getConversation(indexes.get(choice));
                                    //messages sent in the conversation are all in one string and sent to the client
                                    String messages = tempConvo.getMessages(indexes.get(choice));
                                    writer.write(messages);
                                    writer.println();
                                    writer.flush();
                                    //at this point the user is given four option of what to do
                                    do {
                                        String convoChoice = reader.readLine();
                                        switch (convoChoice) {
                                            case "1":
                                                writer.write("1");
                                                writer.println();
                                                writer.flush();

                                                indexes = tempConvo.inConversations(user);
                                                toSend = tempConvo.getConversations(indexes);

                                                writer.write(toSend);
                                                writer.println();
                                                writer.flush();

                                                choice = Integer.parseInt(reader.readLine()) - 1;
                                                activeConvo = tempConvo.getConversation(indexes.get(choice));
                                                messages = tempConvo.getMessages(indexes.get(choice));
                                                writer.write(messages);
                                                writer.println();
                                                writer.flush();
                                                System.out.println(messages);

                                                break;

                                            case "2":
                                                writer.write("2");
                                                writer.println();
                                                writer.flush();

                                                String message = reader.readLine();
                                                activeConvo.sendMessage(message, user.getUserName());

                                                writer.write("done");
                                                writer.println();
                                                writer.flush();

                                            case "3":
                                                writer.write("3");
                                                writer.println();
                                                writer.flush();
                                                StringBuilder members = new StringBuilder();
                                                CSVReadWrite users = new CSVReadWrite("users.csv");
                                                users.readFile();
                                                String username2 = "";
                                                String goAgain = "";
                                                try {
                                                    members.append(user.getIdentifier());
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                                do {
                                                    try {
                                                        username2 = reader.readLine();
                                                    } catch (Exception ie) {
                                                        ie.printStackTrace();
                                                        break;
                                                    }
                                                    boolean nameExist = false;
                                                    for (String line : users.getLines()) {
                                                        if (line.split(",")[1].equals(username2)) {
                                                            members.append("~");
                                                            members.append(line.split(",")[0]);
                                                            nameExist = true;
                                                        }
                                                    }
                                                    if (nameExist) {
                                                        writer.write("true");
                                                        writer.println();
                                                        writer.flush();
                                                    } else {
                                                        goAgain = "y";
                                                        writer.write("false");
                                                        writer.println();
                                                        writer.flush();
                                                    }
                                                } while (goAgain.equals("y"));
                                                String[] memberString = members.toString().split("~");
                                                Conversation tempName = new Conversation(memberString);
                                            case "4":
                                                writer.write("4");
                                                writer.println();
                                                writer.flush();
                                        }
                                    } while (!toSend.equals("test")) ;
                                    break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } while (ifContinue && !choice.equalsIgnoreCase("exit"));
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    writer.close();
                }
            }
        }
    }
}
