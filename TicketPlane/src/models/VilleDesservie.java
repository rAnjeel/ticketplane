package models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import mg.itu.prom16.Annotations.*;

public class VilleDesservie {
    @RequestField("id_ville")
    private int idVille;

    @RequestField("nom")
    private String nom;

    @RequestField("pays")
    private String pays;

    @RequestField("code_aeroport")
    private int codeAeroport;

    public VilleDesservie() {}

    // Constructeur
    public VilleDesservie(String nom, String pays, int codeAeroport) {
        this.nom = nom;
        this.pays = pays;
        this.codeAeroport = codeAeroport;
    }

    // Getters et Setters
    public int getIdVille() { return idVille; }
    public void setIdVille(int idVille) { this.idVille = idVille; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPays() { return pays; }
    public void setPays(String pays) { this.pays = pays; }
    public int getCodeAeroport() { return codeAeroport; }
    public void setCodeAeroport(int codeAeroport) { this.codeAeroport = codeAeroport; }

    // Méthodes CRUD
    public void create(Connection conn) throws SQLException {
        String sql = "INSERT INTO VilleDesservie (nom, pays, code_aeroport) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, nom);
            pstmt.setString(2, pays);
            pstmt.setInt(3, codeAeroport);
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                this.idVille = rs.getInt(1);
            }
        }
    }

    public static VilleDesservie read(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM VilleDesservie WHERE id_ville = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                VilleDesservie ville = new VilleDesservie(
                    rs.getString("nom"),
                    rs.getString("pays"),
                    rs.getInt("code_aeroport")
                );
                ville.setIdVille(rs.getInt("id_ville"));
                return ville;
            }
        }
        return null;
    }

    public void update(Connection conn) throws SQLException {
        String sql = "UPDATE VilleDesservie SET nom = ?, pays = ?, code_aeroport = ? WHERE id_ville = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nom);
            pstmt.setString(2, pays);
            pstmt.setInt(3, codeAeroport);
            pstmt.setInt(4, idVille);
            pstmt.executeUpdate();
        }
    }

    public void delete(Connection conn) throws SQLException {
        String sql = "DELETE FROM VilleDesservie WHERE id_ville = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idVille);
            pstmt.executeUpdate();
        }
    }

    public static List<VilleDesservie> getAll(Connection conn) throws SQLException {
        List<VilleDesservie> villes = new ArrayList<>();
        String sql = "SELECT * FROM VilleDesservie";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                VilleDesservie ville = new VilleDesservie(
                    rs.getString("nom"),
                    rs.getString("pays"),
                    rs.getInt("code_aeroport")
                );
                ville.setIdVille(rs.getInt("id_ville"));
                villes.add(ville);
            }
        }
        return villes;
    }
} 