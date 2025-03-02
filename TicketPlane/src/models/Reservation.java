package models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Reservation {
    private int idReservation;
    private String dateReservation;
    private Vol vol;
    private Utilisateur utilisateur;
    private StatutReservation statut;
    private TypeSiege typeSiege;
    private double prixTotal;
    private String codeReservation;

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public String getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(String dateReservation) {
        this.dateReservation = dateReservation;
    }
    
    public Vol getVol() {
            return vol;
    }

    public void setVol(Vol vol) {
            this.vol = vol;
    }

    public StatutReservation getStatut() {
        return statut;
    }

    public void setStatut(StatutReservation statut) {
        this.statut = statut;
    }

    public TypeSiege getTypeSiege() {
        return typeSiege;
    }

    public void setTypeSiege(TypeSiege typeSiege) {
        this.typeSiege = typeSiege;
    }

    public double getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(double prixTotal) {
        this.prixTotal = prixTotal;
    }

    public String getCodeReservation() {
        return codeReservation;
    }

    public void setCodeReservation(String codeReservation) {
        this.codeReservation = codeReservation;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public void create(Connection conn) throws SQLException {
        String sql = "INSERT INTO Reservation (date_reservation, id_vol, id_utilisateur, " +
                    "id_statut, id_type_siege, prix_total, code_reservation) " +
                    "VALUES (CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            this.codeReservation = generateCode();
            pstmt.setInt(1, vol.getIdVol());
            pstmt.setInt(2, utilisateur.getIdUtilisateur());
            pstmt.setInt(3, statut.getIdStatut());
            pstmt.setInt(4, typeSiege.getIdType());
            pstmt.setDouble(5, prixTotal);
            pstmt.setString(6, codeReservation);
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                this.idReservation = rs.getInt(1);
            }
        }
    }

    private String generateCode() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
} 