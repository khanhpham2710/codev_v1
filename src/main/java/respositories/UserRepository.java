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
    public boolean save(UserEntity user) {
        if (user.getId() == null) {
            user.setId(UUID.randomUUID());
        }

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
