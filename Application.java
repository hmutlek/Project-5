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

    public static void main (String[] args) throws IOException {
        String username;
        String password;
        choice = welcome();
        Boolean ifContinue = true;
        Account user = new Account();


        //the following codes are used to test login and signup options
        do {
            try {
                switch (choice) {
                    case "1" :
                        user = new Account();
                        System.out.println("Please enter your username:");
                        username = scan.nextLine();
                        System.out.println("Please enter your password:");
                        password = scan.nextLine();
                        if (user.logIn(username, password)) {
                            System.out.println("Logged in");
                            user.getIdentifier();
                            ifContinue = false;
                        } else {
                            System.out.println("Log in failed, please check your username or password");
                            choice = welcome();
                        }
                        break;



                    case "2" :
                        System.out.println("Please enter your username:");
                        username = scan.nextLine();
                        System.out.println("Please enter your password:");
                        password = scan.nextLine();
                        user = new Account(username, password);
                        if (user.signUp()) {
                            System.out.println("Signed up");
                            user.getIdentifier();
                            ifContinue = false;
                        } else {
                            System.out.println("Sign up failed, the username is already in use");
                            choice = welcome();
                        }
                        break;

                    case "exit" :
                        break;

                    default :
                        System.out.println("please enter a valid input");
                        choice = welcome();
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("oops, IOException occurred.");
            }
        } while (ifContinue && !choice.equals("exit"));

        //the following do while loop test account modification
        ifContinue = true;
        do {
            System.out.println("What would you like to do?\n" +
                "1. change username     2. change password      3.delete account");
            choice = scan.nextLine();
            try {
                switch (choice) {
                    case "1" :
                        System.out.println("please enter a new username");
                        String temp = scan.nextLine();
                        if (user.changeUserName(temp)) {
                            System.out.println("username changed successfully.");
                        } else {
                            System.out.println("username is already been used");
                        }
                        break;

                    case "2" :
                        System.out.println("please enter a new password");
                        temp = scan.nextLine();
                        if (user.changePassword(temp)) {
                            System.out.println("password changed successfully.");
                        } else {
                            System.out.println("failed to change password");
                        }
                        break;

                    case "3" :
                        System.out.println("Are you sure you want to change the password?");
                        temp = scan.nextLine();
                        if (temp.equalsIgnoreCase("yes")) {
                            user.deleteAccount();
                            System.out.println("account deleted");
                        } else {
                            System.out.println("going back...");
                        }
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (ifContinue && !choice.equals("exit"));

    }
}
