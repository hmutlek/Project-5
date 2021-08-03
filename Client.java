import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * The Client
 *
 * Handles Client
 *
 * @author Group 03
 *
 * @version July 30 2021
 *
 */

public class Client implements Runnable {
    String usernameInput = "Please enter your username:";
    String passwordInput = "Please enter your password:";
    String loggedIn = "Logged in";
    String loggedFailed = "Log in failed, please check your username or password";
    String reserved = "Sorry, it is a reserved word. Please use another username!";
    String signedUp = "Signed up";
    Scanner scanner;
    Socket socket;
    BufferedReader reader;
    PrintWriter writer;
    String read;

    public void run() {
        try {
            //starting the gui
            JFrame frame = new JFrame();
            //if we get a better name for this change name here
            frame.setTitle("Application");

            Container content = frame.getContentPane();
            content.setLayout(new BorderLayout());

            frame.setSize(600, 400);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);
            GuiHandler run = new GuiHandler();
            do {
                try {
                    run.welcome(content, frame, socket, writer);
                    reader.readLine();
                    do {
                        run.getLogin(content, frame, socket, writer, reader);
                        read = reader.readLine();
                        if (!read.equals("true")) {
                            JOptionPane.showMessageDialog(null, "Invalid username / password.\n" +
                                "Please a different username / password");
                        }
                    } while(!read.equals("true"));
                    read = reader.readLine(); //should read continue
                    String doesWork = " ";
                    do {
                        run.accountChoice(content, frame, socket, writer);
                        //after a choice is made
                        read = reader.readLine();
                        switch (read) {
                            case "1":
                                do {
                                    run.changeUsername(content, frame, socket, writer);
                                    doesWork = reader.readLine();

                                    if (!doesWork.equals("worked")) {
                                        JOptionPane.showMessageDialog(null, "Invalid username", "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                } while (!doesWork.equals("worked"));
                                break;

                            case "2":
                                do {
                                    run.changePassword(content, frame, socket, writer);
                                    doesWork = reader.readLine();


                                    if (!doesWork.equals("worked")) {
                                        JOptionPane.showMessageDialog(null, "Invalid password", "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                } while (!doesWork.equals("worked"));
                                break;

                            case "3":
                                run.deleteAccount(content, frame, socket, writer);
                                doesWork = reader.readLine();

                                String result = reader.readLine();
                                if (result.equals("account deleted")) {
                                    JOptionPane.showMessageDialog(null, "Account has been deleted", "It Gone", JOptionPane.WARNING_MESSAGE);
                                }
                                break;

                            case "4":
                                //reader.read(); //don't know what this part is reading but the whole thing does not work if this is not here
                                String conversationsTemp = reader.readLine();
                                String messages = "";
                                int size = 0;
                                String conversations;
                                conversationsTemp = conversationsTemp.replaceAll("~", "\n");
                                size = Integer.parseInt(conversationsTemp.split(",")[1]);
                                conversations = conversationsTemp.split(",")[0];
                                if (!conversations.isEmpty()) {
                                    run.showConversations(content, frame, conversations, size, socket, writer);
                                    messages = reader.readLine();
                                    messages = messages.replaceAll("~", "\n");
                                }
                                boolean exit = false;
                                do {
                                    System.out.println("here");
                                    run.showMessages(content, frame, messages, socket, writer);
                                    String newChoice = reader.readLine();
                                    switch (newChoice) {
                                        case "1":
                                            conversationsTemp = reader.readLine();
                                            conversationsTemp = conversationsTemp.replaceAll("~", "\n");
                                            size = Integer.parseInt(conversationsTemp.split(",")[1]);
                                            conversations = conversationsTemp.split(",")[0];
                                            run.showConversations(content, frame, conversations, size, socket, writer);
                                            String testOne = reader.readLine();

                                            messages = reader.readLine();
                                            messages = messages.replaceAll("~", "\n");

                                            run.showMessages(content, frame, messages, socket, writer);
                                            String testTwo = reader.readLine();
                                            reader.readLine();
                                            break;

                                        case "2":
                                            run.sendMessage(content, frame, socket, writer);
                                            reader.readLine();
                                            //this one is really simple which is nice
                                            //it just opens the message sending gui send to the server there,
                                            //and the server writes the message to messages there

                                        case "3":
                                            run.startConvo(content, frame, socket, writer);
                                            reader.readLine();

                                        case "4":
                                            System.out.println("here");
                                            exit = true;
                                    }

                                } while (exit); // fourth option from the buttons is exit change the boolean there

                                break;
                            // no loop here because if the user exits out then it ends
                        }
                    } while (!doesWork.equals(""));
                } catch (Exception ie) {
                    writer.close();
                    reader.close();
                    ie.printStackTrace();
                    return;
                }
            } while (read != null);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Client() throws IOException {
        Scanner scanner = new Scanner(System.in);
        Socket socket = new Socket("localhost", 1212);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream());
        String read = null;
        Thread t = new Thread(this);
        t.start();
    }
}
