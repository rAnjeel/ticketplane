package dao;

import java.sql.SQLException;
import models.Utilisateur;

public interface UtilisateurDAO {
    Utilisateur findByEmail(String email) throws SQLException;
    boolean verifyCredentials(String email, String password) throws SQLException;
} 