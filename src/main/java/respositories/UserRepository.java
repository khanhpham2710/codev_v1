package respositories;

import entites.UserEntity;
import enums.EGender;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class UserRepository {
    public Optional<UserEntity> findByIdOrThrow(UUID userId) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE id = ?")) {
            ps.setString(1, userId.toString());
            try (ResultSet rs = ps.executeQuery()) {
                return mapDataToEntity(rs);
            }
        }
    }

    public boolean save(UserEntity user) {
        String sql = """
            INSERT INTO users
            (id, username, password, gender, first_name, last_name)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getId().toString());
            ps.setString(2, user.getUserName());
            ps.setString(3, user.getPassWord());
            ps.setString(4, user.getGender().name());
            ps.setString(5, user.getFirstName());
            ps.setString(6, user.getLastName());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Optional<UserEntity> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            return mapDataToEntity(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Optional<UserEntity> findById(UUID userId) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId.toString());

            ResultSet rs = ps.executeQuery();

            return mapDataToEntity(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean update(UserEntity user) {
        String sql = """
                UPDATE users
                SET password = ?, gender = ?, first_name = ?, last_name = ?
                WHERE username = ?
                """;

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getPassWord());
            ps.setString(2, user.getGender().name());
            ps.setString(3, user.getFirstName());
            ps.setString(4, user.getLastName());
            ps.setString(5, user.getUserName());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateProfile(UUID userId, String firstName, String lastName, EGender gender)
            throws SQLException {
        String sql = "UPDATE users SET first_name = ?, last_name = ?, gender = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, gender.name());
            ps.setString(4, userId.toString());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean changePassword(UUID userId, String previousHash, String newHash) throws SQLException {
        // Compare the previous hash to avoid overwriting a concurrent password change.
        String sql = "UPDATE users SET password = ? WHERE id = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newHash);
            ps.setString(2, userId.toString());
            ps.setString(3, previousHash);
            return ps.executeUpdate() == 1;
        }
    }

    public boolean delete(UUID userId) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId.toString());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private Optional<UserEntity> mapDataToEntity(ResultSet rs) throws SQLException {
        if (!rs.next()) {
            return Optional.empty();
        }

        return Optional.of(
                new UserEntity.Builder()
                        .id(UUID.fromString(rs.getString("id")))
                        .userName(rs.getString("username"))
                        .passWord(rs.getString("password"))
                        .gender(EGender.valueOf(rs.getString("gender")))
                        .firstName(rs.getString("first_name"))
                        .lastName(rs.getString("last_name"))
                        .build()
        );
    }
}
