import java.io.IOException;
import java.util.Scanner;

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
    public static void main (String[] args) {
        String username;
        String password;
        choice = welcome();
        try {
            switch (choice) {
                case "1" :
                    Account user = new Account();
                    boolean userCheck = false;
                    do {
                        userCheck = false;
                        System.out.println("Please enter your username:");
                        username = scan.nextLine();
                        System.out.println("Please enter your password:");
                        password = scan.nextLine();
                        if (user.logIn(username, password)) {
                            System.out.println("Logged in");
                            userCheck = true;
                        } else {
                            System.out.println("Log in failed, please check your username or password");
                        }
                    } while (!userCheck);
                    break;


                case "2" :
                    System.out.println("Please enter your username:");
                    username = scan.nextLine();
                    System.out.println("Please enter your password:");
                    password = scan.nextLine();
                    Account newUser = new Account(username, password);
                    if (newUser.signUp()) {
                        System.out.println("Signed up");
                    } else {
                        System.out.println("Sign up failed, the username is already in use");
                    }
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("oops, IOException occurred.");
        }


//        do {
//            System.out.println();
//        } while (!testIfExit());
    }
}
