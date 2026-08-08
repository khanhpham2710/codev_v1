package config;

import dto.UserDTO;
import entites.UserEntity;
import mapper.UserMapper;

import java.util.UUID;

public class CurrentUser {

    private static final CurrentUser INSTANCE = new CurrentUser();

    private UserDTO userDTO;

    private CurrentUser() {
    }

    public static CurrentUser getInstance() {
        return INSTANCE;
    }

    public void login(UserEntity userEntity) {
        this.userDTO = UserMapper.toDTO(userEntity);
    }

    public void login(UserDTO userDTO){
        this.userDTO = userDTO;
    }

    public void logout() {
        this.userDTO = null;
    }

    public UserDTO getUser() {
        return userDTO;
    }

    public UUID getCurrentUserId() {
        if (userDTO == null) {
            throw new IllegalStateException("No user is logged in.");
        }
        return userDTO.getId();
    }

    public boolean isLoggedIn() {
        return userDTO != null;
    }
}
