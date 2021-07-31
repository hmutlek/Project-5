/**
 * This is a basic outline/ guide for what the User class should be and how the information should be stored
 *
 * Purdue University -- CS18000 -- Summer 2021 - User class
 *
 * @author Group 003 - Haroone Mutlek,
 * @version 7/18/21 last edited at 6:57
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class User {
    private String username;
    private String passcode;
    private ArrayList<String> conversationsIndex = new ArrayList<>();
    private int userIndex;

    public User(String username, String passcode) {
        // username will be checked on input before making user. If username is unique then it will be added.
        this.username = username;
        this.passcode = passcode;
        // create a new file with user's username to contain all conversations.
    }
    public User(String username) {
        this.username = username;
    }
    public User(String username, String passcode, ArrayList<String> conversations) {
        // After reading from csv file, recreates user object.
        this.username = username;
        this.passcode = passcode;
        this.conversationsIndex = conversations;
    }
    public String getUsername() {
        return this.username;
    }
    public String getPasscode() {
        return this.passcode;
    }
    public void setUsername(String username) throws IOException {
        CSVReadWrite changeUsername = new CSVReadWrite("logins.csv");
        changeUsername.readFile();
        int x = 0;
        for (int i = 0; i < changeUsername.getLines().size(); i++) {
            if (changeUsername.getLines().get(i).contains(this.toString())) {
                x = i;
            }
        }
        System.out.println(x);
        ArrayList<String> fields =
                new ArrayList<String>(Arrays.asList(changeUsername.getLines().get(x).split(",")));
        changeUsername.replaceLine
                (1, fields.get(1), username + "," + fields.get(2) + "," + fields.get(3));
        changeUsername.readFile();

    }
    public ArrayList<String> getConversations() {
        return this.conversationsIndex;
    }
    public void setPasscode(String passcode) throws IOException {
        CSVReadWrite changePassword = new CSVReadWrite("logins.csv");
        changePassword.readFile();
        int x = 0;
        for (int i = 0; i < changePassword.getLines().size(); i++) {
            if (changePassword.getLines().get(i).contains(this.toString())) {
                x = i;
            }
        }
        System.out.println(x);
        ArrayList<String> fields =
                new ArrayList<String>(Arrays.asList(changePassword.getLines().get(x).split(",")));
        changePassword.replaceLine
                (1, fields.get(1), fields.get(1) + "," + passcode + "," + fields.get(3));
        changePassword.readFile();
    }

        public void addConversation (String index){
            this.conversationsIndex.add(index);
        }
    public String conversationstoString() {
        // converts ArrayList into String of the GroupNames seperated by "/" such as "GroupName1/GroupName2/GroupName3/
        String conversationNames = "";
        for (int i = 0; i < this.conversationsIndex.size(); i++) {
            conversationNames += this.conversationsIndex.get(i);
            conversationNames += "/";
        }
        if (conversationNames.length() > 1) {
            conversationNames = conversationNames.substring(0, conversationNames.length() - 1);
        }
        return conversationNames;
    }
    public String toString() {
        // converts object into String that can be wrote into csv and read out of csv such as each line
        // is "username,password,GroupName1/GroupName2/GroupName3
        if (this.conversationsIndex.isEmpty()) {
            return username + "," + passcode;
        }
        return username + "," + passcode + "," + conversationstoString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!this.getClass().equals(o.getClass())) {
            return false;
        }
        User that = (User) o;
        boolean returnValue;
        if ((this.username.equals(that.username)) && (this.passcode == that.passcode) &&
                (this.conversationsIndex == that.conversationsIndex)) {
            returnValue = true;
        } else {
            returnValue = false;
        }
        return returnValue;
    }
}

