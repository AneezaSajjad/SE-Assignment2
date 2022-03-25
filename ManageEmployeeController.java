package com.mycompany.atmmanagementsys;

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

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class ManageEmployeeController implements Initializable {
    @FXML
    private TableView<EmployeeData> employeeTable;
    @FXML
    private TableColumn<EmployeeData,String> empID ;
    @FXML
    private TableColumn<EmployeeData,String> empName ;
    @FXML
    private TableColumn<EmployeeData,String> empDOB ;
    @FXML
    private TableColumn<EmployeeData,String> empGender ;
    @FXML
    private TableColumn<EmployeeData,String> empCNIC ;
    @FXML
    private TableColumn<EmployeeData,String> empFName ;
    @FXML
    private TableColumn<EmployeeData,String> empPhone;
    @FXML
    private TableColumn<EmployeeData,String> empEmail ;
    @FXML
    private TableColumn<EmployeeData,String> empAddress ;
    @FXML
    private TableColumn<EmployeeData,String> empPassword ;
    @FXML

    private ObservableList<EmployeeData> data;
    String UserID;
    @FXML
    Button back;
    @FXML
    Button addEmpBtn;
    @FXML
    Button editEmpBtn;
    @FXML
    Label confirmation;
    @FXML
    Button delEmpBtn;
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
    public void addEmployee(ActionEvent event)throws IOException, SQLException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/AddEmployee.fxml"));
        loader.load();
        Parent root = loader.getRoot();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/AdminPage.css");
        Stage stage = (Stage) addEmpBtn.getScene().getWindow();
        AddEmployeeController aic = loader.getController();
        aic.getUserID(UserID);
        stage.setResizable(false);
        stage.setFullScreen(true);
        stage.setFullScreenExitKeyCombination(KeyCombination.keyCombination("q"));

        stage.sizeToScene();
        stage.setTitle("Manage Employee");
        stage.setScene(scene);
        stage.show();
    }
    public void loadEmployeeData(ActionEvent event) throws SQLException{
        Connection con = DbConnection.Connection();
        data = FXCollections.observableArrayList();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM Users WHERE User_Type = ? ");
        ps.setString(1, "E");
        ResultSet rs = null;
        try {
            rs = ps.executeQuery();
            while(rs.next()){
                data.add(new EmployeeData(rs.getString("User_ID"), rs.getString("First_Name"), rs.getString("Last_Name"), rs.getString("User_Name"), rs.getString("Password"), rs.getString("DOB"), rs.getString("Gender"), rs.getString("CNIC"), rs.getString("Father_Name"), rs.getString("Phone_No"), rs.getString("Email"), rs.getString("Address")));
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
        System.out.println(data);
        empID.setCellValueFactory(new PropertyValueFactory<EmployeeData,String>("Id"));
        empName.setCellValueFactory(new PropertyValueFactory<EmployeeData,String>("FirstName"));
        empPassword.setCellValueFactory(new PropertyValueFactory<EmployeeData,String>("Password"));
        empAddress.setCellValueFactory(new PropertyValueFactory<EmployeeData,String>("Address"));
        empDOB.setCellValueFactory(new PropertyValueFactory<EmployeeData,String>("DOB"));
        empGender.setCellValueFactory(new PropertyValueFactory<EmployeeData,String>("Gender"));
        empCNIC.setCellValueFactory(new PropertyValueFactory<EmployeeData,String>("CNIC"));
        empFName.setCellValueFactory(new PropertyValueFactory<EmployeeData,String>("FatherName"));
        empPhone.setCellValueFactory(new PropertyValueFactory<EmployeeData,String>("Phone"));
        empEmail.setCellValueFactory(new PropertyValueFactory<EmployeeData,String>("Email"));
        empAddress.setCellValueFactory(new PropertyValueFactory<EmployeeData,String>("Address"));


        employeeTable.setItems(data);
        ps.close();
        rs.close();
        con.close();
    }

    public void editEmployee(ActionEvent event)throws IOException, SQLException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/EditEmployee.fxml"));
        loader.load();
        Parent root = loader.getRoot();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/EditCustomer.css");
        Stage stage = (Stage) editEmpBtn.getScene().getWindow();
        stage.setResizable(false);
        stage.setMaximized(true);
        EditEmployeeController aic = loader.getController();
        EmployeeData employeeData = employeeTable.getSelectionModel().getSelectedItem();
        aic.getUserID(UserID, employeeData.getId());
        stage.setTitle("Manage Employee");
        stage.setScene(scene);
        stage.show();
    }
    public void deleteEmployee(ActionEvent event)throws IOException, SQLException{
        Connection con = DbConnection.Connection();
        System.out.println("Delete Employee");
        EmployeeData employeeData = employeeTable.getSelectionModel().getSelectedItem();
        String Emp_ID = employeeData.getId();
        employeeTable.getItems().removeAll(employeeData);
        PreparedStatement ps = con.prepareStatement("DELETE FROM Users WHERE User_ID = ?");
        ps.setString(1, Emp_ID);
        try {
            int rs = ps.executeUpdate();
            if (rs >= 1)
            {
                System.out.println("Employee Deleted");
                confirmation.setText("Employee Deleted Successfully");
            }
            else{
                System.out.println("Employee Not Deleted");
                confirmation.setText("Employee  Not Deleted Successfully");
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
