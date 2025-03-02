package models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Modele {
    private int idModele;
    private String nom;

    // Constructeur
    public Modele(int idModele, String nom) {
        this.idModele = idModele;
        this.nom = nom;
    }

    // Getters et Setters
    public int getIdModele() {
        return idModele;
    }

    public void setIdModele(int idModele) {
        this.idModele = idModele;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    // Méthodes CRUD
    public void create(Connection conn) throws SQLException {
        String sql = "INSERT INTO Modele (nom) VALUES (?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, nom);
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                this.idModele = rs.getInt(1);
            }
        }
    }

    public static Modele read(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM Modele WHERE id_modele = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Modele(
                    rs.getInt("id_modele"),
                    rs.getString("nom")
                );
            }
        }
        return null;
    }

    public void update(Connection conn) throws SQLException {
        String sql = "UPDATE Modele SET nom = ? WHERE id_modele = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nom);
            pstmt.setInt(2, idModele);
            pstmt.executeUpdate();
        }
    }

    public void delete(Connection conn) throws SQLException {
        String sql = "DELETE FROM Modele WHERE id_modele = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idModele);
            pstmt.executeUpdate();
        }
    }

    public static List<Modele> getAll(Connection conn) throws SQLException {
        List<Modele> modeles = new ArrayList<>();
        String sql = "SELECT * FROM Modele";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Modele modele = new Modele(
                    rs.getInt("id_modele"),
                    rs.getString("nom")
                );
                modeles.add(modele);
            }
        }
        return modeles;
    }
} 