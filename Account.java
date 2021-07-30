import java.io.IOException;
import java.util.ArrayList;

public class Account {
    private String userName;
    private String password;
    ArrayList<String> users;
    private String identifier;
    static CSVReadWrite accounts = new CSVReadWrite("users.csv");

    //used for sign up
    public Account(String userName, String password) throws IOException {
        this.userName = userName;
        this.password = password;
    }

    public void getIdentifier() throws IOException {
        accounts.readFile();
        users = accounts.getLines();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).split(",")[1].equals(userName)) {
                this.identifier = users.get(i).split(",")[0];
            }
        }
    }

    public void update() throws IOException {
        accounts.readFile();
        users = accounts.getLines();
    }

    public Boolean signUp() throws IOException {
        if(!checkIfExist(userName, 1)) {
            //got rid of line that made new csvReader since that stopped it from working
            update();
            accounts.append(this.toString());
            update();
            return true;
        } else {
            return false;
        }
    }

    //used for log in
    public Account() throws IOException {
        super();
        accounts.readFile();
        users = accounts.getLines();

    }

    public Boolean logIn(String userName, String password) throws IOException {
        accounts.readFile();
        users = accounts.getLines();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).split(",")[1].equals(userName)) {
                this.userName = userName;
                this.password = password;
                return true;
            }
        }
        return false;
    }

    public boolean checkIfExist(String input, int identifierIndex) throws IOException {
        accounts.readFile();
        users = accounts.getLines();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).split(",")[identifierIndex].equals(input)) {
                return true;
            }
        }
        return false;
    }

    public boolean deleteAccount() throws IOException {
        accounts.removeLine(1, userName);
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).split(",")[1].equals(userName)) {
                users.remove(i);
                break;
            }
        }
        this.userName = null;
        this.password = null;
        return true;
    }

    public Boolean changeUserName(String userName) throws IOException {
        if (checkIfExist(userName, 1)) {
            return false;
        }
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).split(",")[1].equals(this.userName)) {
                this.userName = userName;
                accounts.replaceLine(0, identifier, toString());
                update();
                break;
            }
        }
        return true;
    }

    public Boolean changePassword(String password) throws IOException {
        if (!checkIfExist(userName, 1)) {
            return false;
        }

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).split(",")[1].equals(userName)) {
                this.password = password;
                accounts.replaceLine(0, identifier, toString());
                update();
                break;
            }
        }
        return true;
    }

    public String toString() {
        return String.format("%s,%s", userName, password);
    }

}
