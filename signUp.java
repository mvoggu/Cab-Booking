import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
public class signUp{
    JFrame register = new JFrame("Sign Up");
    
    JLabel userNameText = new JLabel("Full Name: "); 
    JLabel passwordText = new JLabel("Password: ");
    JLabel emailIDText = new JLabel("Email ID: ");
    JLabel phoneNumText = new JLabel("Phone No: ");
    
    JTextField userName = new JTextField();
    JPasswordField password = new JPasswordField();
    JTextField emailID = new JTextField();
    JTextField phoneNum = new JTextField();
    
    JButton goToLoginPage = new JButton("Login Page");
    JButton signUpButton = new JButton("Submit");
    signUp(){
        userNameText.setBounds(0, 0, 110, 50);
        userName.setBounds(80, 0, 150, 50);
        passwordText.setBounds(0, 50, 110, 50);
        password.setBounds(80, 50, 150, 50);
        emailIDText.setBounds(0, 100, 110, 50);
        emailID.setBounds(80, 100, 150, 50);
        phoneNumText.setBounds(0, 150, 110, 50);
        phoneNum.setBounds(80, 150, 150, 50);
        goToLoginPage.setBounds(0, 200, 110, 50);
        signUpButton.setBounds(110,200,100,50);
        register.add(userNameText);
        register.add(passwordText);
        register.add(emailIDText);
        register.add(phoneNumText);
        register.add(userName);
        register.add(password);
        register.add(emailID);
        register.add(phoneNum);
        register.add(signUpButton);
        register.add(goToLoginPage);

        register.setExtendedState(JFrame.MAXIMIZED_BOTH);
        register.setLayout(null);
        // register.setVisible(true);
        register.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        register.getRootPane().setDefaultButton(signUpButton);
        goToLoginPage.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                login loginpage = new login();
                register.setVisible(false);
                loginpage.loginPage.setVisible(true);
            }
        });

        signUpButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "mysecretpass");
                    Statement stmt = conn.createStatement();
                    stmt.execute("USE CABBOOKING");
                    stmt.execute("INSERT INTO USERS(USERNAME,PASSWORD,PHONENUM,EMAILID) VALUES(\""+userName.getText()+"\",\""+new String(password.getPassword())+"\","+phoneNum.getText()+",\""+emailID.getText()+"\");");
                    ResultSet userIDSet = stmt.executeQuery("SELECT LAST_INSERT_ID()");//adding user details to database
                    String userID = "";
                    if(userIDSet.next()){
                        userID = userIDSet.getString("LAST_INSERT_ID()");
                    }
                    conn.close();
                    login loginpage = new login();
                    register.setVisible(false);
                    loginpage.loginPage.setVisible(true);
                    ToastMessage toastMessage = new ToastMessage("SIGN UP SUCCESS,Your userID: "+userID,3000);
                    toastMessage.setVisible(true);
                } catch (Exception exception) {
                    System.out.println(exception);
                    ToastMessage toastMessage = new ToastMessage("Databases error, check connection and inputs",3000);
                    toastMessage.setVisible(true);
                }
            }
        });
    }
}
