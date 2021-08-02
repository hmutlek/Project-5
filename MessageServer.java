import javax.swing.*;
import java.io.*;
import java.net.*;

public class MessageServer {
    public static void main(String[] args) throws IOException {
        Account user = new Account();
        Boolean ifContinue = true;

        ServerSocket serverSocket = new ServerSocket(4242);
        Socket socket = serverSocket.accept();

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream());

        int choice = reader.read();
        System.out.printf("Received:\n%f\n", choice); //checks whether 1.login or 2.sign up

            try {
                switch (choice) {
                    case 1:
                        user = new Account();
                        String username = reader.readLine();
                        String password = reader.readLine();
                        if (user.logIn(username, password)) {
                            user.getIdentifier();
                            writer.write("Success"); //client side: go to next
                        } else {
                            writer.write("Fail"); // re-do
                        }
                        break;

                    case 2:
                        user = new Account();
                        String newUsername = reader.readLine(); // first enter username and verify
                        if (!user.signUp()) {
                            writer.write("Fail");
                        } else {
                            String newUserPass = reader.readLine();  // enter password
                            user.getIdentifier();
                            writer.write("Success");
                        }
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        int choice2 = reader.read();
        System.out.printf("Received:\n%f\n", choice2); //checks whether 1.login or 2.sign up

        ifContinue = true;
        Boolean ifDeleted = false;
        while (ifDeleted) {
            try {
                switch (choice2) {
                    // I don't think changing username/password should be sent to server
                    // If only the result is sent to the server, it can be saved then.
                    case 1:
                        String temp = reader.readLine();
                        user.changeUserName(temp);
                        //change Username;
                        break;
                    case 2:
                        temp = reader.readLine();
                        user.changePassword(temp);
                        // change Password
                        break;
                    case 3:
                        user.deleteAccount();
                        ifDeleted = true;
                        // Delete
                        break;
                    case 4:
                        String[] tempStrings = new String[]{"me"};
                        Conversation tempConvo = new Conversation(tempStrings, "0", "doesn't matter");
                        tempConvo.runConversation(user);
                        //start Conversation
                        break;
                    }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

