package entites;

import enums.EGender;

import java.util.UUID;

public class UserEntity {
    private UUID id;
    private String userName;
    private String passWord;

    private EGender gender = EGender.MALE;

    private String firstName;
    private String lastName;


    private UserEntity(
            String userName,
            String passWord,
            EGender gender,
            String firstName,
            String lastName
    ){

        this.userName = userName;
        this.passWord = passWord;
        this.gender = gender;
        this.firstName = firstName;
        this.lastName = lastName;

        validate();
    }


    private void validate(){

        if(userName == null || userName.isBlank()){
            throw new IllegalArgumentException(
                    "Username is required"
            );
        }


        if(passWord == null || passWord.isBlank()){
            throw new IllegalArgumentException(
                    "Password is required"
            );
        }


        if(userName.length() < 4){
            throw new IllegalArgumentException(
                    "Username min 4 characters"
            );
        }

    }



    public static class Builder {


        private String userName;

        private String passWord;

        private EGender gender = EGender.MALE;

        private String firstName;

        private String lastName;



        public Builder userName(String userName){

            this.userName = userName;

            return this;
        }


        public Builder passWord(String passWord){

            this.passWord = passWord;

            return this;
        }


        public Builder gender(EGender gender){

            this.gender = gender;

            return this;
        }


        public Builder firstName(String firstName){

            this.firstName = firstName;

            return this;
        }


        public Builder lastName(String lastName){

            this.lastName = lastName;

            return this;
        }



        public UserEntity build(){

            return new UserEntity(
                    userName,
                    passWord,
                    gender,
                    firstName,
                    lastName
            );
        }

    }



    public String getUserName() {
        return userName;
    }


    public String getPassWord() {
        return passWord;
    }


    public EGender getGender() {
        return gender;
    }


    public String getFirstName() {
        return firstName;
    }


    public String getLastName() {
        return lastName;
    }

}