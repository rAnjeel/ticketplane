package models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import mg.itu.prom16.Annotations.RequestField;
import mg.itu.prom16.Annotations.FormField;
import mg.itu.prom16.Annotations.Required;

public class Promotion {
    @RequestField("id")
    private int id;

    @RequestField("typeSiege")
    @FormField(name = "type_siege")
    @Required
    private TypeSiege typeSiege;

    @RequestField("date_debut")
    @FormField(name = "date_debut")
    @Required
    private Date dateDebut;

    @RequestField("date_fin")
    @FormField(name = "date_fin")
    @Required
    private Date dateFin;

    @RequestField("pourcentage")
    @FormField(name = "pourcentage")
    @Required
    private double pourcentage;

    @RequestField("nombre")
    @FormField(name = "nombre")
    @Required
    private int nombre;

    // --- Constructeurs ---
    public Promotion() {}

    public Promotion(int id, TypeSiege typeSiege, Date dateDebut, Date dateFin, double pourcentage, int nombre) {
        this.id = id;
        this.typeSiege = typeSiege;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.pourcentage = pourcentage;
        this.nombre = nombre;
    }

    // --- Getters & Setters ---
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public TypeSiege getTypeSiege() {
        return typeSiege;
    }
    public void setTypeSiege(TypeSiege typeSiege) {
        this.typeSiege = typeSiege;
    }

    public Date getDateDebut() {
        return dateDebut;
    }
    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }
    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public double getPourcentage() {
        return pourcentage;
    }
    public void setPourcentage(double pourcentage) {
        this.pourcentage = pourcentage;
    }

    public int getNombre() {
        return nombre;
    }
    public void setNombre(int nombre) {
        this.nombre = nombre;
    }

    // --- CRUD Methods ---
    public void create(Connection conn) throws SQLException {
        String sql = "INSERT INTO promotions (id_type_siege, date_debut, date_fin, pourcentage, nombre) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, typeSiege.getIdType());
            pstmt.setDate(2, dateDebut);
            pstmt.setDate(3, dateFin);
            pstmt.setDouble(4, pourcentage);
            pstmt.setInt(5, nombre);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                this.id = rs.getInt(1);
            }
        }
    }

    public static Promotion read(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM promotions WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Promotion(
                    rs.getInt("id"),
                    TypeSiege.getElementById(conn, rs.getInt("id_type_siege")),
                    rs.getDate("date_debut"),
                    rs.getDate("date_fin"),
                    rs.getDouble("pourcentage"),
                    rs.getInt("nombre")
                );
            }
        }
        return null;
    }

    public void update(Connection conn) throws SQLException {
        String sql = "UPDATE promotions SET id_type_siege = ?, date_debut = ?, date_fin = ?, " +
                     "pourcentage = ?, nombre = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, typeSiege.getIdType());
            pstmt.setDate(2, dateDebut);
            pstmt.setDate(3, dateFin);
            pstmt.setDouble(4, pourcentage);
            pstmt.setInt(5, nombre);
            pstmt.setInt(6, id);
            pstmt.executeUpdate();
        }
    }

    public void delete(Connection conn) throws SQLException {
        String sql = "DELETE FROM promotions WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public static List<Promotion> getAll(Connection conn) throws SQLException {
        List<Promotion> promotions = new ArrayList<>();
        String sql = "SELECT * FROM promotions ORDER BY date_debut DESC";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Promotion promo = new Promotion(
                    rs.getInt("id"),
                    TypeSiege.getElementById(conn, rs.getInt("id_type_siege")),
                    rs.getDate("date_debut"),
                    rs.getDate("date_fin"),
                    rs.getDouble("pourcentage"),
                    rs.getInt("nombre")
                );
                promotions.add(promo);
            }
        }
        return promotions;
    }

    // --- Recherche simple (facultatif comme Vol.search) ---
    public static List<Promotion> search(Connection conn, Integer typeSiegeId, Date dateDebut, Date dateFin) throws SQLException {
        List<Promotion> promotions = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM promotions WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (typeSiegeId != null) {
            sql.append(" AND id_type_siege = ?");
            params.add(typeSiegeId);
        }
        if (dateDebut != null) {
            sql.append(" AND date_debut >= ?");
            params.add(dateDebut);
        }
        if (dateFin != null) {
            sql.append(" AND date_fin <= ?");
            params.add(dateFin);
        }

        sql.append(" ORDER BY date_debut ASC");

        try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Promotion promo = new Promotion(
                    rs.getInt("id"),
                    TypeSiege.getElementById(conn, rs.getInt("id_type_siege")),
                    rs.getDate("date_debut"),
                    rs.getDate("date_fin"),
                    rs.getDouble("pourcentage"),
                    rs.getInt("nombre")
                );
                promotions.add(promo);
            }
        }
        return promotions;
    }
}
