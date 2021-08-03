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
        do {
            try {
                read = reader.readLine();
                System.out.println(read);
                String output = scanner.nextLine();
                writer.write(output);
                writer.println();
                writer.flush();
            } catch (Exception ie) {
                writer.close();
                reader.close();
                ie.printStackTrace();
            }
        } while (read != null);
    }
}
