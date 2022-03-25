package com.mycompany.atmmanagementsys;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AccountInfoController implements Initializable {
    
    String UserID;

    @FXML
    private PasswordField oldpass;
    @FXML
    private PasswordField newpass;
    @FXML
    private PasswordField passretype;
    @FXML
    private Label changepassconf;
    @FXML
    Button back;


    public void getUserID(String Id) {
        UserID = Id;
    }
   public void ChangePassword(ActionEvent event) throws SQLException{
       if(!newpass.getText().equals(passretype.getText())){
       changepassconf.setText("Password Confirmation Failed");
       passretype.setText("");
       passretype.setStyle("-fx-border-color:red;-fx-border-width:2;-fx-border-radius:20;-fx-background-radius:22;");
       }
       else if(oldpass.getText().isEmpty()||newpass.getText().isEmpty()||passretype.getText().isEmpty()){
       changepassconf.setText("Please Fill Up Everything Correctly.");
       }
       else{
       Connection con = DbConnection.Connection();
       PreparedStatement ps = con.prepareStatement("UPDATE Users SET Password = ? WHERE User_ID ='"+UserID+"' AND Password ='"+oldpass.getText()+"'");
       ps.setString(1, newpass.getText());
       int i = ps.executeUpdate();
       if(i>0){
       changepassconf.setText("Password Changed.");
       }
       else{
       changepassconf.setText("Wrong Password.");
       }
       oldpass.setText("");
       newpass.setText("");
       passretype.setText("");
       passretype.setStyle("-fx-border-color: #3b5998;-fx-border-width:2;-fx-border-radius:20;-fx-background-radius:22;");
       ps.close();
       con.close();
       }
   }
    public void backPressed(ActionEvent event) throws IOException, SQLException {
        Connection con = DbConnection.Connection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ps = con.prepareStatement("SELECT * FROM Users WHERE User_ID = ?");
        ps.setString(1, UserID);
        rs = ps.executeQuery();
        if (UserID.charAt(0) == 'C') {

            if (rs.next()) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/fxml/UserPage.fxml"));

                loader.load();
                Parent root = loader.getRoot();
                UserPageController upc = loader.getController();
                InputStream is = rs.getBinaryStream("User_Image");
                Image image = new Image("/icons/edituser.png");
                if (is != null) {
                    OutputStream os = new FileOutputStream(new File("photo.jpg"));
                    byte[] content = new byte[1024];
                    int size = 0;
                    while ((size = is.read(content)) != -1) {
                        os.write(content, 0, size);
                    }
                    os.close();
                    is.close();
                    image = new Image("file:photo.jpg", 250, 250, true, true);
                }
                Stage stage = (Stage) back.getScene().getWindow();
                upc.GetUserID(UserID, rs.getString("First_Name") + " " + rs.getString("Last_Name"), rs.getString("Phone_No"), rs.getString("Email"), rs.getString("CNIC"), image);

                stage.setTitle("Customer Page");
                stage.setMaximized(true);
                stage.setResizable(false);
                Scene scene = new Scene(root);
                scene.getStylesheets().add("/styles/UserPage.css");
                stage.setScene(scene);
                stage.show();
            }
            }
        if (UserID.charAt(0) == 'E') {

            if (rs.next()) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/fxml/EmployeePage.fxml"));
                loader.load();
                Parent root = loader.getRoot();
                EmployeePageController upc = loader.getController();
                InputStream is = rs.getBinaryStream("User_Image");
                Image image = new Image("/icons/edituser.png");
                if (is != null) {
                    OutputStream os = new FileOutputStream(new File("photo.jpg"));
                    byte[] content = new byte[1024];
                    int size = 0;
                    while ((size = is.read(content)) != -1) {
                        os.write(content, 0, size);
                    }
                    os.close();
                    is.close();
                    image = new Image("file:photo.jpg", 250, 250, true, true);
                }
                Stage stage = (Stage) back.getScene().getWindow();
                upc.GetUserID(UserID, rs.getString("First_Name") + " " + rs.getString("Last_Name"), rs.getString("Phone_No"), rs.getString("Email"), rs.getString("CNIC"), image);
                stage.setTitle("User Page");
                stage.setMaximized(true);
                stage.setResizable(false);
                Scene scene = new Scene(root);
                scene.getStylesheets().add("/styles/EmployeePage.css");
                stage.setScene(scene);
                stage.show();
            }
        }
        else {

                if (rs.next()) {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/fxml/AdminPage.fxml"));

                    loader.load();
                    Parent root = loader.getRoot();
                    AdminPageController upc = loader.getController();
                    InputStream is = rs.getBinaryStream("User_Image");
                    Image image = new Image("/icons/edituser.png");
                    if (is != null) {
                        OutputStream os = new FileOutputStream(new File("photo.jpg"));
                        byte[] content = new byte[1024];
                        int size = 0;
                        while ((size = is.read(content)) != -1) {
                            os.write(content, 0, size);
                        }
                        os.close();
                        is.close();
                        image = new Image("file:photo.jpg", 250, 250, true, true);
                    }
                    Stage stage = (Stage) back.getScene().getWindow();
                    upc.GetUserID(UserID, rs.getString("First_Name") + " " + rs.getString("Last_Name"), rs.getString("Phone_No"), rs.getString("Email"), rs.getString("CNIC"), image);

                    stage.setTitle("Admin Page");
                    stage.setMaximized(true);
                    stage.setResizable(false);
                    Scene scene = new Scene(root);
                    scene.getStylesheets().add("/styles/AdminPage.css");
                    stage.setScene(scene);
                    stage.show();
                }
            }
            ps.close();
            rs.close();
            con.close();
        }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
