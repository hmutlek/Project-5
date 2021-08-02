import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GuiHandler {
//fields go here
    static boolean canContinue = false;
    JButton WelcomeOne;
    JButton WelcomeTwo;

    public GuiHandler() {
        super();
    }
    public String toReturn(String toReturn) {
        return toReturn;
    }


    public void welcome(Container content, JFrame frame) {

        //welcome buttons
        WelcomeOne = new JButton("Log in");
        WelcomeOne.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //tell server 1 was picked
            }
        });

        WelcomeTwo = new JButton("Sign up");
        WelcomeTwo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //tell server that 2 was selected
            }
        });
        JLabel WelcomeText = new JLabel("Please select an option", SwingConstants.CENTER);
        WelcomeText.setHorizontalAlignment(JLabel.CENTER);

        JPanel south = new JPanel();
        south.add(WelcomeOne);
        south.add(WelcomeTwo);
        frame.add(WelcomeText);
        content.add(south, BorderLayout.SOUTH);
        //making sure an input has been done

    }

    public void getLogin(Container content, JFrame frame) {

        final String[] username = {""};
        final String[] password = {""};
        JTextField passwordField = new JTextField(20);

        JButton enterPass = new JButton("Enter");
        enterPass.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("TEST");
                password[0] = passwordField.getText();
                //send server username and password
                Boolean validUsername = false;
                if (validUsername) {

                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username / password.\n" +
                            "Please a different username / password");
                }
                //move onto next thing
            }
        });

        JLabel passText = new JLabel("Please enter a password", SwingConstants.CENTER);
        passText.setHorizontalAlignment(JLabel.CENTER);
        JPanel south2 = new JPanel();
        south2.add(enterPass);
        south2.add(passwordField);
        frame.add(passText);
        content.add(south2, BorderLayout.SOUTH);

        JButton enterUser = new JButton("Enter");

        JTextField userNameField = new JTextField(20);
        JLabel userText = new JLabel("Please enter a username", SwingConstants.CENTER);
        userText.setHorizontalAlignment(JLabel.CENTER);

        enterUser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //get text if blank give jPanel window that says error
                username[0] = userNameField.getText();
                frame.getContentPane().removeAll();
                frame.revalidate();
                frame.repaint();
                frame.add(passText);
                content.add(south2, BorderLayout.SOUTH);
                frame.validate();
                //clear jFrame and put the new stuff there
            }
        });
        JPanel south1 = new JPanel();
        south1.add(enterUser);
        south1.add(userNameField);
        frame.add(userText);
        content.add(south1, BorderLayout.SOUTH);
    }

    public void accountChoice(Container content, JFrame frame) {
        //three buttons and the text
        JButton changeUser = new JButton("Change Username");
        JButton changePass = new JButton("Change Password");
        JButton deleteUser = new JButton("Delete Account");
        JButton goToConvo = new JButton("Go to Conversations");
        JLabel choiceText = new JLabel("What would you like to do?", SwingConstants.CENTER);
        choiceText.setHorizontalAlignment(JLabel.CENTER);

        changeUser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //tell server go here
            }
        });
        changePass.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //tell server go here
            }
        });
        deleteUser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //tell server go here
            }
        });
        goToConvo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //tell server go here
            }
        });
        JPanel south = new JPanel();
        south.add(changeUser);
        south.add(changePass);
        south.add(deleteUser);
        south.add(goToConvo);
        frame.add(choiceText);
        content.add(south, BorderLayout.SOUTH);
    }

    public void changeUsername(Container content, JFrame frame) {
        JLabel centerText = new JLabel("Please enter new username", SwingConstants.CENTER);
        centerText.setHorizontalAlignment(JLabel.CENTER);
        JTextField newUser = new JTextField(20);
        JButton enter = new JButton("Enter");
        enter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newUsername = newUser.getText();
                if (newUsername.equals("")) {
                    JOptionPane.showMessageDialog(null, "Invalid username", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel south = new JPanel();
        south.add(enter);
        south.add(newUser);
        content.add(south, BorderLayout.SOUTH);
        frame.add(centerText);
    }

    public void changePassword(Container content, JFrame frame) {
        JLabel centerText = new JLabel("Please enter new password", SwingConstants.CENTER);
        centerText.setHorizontalAlignment(JLabel.CENTER);
        JTextField newPass = new JTextField(20);
        JButton enter = new JButton("Enter");
        enter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newUsername = newPass.getText();
                if (newUsername.equals("")) {
                    JOptionPane.showMessageDialog(null, "Invalid password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel south = new JPanel();
        south.add(enter);
        south.add(newPass);
        content.add(south, BorderLayout.SOUTH);
        frame.add(centerText);
    }

    public void deleteAccount(Container content, JFrame frame) {
        JButton delete = new JButton("DELETE");
        //delete.setSize(new Dimension(10,10));
        JButton yes = new JButton("Yes");
        JButton no = new JButton("No");
        JLabel centerText = new JLabel("Are you sure about that?", SwingConstants.CENTER);
        centerText.setHorizontalAlignment(JLabel.CENTER);
        JPanel south = new JPanel();

        yes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();
                frame.revalidate();
                frame.repaint();
                content.add(delete);
                frame.validate();
                //clears frame for the big boy button
            }
        });

        delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //tell server to delete
                JOptionPane.showMessageDialog(null, "Account has been deleted", "It Gone", JOptionPane.WARNING_MESSAGE);
            }
        });

        south.add(yes);
        south.add(no);
        content.add(south, BorderLayout.SOUTH);
        frame.add(centerText);


    }

    public void showConversations(Container content, JFrame frame, String conversations, int size) {
        JButton enter = new JButton("Enter");
        JTextArea conversationSelect = new JTextArea("Enter the conversation you want to view\n" + conversations);
        conversationSelect.setEditable(false);
        conversationSelect.setLineWrap(true);
        conversationSelect.setWrapStyleWord(true);
        JTextField selectField = new JTextField(2);

        JPanel south = new JPanel();
        south.add(enter);
        south.add(selectField);
        content.add(south, BorderLayout.SOUTH);

        frame.add(conversationSelect);

        enter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int select = 0;
                try {
                    select = Integer.parseInt(selectField.getText());
                } catch (NumberFormatException b) {
                    select = -1;
                    //makes it so the next pane is triggered
                }
                if (select < 1 || select > size) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid number",
                            "You Goofed", JOptionPane.ERROR_MESSAGE);
                }
                //send select to server and move to next part
            }
        });
    }

    public void showMessages(Container content, JFrame frame, String conversations) {
        JButton quit = new JButton("Go back to account");
        JButton viewConvo = new JButton("View conversations");
        JButton sendMessage = new JButton("Send Message");
        JButton newConvo = new JButton("New conversation");
        JTextArea messages = new JTextArea( conversations + "\nWhat would you like to do now?");
        messages.setEditable(false);
        messages.setLineWrap(true);
        messages.setWrapStyleWord(true);

        frame.add(messages);
        JPanel south = new JPanel();
        south.add(viewConvo);
        south.add(sendMessage);
        south.add(newConvo);
        south.add(quit);

        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //tell server go here
            }
        });

        viewConvo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //tell server go here
            }
        });

        sendMessage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //tell server go here
            }
        });

        newConvo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //tell server go here
            }
        });
        content.add(south, BorderLayout.SOUTH);


    }

    public void sendMessage(Container content, JFrame frame) {
        JButton enter = new JButton("Enter");
        JTextField message = new JTextField(55);
        JLabel centerMessage = new JLabel("Enter message to send");
        centerMessage.setHorizontalAlignment(JLabel.CENTER);
        enter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String toSend = message.getText();
                //even though text box is 55 message can be as long as the user wants
                //send message to server
                //wait for server to say it was sent
                JOptionPane.showMessageDialog(null, "Message has been sent", "It Sent", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JPanel south = new JPanel();
        south.add(enter);
        south.add(message);
        frame.add(centerMessage);
        content.add(south, BorderLayout.SOUTH);

    }

    public void startConvo(Container content, JFrame frame) {
        JButton add = new JButton("Add");
        JTextField enterUser = new JTextField(30);
        JLabel centerText = new JLabel("Enter username to add");
        centerText.setHorizontalAlignment(JLabel.CENTER);

        JPanel south = new JPanel();
        south.add(add);
        south.add(enterUser);
        content.add(south, BorderLayout.SOUTH);
        frame.add(centerText);

        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String userToAdd = enterUser.getText();
                //send to server
                //boolean exist for testing purposes will be replaced by server output
                boolean validName = false;
                if (validName) {
                    if (JOptionPane.showConfirmDialog(null, "Would you like to add another user?", "Add More?"
                            , JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        //tells server it is going to do this again
                    } else {
                        //tells server to move on to next part
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a valid username!", "Bad Username", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }



    //Container content, JFrame frame
    public static void main(String[] args) {
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
        //first part user either logs in or signs up.
        //run.welcome(params);
        //wait for server response
        //sign in or sign up
        //give four options of what you can do
        //switch statement of choices
            //case 1 change user
            //case 2 change password
            //case 3 delete
            //case 4 go to conversations
                //some logic to see if there are any conversation if none then it will prompt to start one
            run.startConvo(content, frame);
        // wait for server to make sure it gets the right input"test\n\n\n\n\n\n\n\n\n\n\n\n\ndoes it go down"

        //what do now?

    }

}