package com.mycompany.atmmanagementsys;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

public class ManageCustomerController implements Initializable {
    @FXML
    private TableView<CustomerData> CustomerTable;
    @FXML
    private TableColumn<CustomerData,String> cusID ;
    @FXML
    private TableColumn<CustomerData,String> cusFirstName ;
    @FXML
    private TableColumn<CustomerData,String> cusDOB ;
    @FXML
    private TableColumn<CustomerData,String> cusGender ;
    @FXML
    private TableColumn<CustomerData,String> cusCNIC ;
    @FXML
    private TableColumn<CustomerData,String> cusFatherName ;
    @FXML
    private TableColumn<CustomerData,String> cusPhone;
    @FXML
    private TableColumn<CustomerData,String> cusEmail ;
    @FXML
    private TableColumn<CustomerData,String> cusAddress ;
    @FXML
    private TableColumn<CustomerData,String> cusUserName ;
    @FXML
    private TableColumn<CustomerData,String> cusPassword ;
    @FXML
    private TableColumn<CustomerData,String> Accountno;
    @FXML

    private TableColumn<CustomerData,String> cusStatus ;
    @FXML
    private TableColumn<CustomerData,String> cusCreationDate ;
    @FXML
    private TableColumn<CustomerData,String> cusCreationTime ;
    private ObservableList<CustomerData> data;
    @FXML
    Button back;
    @FXML
    Button Searchbtn;
    @FXML
    Button Loadbtn;
    @FXML
    Button AddCustomerbtn;
    @FXML
    Button EditCustomerbtn;
    @FXML
    Button DeleteCustomerbtn;
    @FXML
    Label confirmation;
    @FXML
    String UserID;
    public void getUserID(String Id) {
        UserID = Id;
    }

    public void backPressed(ActionEvent event) throws IOException, SQLException {
        Connection con = DbConnection.Connection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ps = con.prepareStatement("SELECT * FROM Users WHERE User_ID = ?");
        ps.setString(1, UserID);
        rs = ps.executeQuery();

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
        ps.close();
        rs.close();
        con.close();
    }

    @FXML
    public void LoadCustomerData(ActionEvent event) throws SQLException{
        Connection con = DbConnection.Connection();
        data = FXCollections.observableArrayList();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM Users u, Bank_Account b WHERE u.User_Type = ? and u.User_ID = b.Cust_ID");
        ps.setString(1, "C");
        ResultSet rs = null;
        try {
            rs = ps.executeQuery();
            while(rs.next()){
                System.out.println("ID: " + rs.getString("Cust_ID"));
                data.add(new CustomerData(rs.getString("Cust_ID"), rs.getString("First_Name"), rs.getString("Last_Name"), rs.getString("User_Name"), rs.getString("Password"), rs.getString("DOB"), rs.getString("Gender"), rs.getString("CNIC"), rs.getString("Father_Name"), rs.getString("Phone_No"), rs.getString("Email"), rs.getString("Address"), rs.getString("Account_No"), rs.getString("Status"), rs.getString("Account_Create_Date"), rs.getString("Account_Create_Time")));
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
        System.out.println(data);
        cusID.setCellValueFactory(new PropertyValueFactory<CustomerData,String>("Id"));
        cusFirstName.setCellValueFactory(new PropertyValueFactory<CustomerData,String>("FirstName"));
        cusUserName.setCellValueFactory(new PropertyValueFactory<CustomerData,String>("UserName"));
        cusPassword.setCellValueFactory(new PropertyValueFactory<CustomerData,String>("Password"));
        cusAddress.setCellValueFactory(new PropertyValueFactory<CustomerData,String>("Address"));
        cusDOB.setCellValueFactory(new PropertyValueFactory<CustomerData,String>("DOB"));
        cusGender.setCellValueFactory(new PropertyValueFactory<CustomerData,String>("Gender"));
        cusCNIC.setCellValueFactory(new PropertyValueFactory<CustomerData,String>("CNIC"));
        cusFatherName.setCellValueFactory(new PropertyValueFactory<CustomerData,String>("FatherName"));
        cusPhone.setCellValueFactory(new PropertyValueFactory<CustomerData,String>("Phone"));
        cusEmail.setCellValueFactory(new PropertyValueFactory<CustomerData,String>("Email"));
        cusAddress.setCellValueFactory(new PropertyValueFactory<CustomerData,String>("Address"));
        Accountno.setCellValueFactory(new PropertyValueFactory<CustomerData,String>("AccountNo"));
        cusStatus.setCellValueFactory(new PropertyValueFactory<CustomerData,String>("Status"));
        cusCreationDate.setCellValueFactory(new PropertyValueFactory<CustomerData,String>("CreationDate"));
        cusCreationTime.setCellValueFactory(new PropertyValueFactory<CustomerData,String>("CreationTime"));

        CustomerTable.setItems(data);
        ps.close();
        rs.close();
        con.close();
    }

    public void SearchCustomer(ActionEvent event) throws IOException{

    }

    public void AddCustomer(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/AddCustomer.fxml"));
        loader.load();
        Parent root = loader.getRoot();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/AddCustomer.css");
        Stage stage = (Stage) back.getScene().getWindow();
        AddCustomerController aic = loader.getController();
        aic.getUserID(UserID);
        stage.setResizable(false);
        stage.setMaximized(true);
        stage.setTitle("Add Customer");
        stage.setScene(scene);
        stage.show();
    }

    public void DeleteCustomer(ActionEvent event) throws IOException, SQLException {
        Connection con = DbConnection.Connection();
        System.out.println("Delete Customer");
        CustomerData customerData = CustomerTable.getSelectionModel().getSelectedItem();
        String Cust_ID = customerData.getId();
        CustomerTable.getItems().removeAll(customerData);
        PreparedStatement ps = con.prepareStatement("DELETE FROM Users WHERE User_ID = ?");
        ps.setString(1, Cust_ID);
        try {
            int rs = ps.executeUpdate();
            if (rs >= 1)
            {
                ps = con.prepareStatement("DELETE FROM Bank_Account WHERE Cust_ID = ?");
                ps.setString(1, Cust_ID);
                int rs2 = ps.executeUpdate();
                if (rs2 >= 1)
                {
                    System.out.println("Customer Deleted");
                    confirmation.setText("Customer Deleted Successfully");
                }
                else{
                    System.out.println("Customer Delete from Users table not the Bank_Account table");
                    confirmation.setText("Customer Not Deleted Successfully from both Table");
                }
            }
            else{
                System.out.println("Customer Not Deleted");
                confirmation.setText("Customer Not Deleted at all");
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public void EditCustomer(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/EditCustomer.fxml"));
        loader.load();
        Parent root = loader.getRoot();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/EditCustomer.css");
        Stage stage = (Stage) back.getScene().getWindow();
        EditCustomerController aic = loader.getController();
        CustomerData customerData = CustomerTable.getSelectionModel().getSelectedItem();
        String Cust_ID = customerData.getId();
            try {
                aic.getUserID(UserID, Cust_ID);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        stage.setResizable(false);
        stage.setMaximized(true);
        stage.setTitle("Edit Customer");
        stage.setScene(scene);
        stage.show();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
