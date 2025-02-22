package dao;

import java.sql.*;
import models.Utilisateur;
import models.Role;
import config.DatabaseConnection;

public class UtilisateurDAOImpl implements UtilisateurDAO {
    
    @Override
    public Utilisateur findByEmail(String email) throws SQLException {
        String query = "SELECT u.*, r.nom as role_nom FROM Utilisateur u " +
                      "JOIN Role r ON u.id_role = r.id_role " +
                      "WHERE u.email = ?";
                      
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Role role = new Role(
                    rs.getInt("id_role"),
                    rs.getString("role_nom")
                );
                
                return new Utilisateur(
                    rs.getInt("id_utilisateur"),
                    rs.getString("email"),
                    rs.getString("mdp"),
                    role
                );
            }
            return null;
        }
    }

    @Override
    public boolean verifyCredentials(String email, String password) throws SQLException {
        String query = "SELECT COUNT(*) as count FROM utilisateur WHERE email = ? AND mdp = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL dans verifyCredentials: " + e.getMessage());
            throw e;
        }
    }
} 