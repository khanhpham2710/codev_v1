package service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import dto.UserDTO;
import entites.UserEntity;
import enums.EGender;
import helpers.PasswordHelper;
import mapper.UserMapper;
import respositories.UserRepository;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class UserService {
    private final UserRepository repository;

    public UserService() {
        this.repository = new UserRepository();
    }

    public UserDTO create(UserEntity entity) {
        if (repository.save(entity)) {
            return UserMapper.toDTO(entity);
        }
        return null;
    }

    public boolean update(UserDTO dto) {

        UserEntity entity = UserMapper.toEntity(dto);

        return repository.update(entity);
    }

    public boolean delete(UUID userId) throws Exception {
        Optional<UserEntity> entity = repository.findById(userId);

        if (entity.isPresent()){
            return repository.delete(userId);
        } else {
            throw new Exception("User not found");
        }
    }

    public UserDTO getProfile(UUID userId) throws SQLException {
        return UserMapper.toDTO(repository.findByIdOrThrow(Objects.requireNonNull(userId))
                .orElseThrow(() -> new IllegalArgumentException("Account no longer exists.")));
    }

    public UserDTO updateProfile(UUID userId, String firstName, String lastName, EGender gender)
            throws SQLException {
        UserDTO user = getProfile(userId);
        String first = validateName(firstName);
        String last = validateName(lastName);
        if (gender == null) throw new IllegalArgumentException("Please select a gender.");
        if (!repository.updateProfile(userId, first, last, gender)) {
            throw new IllegalArgumentException("Profile could not be updated. Please reload and try again.");
        }
        user.setFirstName(first);
        user.setLastName(last);
        user.setGender(gender);
        return user;
    }

    private String validateName(String value) {
        String name = value == null ? "" : value.strip();
        if (name.codePointCount(0, name.length()) > 100) {
            throw new IllegalArgumentException("Each name must be at most 100 characters.");
        }
        return name;
    }

    public void changePassword(UUID userId, char[] currentPassword, char[] newPassword,
                               char[] confirmation) throws SQLException {
        if (currentPassword == null || currentPassword.length == 0) {
            throw new IllegalArgumentException("Enter your current password.");
        }
        if (newPassword == null || newPassword.length < 8) {
            throw new IllegalArgumentException("New password must contain at least 8 characters.");
        }
        if (StandardCharsets.UTF_8.encode(CharBuffer.wrap(newPassword)).remaining() > 72) {
            throw new IllegalArgumentException("New password must be at most 72 UTF-8 bytes.");
        }
        if (!java.util.Arrays.equals(newPassword, confirmation)) {
            throw new IllegalArgumentException("Password confirmation does not match.");
        }
        UserEntity user = repository.findByIdOrThrow(Objects.requireNonNull(userId))
                .orElseThrow(() -> new IllegalArgumentException("Account no longer exists."));
        if (!BCrypt.verifyer().verify(currentPassword, user.getPassWord()).verified) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }
        if (java.util.Arrays.equals(currentPassword, newPassword)) {
            throw new IllegalArgumentException("Choose a different new password.");
        }
        String hash = PasswordHelper.hashPassword(newPassword);
        if (!repository.changePassword(userId, user.getPassWord(), hash)) {
            throw new IllegalArgumentException("Password changed elsewhere. Please try again.");
        }
    }

    public Optional<UserEntity> findByUsername(String userName){

        return repository.findByUsername(userName);
    }
}