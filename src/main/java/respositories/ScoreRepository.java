package respositories;

import com.mysql.cj.protocol.Resultset;
import entites.ScoreEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ScoreRepository {

    public boolean save(ScoreEntity score) {
        String sql = """
            INSERT INTO scores(id, quiz_id, user_id, score, created_at)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, score.getId().toString());
            ps.setString(2, score.getQuizId());
            ps.setString(3, score.getUserId().toString());
            ps.setInt(4, score.getScore());
            ps.setTimestamp(5, Timestamp.from(score.getCreatedAt()));

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<ScoreEntity> findByUserId(UUID userId) {
        List<ScoreEntity> scores = new ArrayList<>();

        String sql = "SELECT * FROM scores WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId.toString());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ScoreEntity score = new ScoreEntity.Builder()
                        .id(UUID.fromString(rs.getString("id")))
                        .quizId(rs.getString("quiz_id"))
                        .userId(UUID.fromString(rs.getString("user_id")))
                        .score(rs.getInt("score"))
                        .build();

                scores.add(score);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return scores;
    }
}
