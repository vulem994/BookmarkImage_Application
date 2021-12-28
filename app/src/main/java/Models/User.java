package Models;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private int Id;
    private String Fullname;
    private String DateOfBirth;
    private String Email;
    private String Username;
    private String Password;
    private String Gender;

    private Date TimeCreated;




    //Constructors
    public User() {
        this.TimeCreated = new Date(System.currentTimeMillis());
    }

    public User(int Id, String FullName, String DateOfBirth, String Email, String Password, String Username,String gender) {
        this.Id = Id;
        this.Fullname = FullName;
        this.DateOfBirth = DateOfBirth;
        this.Email = Email;
        this.Password = Password;
        this.Username = Username;
        this.Gender = gender;
    }


    public int GetId() {
        return this.Id;
    }

    public void SetFullname(String fullName) {
        this.Fullname = fullName;
    }

    public String GetFullname() {
        return this.Fullname;
    }

    public void SetDateOfBirth(String dateBirth) {
        this.DateOfBirth = dateBirth;
    }

    public String GetDateOfBirth() {
        return this.DateOfBirth;
    }

    public void SetEmail(String email) {
        this.Email = email;
    }

    public String GetEmail() {
        return this.Email;
    }

    public void SetUsername(String username) {
        this.Username = username;
    }

    public String GetUsername() {
        return this.Username;
    }

    public void SetPassword(String password) {
        this.Password = password;
    }

    public String GetPassword() {
        return this.Password;
    }

    public Date GetTimeCreated() {
        return this.TimeCreated;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }
}//[Class]
