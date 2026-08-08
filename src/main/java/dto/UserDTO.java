package dto;

import enums.EGender;

import java.util.UUID;

public class UserDTO {
    private UUID id;

    private String userName;

    private EGender gender;

    private String firstName;

    private String lastName;

    public UUID getId(){
        return id;
    }

    public void setId(UUID userId) { this.id = userId; };

    public String getUserName() {
        return userName;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }


    public EGender getGender() {
        return gender;
    }


    public void setGender(EGender gender) {
        this.gender = gender;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
