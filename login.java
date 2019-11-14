import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class login{
    JFrame loginPage = new JFrame("Login"); // JFrame means a window
    JLabel userIDText = new JLabel("Enter UserID:"); //JLabel is a view-only text
    JTextField userID = new JTextField(); //JTextField is an input field
    JLabel passwordText = new JLabel("Enter Password:");
    JPasswordField password = new JPasswordField(); //JPasswordField is a password input field
    JLabel newUserText = new JLabel("New User ?"); 
    JButton logIn = new JButton("Log In");
    JButton signUpButton = new JButton("Sign Up");
    login(){

        //Set Positions of Components inside Jframe
        userIDText.setBounds(0,0,150,50);
        userID.setBounds(150,0,100,50);
        passwordText.setBounds(0, 50, 150, 50);
        password.setBounds(150, 50, 100, 50);
        logIn.setBounds(150,100,100,50);
        newUserText.setBounds(30, 150, 150, 50);
        signUpButton.setBounds(150, 150, 100, 50);

        //Add components to the JFrame
        loginPage.add(logIn);
        loginPage.add(userIDText);
        loginPage.add(userID);
        loginPage.add(passwordText);
        loginPage.add(password);
        loginPage.add(newUserText);
        loginPage.add(signUpButton);

        loginPage.setLayout(null); //Remove the layout which is present by default
        loginPage.setExtendedState(JFrame.MAXIMIZED_BOTH); //Make JFrame full screen
        //Optional Code
        // loginPage.setSize(1000, 1000); // Set size instead of full screen
        // loginPage.setUndecorated(true); //Make completely full screen(no buttons)
        // loginPage.setLocationRelativeTo(null); //Move JFrame to centre
        loginPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Make program exit when window is Closed(X pressed)
        loginPage.getRootPane().setDefaultButton(logIn);// Press button when return key is pressed
        // loginPage.setVisible(true); //Show JFrame

        logIn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "mysecretpass");//connecting to database
                    Statement stmt = conn.createStatement();
                    stmt.execute("USE CABBOOKING");
                    ResultSet rs = stmt.executeQuery("SELECT PASSWORD FROM USERS WHERE USERID=\""+userID.getText()+"\"");
                    boolean loginSuccess = false;//checking the identity of user
                    while(rs.next()){
                        if(new String(password.getPassword()).equals(rs.getString("PASSWORD"))){
                            book myBooking = new book(userID.getText());
                            loginPage.setVisible(false);
                            myBooking.bookingPage.setVisible(true);
                            loginSuccess = true;
                        }
                    }
                    if(!loginSuccess){
                        ToastMessage toastMessage = new ToastMessage("Incorrect UserID or Password",3000);
                        toastMessage.setVisible(true);
                    }
                    conn.close();
                } catch (Exception exception) {
                    System.out.println(exception);
                }
            }
        });

        signUpButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                signUp mySignUp = new signUp();
                loginPage.setVisible(false);
                mySignUp.register.setVisible(true);
            }
        });
    }
    public static void main(String[] args) {
        login MyLoginPage = new login();
        MyLoginPage.loginPage.setVisible(true);
    }
}
