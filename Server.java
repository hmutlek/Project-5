import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

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

    public static void main (String[] args) throws IOException {
        port = 4242;
        ServerSocket serverSocket = new ServerSocket(port);
        while (running) {
            System.out.println("Waiting for the client to connect...");
            Socket socket = serverSocket.accept();
            System.out.println("Client connected!");
            Functions functionThread = new Functions(socket);
            functionThread.start();
            functions.add(functionThread);
        }
    }
}

class Functions extends Thread {
    private Socket socket;
    String choice;
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
                    //the following codes are used to test login and signup options
                    do {
                        try {
                            switch (choice) {
                                case "1" :
                                    user = new Account();
                                    writer.write("Please enter your username:");
                                    username = reader.readLine();
                                    writer.write("Please enter your password:");
                                    password = reader.readLine();
                                    if (user.logIn(username, password)) {
                                        writer.write("Logged in");
                                        user.getIdentifier();
                                        ifContinue = false;
                                    } else {
                                        writer.write("Log in failed, please check your username or password");
                                        choice = reader.readLine();
                                    }
                                    break;



                                case "2" :
                                    System.out.println("Please enter your username:");
                                    username = reader.readLine();
                                    System.out.println("Please enter your password:");
                                    password = reader.readLine();
                                    if (username.equals("deletedAccount")) {
                                        writer.write("Sorry, it is a reserved word. Please use another username!");
                                        break;
                                    }
                                    user = new Account(username, password);
                                    if (user.signUp()) {
                                        writer.write("Signed up");
                                        user.getIdentifier();
                                        ifContinue = false;
                                    } else {
                                        writer.write("Sign up failed, the username is already in use");
                                        System.out.println();
                                        choice = reader.readLine();
                                    }
                                    break;

                                case "exit" :
                                    break;

                                default :
                                    writer.write("please enter a valid input");
                                    choice = reader.readLine();
                                    break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println("oops, IOException occurred.");
                        }
                    } while (ifContinue && !choice.equals("exit"));

                    //the following do while loop test account modification
                    ifContinue = true;
                    Boolean ifDeleted = false;
                    do {
                        if (ifDeleted) {
                            break;
                        }
                        try {
                            choice = reader.readLine();
                            switch (choice) {
                                case "1" :
                                    writer.write("please enter a new username");
                                    String temp = reader.readLine();
                                    if (user.changeUserName(temp)) {
                                        writer.write("username changed successfully.");
                                    } else {
                                        writer.write("username is already been used");
                                    }
                                    break;

                                case "2" :
                                    writer.write("please enter a new password");
                                    temp = reader.readLine();
                                    if (user.changePassword(temp)) {
                                        writer.write("password changed successfully.");
                                    } else {
                                        writer.write("failed to change password");
                                    }
                                    break;

                                case "3" :
                                    System.out.println("Are you sure you want to delete your account?(type yes to confirm)");
                                    temp = reader.readLine();
                                    if (temp.equalsIgnoreCase("yes")) {
                                        user.deleteAccount();
                                        writer.write("account deleted");
                                        ifDeleted = true;
                                    } else {
                                        writer.write("going back...");
                                    }
                                    break;

                                case "4":
                                    //method can only run if on a conversation object
                                    //so I make a conversation object that does not get added on to conversations.csv
                                    //the only thing it does is exist so runConversation can happen
                                    String[] tempStrings = new String[]{"me"};
                                    Conversation tempConvo = new Conversation(tempStrings, "0", "doesn't matter");
                                    tempConvo.runConversation(user);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } while (ifContinue && !choice.equals("exit"));
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
