package models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import mg.itu.prom16.Annotations.*;

public class TarifVol {
    @RequestField("id_tarif")
    private int idTarif;

    @RequestField("id_vol")
    private int idVol;

    @RequestField("id_typesiege")
    private int idTypeSiege;

    @RequestField("prix")
    private double prix;

    public TarifVol() {}

    public TarifVol(int idTarif, int idVol, int idTypeSiege, double prix) {
        this.idTarif = idTarif;
        this.idVol = idVol;
        this.idTypeSiege = idTypeSiege;
        this.prix = prix;
    }

    // Getters et Setters
    
    public int getIdTarif() { return idTarif; }
    public void setIdTarif(int idTarif) { this.idTarif = idTarif; }
    public int getIdVol() { return idVol; }
    public void setIdVol(int idVol) { this.idVol = idVol; }
    public int getIdTypeSiege() { return idTypeSiege; }
    public void setIdTypeSiege(int idTypeSiege) { this.idTypeSiege = idTypeSiege; }
    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }
    
    public static List<TarifVol> getTarifsByVol(Connection conn, int idVol) throws SQLException {
        List<TarifVol> tarifs = new ArrayList<>();
        String sql = "SELECT * FROM TarifVol WHERE id_vol = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idVol);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tarifs.add(new TarifVol(
                    rs.getInt("id_tarif"),
                    rs.getInt("id_vol"),
                    rs.getInt("id_type_siege"),
                    rs.getDouble("prix")
                ));
            }
        }
        return tarifs;
    }
    
    public static TarifVol getTarifByVolAndType(Connection conn, int idVol, int idTypeSiege) throws SQLException {
        String sql = "SELECT * FROM TarifVol WHERE id_vol = ? AND id_type_siege = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idVol);
            pstmt.setInt(2, idTypeSiege);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new TarifVol(
                    rs.getInt("id_tarif"),
                    rs.getInt("id_vol"),
                    rs.getInt("id_type_siege"),
                    rs.getDouble("prix")
                );
            }
        }
        return null;
    }

    /**
     * Enregistre ce tarif de vol dans la base de données
     * @param conn La connexion à la base de données
     * @return true si l'opération a réussi
     * @throws SQLException En cas d'erreur SQL
     */
    public boolean create(Connection conn) throws SQLException {
        String sql = "INSERT INTO TarifVol (id_vol, id_type_siege, prix) VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, this.getIdVol());
            pstmt.setInt(2, this.getIdTypeSiege());
            pstmt.setDouble(3, this.getPrix());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        this.setIdTarif(rs.getInt(1));
                        return true;
                    }
                }
            }
        }
        return false;
    }
} 