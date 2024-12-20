package org.yearup.data.mysql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.yearup.data.ProfileDao;
import org.yearup.models.Profile;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Component
public class MySqlProfileDao extends MySqlDaoBase implements ProfileDao {

    private static final Logger log = LoggerFactory.getLogger(MySqlProfileDao.class);

    public MySqlProfileDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Profile create(Profile profile) {
        String sql = "INSERT INTO profiles (user_id, first_name, last_name, phone, email, address, city, state, zip) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, profile.getUserId());
            ps.setString(2, profile.getFirstName());
            ps.setString(3, profile.getLastName());
            ps.setString(4, profile.getPhone());
            ps.setString(5, profile.getEmail());
            ps.setString(6, profile.getAddress());
            ps.setString(7, profile.getCity());
            ps.setString(8, profile.getState());
            ps.setString(9, profile.getZip());
            ps.executeUpdate();
            log.info("Created profile: {}", profile);
            return profile;
        } catch (SQLException e) {
            log.error("Error creating profile", e);
            return null;
        }
    }

    @Override
    public Optional<Profile> getProfileByUserId(int userId) {

        String sql = "SELECT * FROM profiles WHERE user_id = ?";
        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql);) {
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Profile profile = mapToRow(rs);

                log.info("Retrieved profile: {}", profile);
                return Optional.of(profile);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            log.error("Error getting profile", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Profile> updateProfile(int userId, Profile profile) {

        try(Connection connection = getConnection()) {
            String sql = "UPDATE profiles SET first_name = ?, last_name = ?, phone = ?, email = ?, address = ?, city = ?, state = ?, zip = ? WHERE user_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, profile.getFirstName());
            ps.setString(2, profile.getLastName());
            ps.setString(3, profile.getPhone());
            ps.setString(4, profile.getEmail());
            ps.setString(5, profile.getAddress());
            ps.setString(6, profile.getCity());
            ps.setString(7, profile.getState());
            ps.setString(8, profile.getZip());
            ps.setInt(9, userId);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                log.info("Profile updated: {}", profile);
                return Optional.of(profile);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            log.error("Error updating profile", e);
            return Optional.empty();
        }
    }

    public Profile mapToRow(ResultSet rs) throws SQLException {
        Profile profile = new Profile();
        profile.setUserId(rs.getInt("user_id"));
        profile.setFirstName(rs.getString("first_name"));
        profile.setLastName(rs.getString("last_name"));
        profile.setPhone(rs.getString("phone"));
        profile.setEmail(rs.getString("email"));
        profile.setAddress(rs.getString("address"));
        profile.setCity(rs.getString("city"));
        profile.setState(rs.getString("state"));
        profile.setZip(rs.getString("zip"));
        return profile;
    }

}
