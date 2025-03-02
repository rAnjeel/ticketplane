package models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TarifVol {
    private int idTarif;
    private int idVol;
    private int idTypeSiege;
    private double prix;

    public TarifVol() {}

    public TarifVol(int idTarif, int idVol, int idTypeSiege, double prix) {
        this.idTarif = idTarif;
        this.idVol = idVol;
        this.idTypeSiege = idTypeSiege;
        this.prix = prix;
    }

    // Getters et Setters
    
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
} 