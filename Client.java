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
        Socket socket = new Socket("localhost", 4242);
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
                run.accountChoice(content, frame, socket, writer);

                //after a choice is made
                read = reader.readLine();
                String doesWork = "";
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
                        
                }
            } catch (Exception ie) {
                writer.close();
                reader.close();
                ie.printStackTrace();
            }
        } while (read != null);
    }
}
