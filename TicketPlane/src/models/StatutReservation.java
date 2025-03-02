package models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StatutReservation {
    private int idStatut;
    private String nom;

    public StatutReservation() {}

    public StatutReservation(int idStatut, String nom) {
        this.idStatut = idStatut;
        this.nom = nom;
    }

    // Getters et Setters
    public int getIdStatut() { return idStatut; }
    public void setIdStatut(int idStatut) { this.idStatut = idStatut; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public static StatutReservation read(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM StatutReservation WHERE id_statut = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new StatutReservation(
                    rs.getInt("id_statut"),
                    rs.getString("nom")
                );
            }
        }
        return null;
    }

    public static List<StatutReservation> getAll(Connection conn) throws SQLException {
        List<StatutReservation> statuts = new ArrayList<>();
        String sql = "SELECT * FROM StatutReservation";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                statuts.add(new StatutReservation(
                    rs.getInt("id_statut"),
                    rs.getString("nom")
                ));
            }
        }
        return statuts;
    }
} 