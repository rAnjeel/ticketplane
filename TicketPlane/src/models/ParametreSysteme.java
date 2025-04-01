package models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParametreSysteme {
    private int id;
    private String code;
    private String valeur;
    private String description;
    private Timestamp dateModification;

    // Constructeurs
    public ParametreSysteme() {}

    public ParametreSysteme(int id, String code, String valeur, String description, Timestamp dateModification) {
        this.id = id;
        this.code = code;
        this.valeur = valeur;
        this.description = description;
        this.dateModification = dateModification;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public String getValeur() { return valeur; }
    public void setValeur(String valeur) { this.valeur = valeur; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Timestamp getDateModification() { return dateModification; }
    public void setDateModification(Timestamp dateModification) { this.dateModification = dateModification; }

    // Méthodes d'accès à la base de données
    public static ParametreSysteme getByCode(Connection conn, String code) throws SQLException {
        String sql = "SELECT * FROM parametres_systeme WHERE code = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, code);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new ParametreSysteme(
                        rs.getInt("id"),
                        rs.getString("code"),
                        rs.getString("valeur"),
                        rs.getString("description"),
                        rs.getTimestamp("date_modification")
                    );
                }
            }
        }
        return null;
    }

    public static List<ParametreSysteme> getAll(Connection conn) throws SQLException {
        List<ParametreSysteme> parametres = new ArrayList<>();
        String sql = "SELECT * FROM parametres_systeme ORDER BY code";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                parametres.add(new ParametreSysteme(
                    rs.getInt("id"),
                    rs.getString("code"),
                    rs.getString("valeur"),
                    rs.getString("description"),
                    rs.getTimestamp("date_modification")
                ));
            }
        }
        return parametres;
    }

    public void save(Connection conn) throws SQLException {
        if (this.id > 0) {
            // Mise à jour
            String sql = "UPDATE parametres_systeme SET valeur = ?, description = ?, date_modification = CURRENT_TIMESTAMP WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, this.valeur);
                pstmt.setString(2, this.description);
                pstmt.setInt(3, this.id);
                pstmt.executeUpdate();
            }
        } else {
            // Insertion
            String sql = "INSERT INTO parametres_systeme (code, valeur, description) VALUES (?, ?, ?) RETURNING id, date_modification";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, this.code);
                pstmt.setString(2, this.valeur);
                pstmt.setString(3, this.description);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        this.id = rs.getInt("id");
                        this.dateModification = rs.getTimestamp("date_modification");
                    }
                }
            }
        }
    }

    // Méthodes utilitaires
    public static int getHeuresAvantVolReservation(Connection conn) throws SQLException {
        ParametreSysteme param = getByCode(conn, "HEURES_AVANT_VOL_RESERVATION");
        return param != null ? Integer.parseInt(param.getValeur()) : 48; // Valeur par défaut: 48 heures
    }

    public static int getHeuresAvantVolAnnulation(Connection conn) throws SQLException {
        ParametreSysteme param = getByCode(conn, "HEURES_AVANT_VOL_ANNULATION");
        return param != null ? Integer.parseInt(param.getValeur()) : 24; // Valeur par défaut: 24 heures
    }
} 