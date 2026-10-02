package entites;

import enums.EGender;

import java.time.Instant;
import java.util.UUID;

public class UserEntity extends BaseEntity {
    private String userName;
    private String passWord;

    private EGender gender = EGender.MALE;

    private String firstName;
    private String lastName;

    private UserEntity(UUID id, Instant createdAt, String userName, String passWord, EGender gender, String firstName, String lastName) {
        super(id, createdAt);

        this.userName = userName;
        this.passWord = passWord;
        this.gender = gender;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    private UserEntity(String userName, String passWord, EGender gender, String firstName, String lastName) {
        super();

        this.userName = userName;
        this.passWord = passWord;
        this.gender = gender;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private UUID id;
        private Instant createdAt;

        private String userName;
        private String passWord;
        private EGender gender = EGender.MALE;
        private String firstName;
        private String lastName;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder passWord(String passWord) {
            this.passWord = passWord;
            return this;
        }

        public Builder gender(EGender gender) {
            this.gender = gender;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserEntity build() {
            return new UserEntity(id, createdAt, userName, passWord, gender, firstName, lastName);
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
