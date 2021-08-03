import java.io.IOException;
import java.util.Scanner;
/**
 * Application
 *
 * The basic work flow of the program, it resumes project 4
 *
 * @author Group 03
 *
 * @version July 30 2021
 *
 */
public class Application {
    public static String choice;
    static Scanner scan = new Scanner(System.in);
    public static String welcome() {
        System.out.println("Welcome using this application please select options below" +
            "\n1.Log in    2.Sign up     3.type exit to exit anytime");
        return scan.nextLine();
    }

    public static boolean testIfExit() {
        if (choice.equals("exit")) {
            return true;
        }
        return false;
    }

    public static void main (String[] args) throws IOException {
        Client client = new Client();


    }
}
