package models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import mg.itu.prom16.Annotations.FormField;
import mg.itu.prom16.Annotations.RequestField;
import mg.itu.prom16.Annotations.Required;

public class Vol {
    @RequestField("idVol")
    private int idVol;

    @RequestField("date_depart")
    @FormField(name = "date_depart")
    @Required
    private String dateDepart;

    @RequestField("date_arrivee")
    @FormField(name = "date_arrivee")
    @Required
    private String dateArrivee;

    @RequestField("ville_depart")
    @FormField(name = "ville_depart")
    private VilleDesservie villeDepart;

    @RequestField("ville_arrivee")
    @FormField(name = "ville_arrivee")
    private VilleDesservie villeArrivee;

    private List<TarifVol> tarifs;

    // Constructeur par défaut
    public Vol() {}

    // Constructeur complet
    public Vol(int idVol, String dateDepart, String dateArrivee, 
               VilleDesservie villeDepart, VilleDesservie villeArrivee) {
        this.idVol = idVol;
        this.dateDepart = dateDepart;
        this.dateArrivee = dateArrivee;
        this.villeDepart = villeDepart;
        this.villeArrivee = villeArrivee;
    }

    // Getters et Setters
    public int getIdVol() {
        return idVol;
    }

    public void setIdVol(int idVol) {
        this.idVol = idVol;
    }

    public String getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(String dateDepart) {
        this.dateDepart = dateDepart;
    }

    public String getDateArrivee() {
        return dateArrivee;
    }

    public void setDateArrivee(String dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    public VilleDesservie getVilleDepart() {
        return villeDepart;
    }

    public void setVilleDepart(VilleDesservie villeDepart) {
        this.villeDepart = villeDepart;
    }

    public VilleDesservie getVilleArrivee() {
        return villeArrivee;
    }

    public void setVilleArrivee(VilleDesservie villeArrivee) {
        this.villeArrivee = villeArrivee;
    }

    public List<TarifVol> getTarifs() {
        return tarifs;
    }

    public void setTarifs(List<TarifVol> tarifs) {
        this.tarifs = tarifs;
    }

    // Méthode utilitaire pour convertir String en Timestamp
    private Timestamp parseTimestamp(String dateStr) {
        try {
            // Format attendu: "2025-03-02 17:10"
            String formattedDate = dateStr + ":00";  // Ajoute les secondes
            System.out.println("Date à parser : " + formattedDate);
            return Timestamp.valueOf(formattedDate);
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur de parsing pour la date : " + dateStr);
            e.printStackTrace();
            return null;
        }
    }

    // Méthodes CRUD
    public void create(Connection conn) throws SQLException {
        String sql = "INSERT INTO Vol (date_depart, date_arrivee, id_ville_depart, id_ville_arrivee) " +
                    "VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setTimestamp(1, parseTimestamp(dateDepart));
            pstmt.setTimestamp(2, parseTimestamp(dateArrivee));
            pstmt.setInt(3, villeDepart.getIdVille());
            pstmt.setInt(4, villeArrivee.getIdVille());
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                this.idVol = rs.getInt(1);
            }
        }
    }

    public static Vol read(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM Vol WHERE id_vol = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Vol(
                    rs.getInt("id_vol"),
                    rs.getTimestamp("date_depart").toString(),
                    rs.getTimestamp("date_arrivee").toString(),
                    VilleDesservie.read(conn, rs.getInt("id_ville_depart")),
                    VilleDesservie.read(conn, rs.getInt("id_ville_arrivee"))
                );
            }
        }
        return null;
    }

    public void update(Connection conn) throws SQLException {
        String sql = "UPDATE Vol SET date_depart = ?, date_arrivee = ?, " +
                    "id_ville_depart = ?, id_ville_arrivee = ? WHERE id_vol = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, parseTimestamp(dateDepart));
            pstmt.setTimestamp(2, parseTimestamp(dateArrivee));
            pstmt.setInt(3, villeDepart.getIdVille());
            pstmt.setInt(4, villeArrivee.getIdVille());
            pstmt.setInt(5, idVol);
            pstmt.executeUpdate();
        }
    }

    public void delete(Connection conn) throws SQLException {
        String sql = "DELETE FROM Vol WHERE id_vol = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idVol);
            pstmt.executeUpdate();
        }
    }

    public static List<Vol> getAll(Connection conn) throws SQLException {
        List<Vol> vols = new ArrayList<>();
        String sql = "SELECT * FROM Vol";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Vol vol = new Vol(
                    rs.getInt("id_vol"),
                    rs.getTimestamp("date_depart").toString(),
                    rs.getTimestamp("date_arrivee").toString(),
                    VilleDesservie.read(conn, rs.getInt("id_ville_depart")),
                    VilleDesservie.read(conn, rs.getInt("id_ville_arrivee"))
                );
                vols.add(vol);
            }
        }
        return vols;
    }

    public static List<Vol> search(Connection conn, Integer villeDepart, Integer villeArrivee, 
            String dateDepart, String dateArrivee) throws SQLException {
        List<Vol> vols = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Vol WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (villeDepart != null && villeDepart > 0) {
            sql.append(" AND id_ville_depart = ?");
            params.add(villeDepart);
        }
        if (villeArrivee != null && villeArrivee > 0) {
            sql.append(" AND id_ville_arrivee = ?");
            params.add(villeArrivee);
        }
        if (dateDepart != null && !dateDepart.isEmpty()) {
            sql.append(" AND DATE(date_depart) = ?");
            params.add(Date.valueOf(dateDepart));
        }
        if (dateArrivee != null && !dateArrivee.isEmpty()) {
            sql.append(" AND DATE(date_arrivee) = ?");
            params.add(Date.valueOf(dateArrivee));
        }

        sql.append(" ORDER BY date_depart ASC");

        try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Vol vol = new Vol(
                    rs.getInt("id_vol"),
                    rs.getTimestamp("date_depart").toString(),
                    rs.getTimestamp("date_arrivee").toString(),
                    VilleDesservie.read(conn, rs.getInt("id_ville_depart")),
                    VilleDesservie.read(conn, rs.getInt("id_ville_arrivee"))
                );
                vols.add(vol);
            }
        }
        return vols;
    }
} 