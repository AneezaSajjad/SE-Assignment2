package com.mycompany.atmmanagementsys;

import java.io.File;
import java.io.FileInputStream;
import java.time.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AddCustomerController implements Initializable {
    @FXML
    private TextField cusFirstName;
    @FXML
    private TextField cusLastName;
    @FXML
    private TextArea cusAddress;
    @FXML
    private TextField cusPhone;
    @FXML
    private TextField cusGender;
    @FXML
    private TextField cusCNIC;
    @FXML
    private TextField cusFatherName;
    @FXML
    private Button Add;
    @FXML
    private Label Addcusconfirm;
    @FXML
    private TextField cusEmail;
    @FXML
    private TextField cusBalance;
    @FXML
    private TextField cusUserName;
    @FXML
    private CheckBox ShowPasswordcbox;
    @FXML
    private Button browse;
    private File file;
    @FXML
    private DatePicker cusDOB;
    @FXML
    private ImageView userimage;
    @FXML
    Button back;
    @FXML
    private PasswordField cusPass;
    private FileInputStream fis;
    int cf;
    String UserID;
    String Password = "";
    public void getUserID(String Id) {
        UserID = Id;
    }
    public void backPressed(ActionEvent event) throws IOException, SQLException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/ManageCustomer.fxml"));
        loader.load();
        Parent root = loader.getRoot();
        ManageCustomerController upc = loader.getController();
        upc.getUserID(UserID);
        Stage stage = (Stage) back.getScene().getWindow();
        stage.setTitle("Admin Page");
        stage.setMaximized(true);
        stage.setResizable(false);
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/AdminPage.css");
        stage.setScene(scene);
        stage.show();
    }

    public void ChooseFile(ActionEvent event) {
        FileChooser fc = new FileChooser();
        file = fc.showOpenDialog(null);
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            userimage.setImage(image);
            cf = 1;
        } else {
            cf = 0;
        }
    }
    public void ShowPassword(ActionEvent event) throws SQLException, FileNotFoundException {
        if (ShowPasswordcbox.isSelected())
        {
            Password = cusPass.getText();
            cusPass.clear();
            cusPass.setPromptText(Password);
        }
        else
        {
            cusPass.setText(Password);
            cusPass.setPromptText("Password");
        }
    }
    public boolean EmailValidation(String email){
        for (int i = 0; email.charAt(i) != '\0'; i++){
            if (email.charAt(i) == '@') {
                return true;
            }
        }
        return false;
    }
    public String setDateStraight(String dob){
        StringTokenizer st = new StringTokenizer(dob, "-");
        String year = st.nextToken();
        String month = st.nextToken();
        String day = st.nextToken();

        return day + "-" + month + "-" + year;
    }
    @FXML
    public void AddCustomer(ActionEvent event) throws SQLException, FileNotFoundException {
        try {
            System.out.println("First Name: " + cusFirstName.getText());
            System.out.println("Last Name: " + cusLastName.getText());
            System.out.println("cusDOB: " + cusDOB.getValue());
            System.out.println("Gender: " + cusGender.getText());
            System.out.println("CNIC: " + cusCNIC.getText());
            System.out.println("Father Name: " + cusFatherName.getText());
            System.out.println("Balance: " + cusBalance.getText());
            System.out.println("Phone no: " + cusPhone.getText());
            System.out.println("Email: " + cusEmail.getText());
            System.out.println("Address: " + cusAddress.getText());
            System.out.println("User Name: " + cusUserName.getText());
            System.out.println("Password: " + cusPass.getText());

            String dob = cusDOB.getValue() + "";
            cusPass.setText(Password);
            cusPass.setPromptText("Password");
            ShowPasswordcbox.setSelected(false);

            if (dob == "" || cusPass.getText().isEmpty() || cusFirstName.getText().isEmpty() || cusLastName.getText().isEmpty() || cusAddress.getText().isEmpty() || cusEmail.getText().isEmpty() || cusPhone.getText().isEmpty() || cusBalance.getText().isEmpty() || cusGender.getText().isEmpty() || cusCNIC.getText().isEmpty() || cusUserName.getText().isEmpty() || cusFatherName.getText().isEmpty()) {
                Addcusconfirm.setText("Please Fill Up Everything");
            } else if (Integer.parseInt(cusBalance.getText()) < 500) {
                Addcusconfirm.setText("Minimum cusBalance Requirement Is 500 Tk.");
            }
            else if (!((cusGender.getText().equals( "M")) || (cusGender.getText().equals("F") ) || (cusGender.getText().equals("-") )) ){
                Addcusconfirm.setText("Please enter gender as F, M or - Tk");
            }
            else if (EmailValidation(cusEmail.getText()) == false){
                Addcusconfirm.setText("Please Enter Correct Email Address Tk");
            }
            else{
                if (cf != 1) {
                    Addcusconfirm.setText("Please Upload An Image To Add New Customer");
                } else if (cf == 1) {
                    try {
                        int ID = 0;
                        Connection con = DbConnection.Connection();
                        String query = "select * from Users";
                        try (Statement stmt = con.createStatement()) {
                            ResultSet rs = stmt.executeQuery(query);
                            while(rs.next()){
                                String userId = rs.getString("User_ID");
                                System.out.println("UserID: " + userId);
                                StringTokenizer st = new StringTokenizer(userId, "-");
                                String str = st.nextToken();
                                ID = Integer.parseInt(st.nextToken());
                            }
                            rs.close();
                        } catch (SQLException e) {
                            System.out.println(String.valueOf(e));
                            System.out.printf("Get ID Query didn't work");
                        }

                        // Insertion into Users Table here
                        PreparedStatement ps = con.prepareStatement("INSERT INTO Users VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                        ps.setString(1, "C-000" + String.valueOf(ID + 1));
                        ps.setString(2, "C");
                        ps.setString(3, cusFirstName.getText());
                        ps.setString(4, cusLastName.getText());
                        ps.setString(5, setDateStraight(dob));
                        ps.setString(6, cusGender.getText());
                        ps.setString(7, cusCNIC.getText());
                        ps.setString(8, cusFatherName.getText());
                        ps.setString(9, cusPhone.getText());
                        ps.setString(10, cusEmail.getText());
                        ps.setString(11, cusAddress.getText());
                        ps.setString(12, cusUserName.getText());
                        ps.setString(13, cusPass.getText());

                        fis = new FileInputStream(file);
                        ps.setBinaryStream(14, (InputStream) fis, (int) file.length());
                        int i = ps.executeUpdate(); // executing query here

                        String balance = cusBalance.getText();
                        String cusID = "C-000" + String.valueOf(ID + 1);    // making new ID for Bank_Account Table
                        int min = 1;
                        int max = 10000;
                        int random_int1 = (int)Math.floor(Math.random()*(max-min+1)+min);
                        int random_int2 = (int)Math.floor(Math.random()*(max-min+1)+min);
                        String Account_No = String.valueOf(ID + 1) + "-" + String.valueOf(random_int1) + "-" + String.valueOf(random_int2);
                        String Status = "Active";
                        String Account_Create_Date = setDateStraight(dob);
                        String Account_Create_Time = LocalTime.now() + "";
                        System.out.println("Account_Create_Date: " + Account_Create_Date);
                        System.out.println("Account_Create_Time: " + Account_Create_Time);
                        if (i > 0) {    // successful in inserting customer to user table
                            // clearing the txt fields
                            cusFirstName.setText("");
                            cusLastName.setText("");
                            cusDOB.setValue(LocalDate.now());
                            cusGender.setText("");
                            cusEmail.setText("");
                            cusCNIC.setText("");
                            cusUserName.setText("");
                            cusPhone.setText("");
                            cusPass.setText("");
                            cusFatherName.setText("");
                            cusBalance.setText("");
                            cusAddress.setText("");
                            ShowPasswordcbox.setSelected(false);

                            // Inserting now in Bank account table
                            ps = con.prepareStatement("INSERT INTO Bank_Account VALUES (?,?,?,?,?,?)");
                            ps.setString(1, cusID);
                            ps.setString(2, Account_No);
                            ps.setString(3, balance);
                            ps.setString(4, Status);
                            ps.setString(5, Account_Create_Date);
                            ps.setString(6, Account_Create_Time);

                            i = ps.executeUpdate(); // executing query here
                            if (i > 0){
                                cf = 0;
                                Image image = new Image("/icons/usericon1.png");
                                userimage.setImage(image);
                                Addcusconfirm.setText("New Customer Added Successfully");
                            }else{
                                Addcusconfirm.setText("New Customer Added in Users table but not in bank account table Successfully");
                            }
                        } else {
                            Addcusconfirm.setText("Failed To Add New Customer in Users Table");
                        }
                        ps.close();
                        con.close();
                    } catch (FileNotFoundException | NumberFormatException | SQLException e) {
                        Addcusconfirm.setText("Invalid Customer ID or ID Is Not Available");
                        System.out.println(String.valueOf(e));
                    }
                }
            }
        } catch (NumberFormatException e) {
            Addcusconfirm.setText("Please Enter Everything Correctly");
            System.out.println(String.valueOf(e));
        }

    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
