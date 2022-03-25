package com.mycompany.atmmanagementsys;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.jws.soap.SOAPBinding;

public class EditCustomerController implements Initializable {
    @FXML
    private ImageView cusImage;
    @FXML
    private Label custAccNum;
    @FXML
    private Label custFirstName;
    @FXML
    private Label custLastName;
    @FXML
    private Label custCNIC;
    @FXML
    private Label custPhone;
    @FXML
    private Label custEmail;
    @FXML
    private TextField custDOB;
    @FXML
    private TextField custGender;
    @FXML
    private TextField custAddress;
    @FXML
    private TextField custFatherName;
    private RadioButton custActive;
    private RadioButton custInActive;
    private RadioButton custBlocked;
    @FXML
    private TextField cusAddress;
    private File file;
    private FileInputStream fis;
    @FXML
    private Button chosepic;
    int cp;
    @FXML
    private Label confirmation;
    public Button back;
    String Cust_ID;
    String UserID;
    public void getUserID(String userid, String cust_ID) throws SQLException, IOException {
        UserID = userid;
        Cust_ID = cust_ID;

        custActive = new RadioButton("Active");
        custInActive = new RadioButton("InActive");
        custBlocked = new RadioButton("Blocked");
        custActive.setSelected(true);
        custInActive.setSelected(false);
        custBlocked.setSelected(false);
        LoadCusInfo();
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
    @FXML
    public void ChoosePicture(ActionEvent event) {
        FileChooser fc = new FileChooser();
        file = fc.showOpenDialog(null);
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            cusImage.setImage(image);
            cp = 1;

        } else {
            cp = 0;
        }
    }
    @FXML
    public void LoadCusInfo() throws SQLException, FileNotFoundException, IOException {
        try {
            Connection con = DbConnection.Connection();
            PreparedStatement ps = con.prepareStatement("Select * from Users u, Bank_Account b where u.User_ID = ? and u.User_ID = b.Cust_ID");
            ps.setString(1, Cust_ID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                custAccNum.setText(rs.getString("Account_No"));
                custFirstName.setText(rs.getString("First_Name"));
                custLastName.setText(rs.getString("Last_Name"));
                custCNIC.setText(rs.getString("CNIC"));
                custDOB.setText(rs.getString("DOB"));
                custGender.setText(rs.getString("Gender"));
                custFatherName.setText(rs.getString("Father_Name"));
                custPhone.setText(rs.getString("Phone_No"));
                custEmail.setText(rs.getString("Email"));
                custAddress.setText(rs.getString("Address"));
                String temp = rs.getString("Status");
                System.out.println("Status: " + temp);
                if (temp.compareTo("Active") == 0){
                    custActive.setSelected(true);
                    custInActive.setSelected(false);
                    custBlocked.setSelected(false);
                } else if (temp.compareTo("InActive") == 0){
                    custActive.setSelected(false);
                    custInActive.setSelected(true);
                    custBlocked.setSelected(false);
                }
                else if (temp.compareTo("Blocked") == 0){
                    custActive.setSelected(false);
                    custInActive.setSelected(false);
                    custBlocked.setSelected(true);
                }
                InputStream img = rs.getBinaryStream("User_Image");
                OutputStream os = new FileOutputStream(new File("userimage.jpg"));
                byte[] content = new byte[1024];
                int s = 0;
                while ((s = img.read(content)) != -1) {
                    os.write(content, 0, s);
                }
                Image image = new Image("file:userimage.jpg");
                cusImage.setImage(image);
            } else {
                confirmation.setText("Customer Not Found");
            }
            ps.close();
            rs.close();
            con.close();
        } catch (IOException | NumberFormatException | SQLException e) {
            confirmation.setText("Please Enter The ID Correctly");
            System.out.println(String.valueOf(e));
        }
    }

    public boolean checkDOB(String DOB){
        StringTokenizer st = new StringTokenizer(DOB,"-");
        String day = st.nextToken();
        String month = st.nextToken();
        String year = st.nextToken();

        if (day.length() >= 1 && day.length() <= 2 && Integer.parseInt(day) >= 1 && Integer.parseInt(day) <= 31){
            if (month.length() >= 1 && month.length() <= 2 && Integer.parseInt(month) >= 1 && Integer.parseInt(month) <= 12){
                if (year.length() == 4){
                    return true;
                }
            }
        }
        return false;
    }

    public void Active(ActionEvent event){
        System.out.println("In Active");
        custActive.setSelected(true);
        custInActive.setSelected(false);
        custBlocked.setSelected(false);
    }

    public void InActive(ActionEvent event){
        System.out.println("In InActive");
        custActive.setSelected(false);
        custInActive.setSelected(true);
        custBlocked.setSelected(false);
    }

    public void Blocked(ActionEvent event){
        System.out.println("In Blocked");
        custActive.setSelected(false);
        custInActive.setSelected(false);
        custBlocked.setSelected(true);
    }

    public void UpdateCustomerInfo(ActionEvent event) throws SQLException, FileNotFoundException {
        try {
            if (custDOB.getText().isEmpty() || custGender.getText().isEmpty() || custFatherName.getText().isEmpty() || custAddress.getText().isEmpty()) {
                confirmation.setText("Please Fill Up Everything");
            } else if (checkDOB(custDOB.getText()) == false) {
                confirmation.setText("Please Enter The Date of Birth Correctly");
            } else if (!(custGender.getText().compareTo("F") == 0 || custGender.getText().compareTo("M") == 0 || custGender.getText().compareTo("-") == 0)) {
                confirmation.setText("Please Enter The Gender Correctly");
            }else {
                if (cp == 1) {
                    Connection con = DbConnection.Connection();
                    PreparedStatement ps = con.prepareStatement("UPDATE Users SET DOB = ? , Gender = ? , Father_Name = ? , Address = ?, User_Image = ? WHERE User_ID = ?");
                    ps.setString(1, custDOB.getText());
                    ps.setString(2, custGender.getText());
                    ps.setString(3, custFatherName.getText());
                    ps.setString(4, custAddress.getText());
                    fis = new FileInputStream(file);
                    ps.setBinaryStream(5, (InputStream) fis, (int) file.length());
                    ps.setString(6, Cust_ID);
                    int i = ps.executeUpdate();
                    if (i > 0) {
                        ps = con.prepareStatement("UPDATE Bank_Account SET Status = ? WHERE Cust_ID = ?");
                        String temp = "Active";
                        System.out.println("custActive: " + custActive.isSelected());
                        System.out.println("custInActive: " + custInActive.isSelected());
                        System.out.println("custBlocked: " + custBlocked.isSelected());
                        if (custActive.isSelected()){
                            temp = "Active";
                            System.out.println("Status 1 Setting: " + temp);
                        } else if (custInActive.isSelected()){
                            temp = "InActive";
                            System.out.println("Status 1 Setting: " + temp);
                        } else if(custBlocked.isSelected()){
                            temp = "Blocked";
                            System.out.println("Status 1 Setting: " + temp);
                        }
                        ps.setString(1, temp);
                        ps.setString(2, Cust_ID);
                        int j = ps.executeUpdate();
                        if (j > 0){
                            confirmation.setText("Customer Info Updated Successfully");
                        }
                        else{
                            confirmation.setText("Customer Info Not Updated in Bank_Account Table Successfully");
                        }
                    } else {
                        confirmation.setText("Failed To Update Customer Info");
                    }
                    cp = 0;
                    ps.close();
                    con.close();
                } else if (cp != 1) {
                    Connection con = DbConnection.Connection();
                    PreparedStatement ps = con.prepareStatement("UPDATE Users SET DOB = ? , Gender = ? , Father_Name = ? , Address = ? WHERE User_ID = ?");
                    ps.setString(1, custDOB.getText());
                    ps.setString(2, custGender.getText());
                    ps.setString(3, custFatherName.getText());
                    ps.setString(4, custAddress.getText());
                    ps.setString(5, Cust_ID);
                    int i = ps.executeUpdate();
                    if (i > 0) {
                        ps = con.prepareStatement("UPDATE Bank_Account SET Status = ? WHERE Cust_ID = ?");
                        String temp = "Active";
                        System.out.println("custActive: " + custActive.isSelected());
                        System.out.println("custInActive: " + custInActive.isSelected());
                        System.out.println("custBlocked: " + custBlocked.isSelected());
                        if (custActive.isSelected()){
                            temp = "Active";
                            System.out.println("Status 0 Setting: " + temp);
                        } else if (custInActive.isSelected()){
                            temp = "InActive";
                            System.out.println("Status 0 Setting: " + temp);
                        } else if(custBlocked.isSelected()){
                            temp = "Blocked";
                            System.out.println("Status 0 Setting: " + temp);
                        }
                        ps.setString(1, temp);
                        ps.setString(2, Cust_ID);
                        int j = ps.executeUpdate();
                        if (j > 0){
                            confirmation.setText("Customer Info Updated Successfully");
                        }
                        else{
                            confirmation.setText("Customer Info Not Updated in Bank_Account Table Successfully");
                        }
                    } else {
                        confirmation.setText("Failed To Update Customer Info");
                    }
                    cp = 0;
                    ps.close();
                    con.close();
                }
            }
        } catch (FileNotFoundException | NumberFormatException | SQLException e) {
            confirmation.setText("Please Enter Everything Correctly");
            System.out.println(String.valueOf(e));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
