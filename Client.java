import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    String usernameInput = "Please enter your username:";
    String passwordInput = "Please enter your password:";
    String loggedIn = "Logged in";
    String loggedFailed = "Log in failed, please check your username or password";
    String reserved = "Sorry, it is a reserved word. Please use another username!";
    String signedUp = "Signed up";


    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Socket socket = new Socket("localhost", 1212);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream());
        String read = null;

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
                            System.out.println(reader.read());
                            String conversationsTemp = reader.readLine();
                            conversationsTemp = conversationsTemp.replaceAll("~","\n");
                            int size = Integer.parseInt(conversationsTemp.split(",")[1]);
                            String conversations =  "1" + conversationsTemp.split(",")[0];
                            System.out.println(conversations);
                            run.showConversations(content, frame, conversations, size, socket, writer);

                            String messages = reader.readLine();
                            messages = messages.replaceAll("~", "\n");
                            System.out.println(messages);
                            run.showMessages(content, frame, messages, socket, writer);
                            reader.readLine();

                            break;
                        // no loop here because if the user exits out then it ends
                    }
                } while (!doesWork.equals(""));
            } catch (Exception ie) {
                writer.close();
                reader.close();
                ie.printStackTrace();
            }
        } while (read != null);
    }
}
