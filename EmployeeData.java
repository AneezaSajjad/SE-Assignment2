
package com.mycompany.atmmanagementsys;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EmployeeData {
    private final StringProperty cusID;
    private final StringProperty cusFirstName;
    private final StringProperty cusLastName;
    private final StringProperty cusUserName;
    private final StringProperty cusPassword;
    private final StringProperty cusDOB;
    private final StringProperty cusGender;
    private final StringProperty cusCNIC;
    private final StringProperty cusFatherName;
    private final StringProperty cusPhone;
    private final StringProperty cusEmail;
    private final StringProperty cusAddress;



    public EmployeeData(String ID,
                        String FirstName,
                        String LastName,
                        String UserName,
                        String Password,
                        String DOB,
                        String Gender,
                        String CNIC,
                        String FatherName,
                        String Phone,
                        String Email,
                        String Address

    ) {
        this.cusID = new SimpleStringProperty(ID);
        this.cusFirstName = new SimpleStringProperty(FirstName);
        this.cusLastName = new SimpleStringProperty(LastName);
        this.cusUserName = new SimpleStringProperty(UserName);
        this.cusPassword = new SimpleStringProperty(Password);
        this.cusDOB = new SimpleStringProperty(DOB);
        this.cusGender = new SimpleStringProperty(Gender);
        this.cusCNIC = new SimpleStringProperty(CNIC);
        this.cusFatherName = new SimpleStringProperty(FatherName);
        this.cusPhone = new SimpleStringProperty(Phone);
        this.cusEmail = new SimpleStringProperty(Email);
        this.cusAddress = new SimpleStringProperty(Address);

    }


    public String getId(){
        return cusID.get();
    }
    public String getFirstName(){
        return cusFirstName.get()+" "+cusLastName.get();
    }
    public String getUserName(){
        return cusUserName.get();
    }
    public String getPassword(){
        return cusPassword.get();
    }
    public String getDOB(){
        return cusDOB.get();
    }
    public String getGender(){
        return cusGender.get();
    }
    public String getCNIC(){
        return cusCNIC.get();
    }
    public String getFatherName(){
        return cusFatherName.get();
    }
    public String getPhone(){
        return cusPhone.get();
    }
    public String getEmail(){
        return cusEmail.get();
    }
    public String getAddress(){
        return cusAddress.get();
    }

}
