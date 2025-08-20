package models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import mg.itu.prom16.Annotations.RequestField;
import mg.itu.prom16.Annotations.FormField;
import mg.itu.prom16.Annotations.Required;

public class PlaceVol {
    @RequestField("id")
    private int id;

    @RequestField("typeSiege")
    @FormField(name = "id_type_siege")
    @Required
    private TypeSiege typeSiege;

    @RequestField("vol")
    @FormField(name = "id_vol")
    @Required
    private Vol vol;

    @RequestField("date_fin")
    @FormField(name = "date_fin")
    @Required
    private Date dateFin;

    @RequestField("prix")
    @FormField(name = "prix")
    @Required
    private double prix;

    @RequestField("nombre")
    @FormField(name = "nombre")
    @Required
    private int nombre;

    @RequestField("reste")
    private int reste;

    // --- Constructeurs ---
    public PlaceVol() {}

    public PlaceVol(int id, TypeSiege typeSiege, Vol vol, Date dateFin, double prix, int nombre, int reste) {
        this.id = id;
        this.typeSiege = typeSiege;
        this.vol = vol;
        this.dateFin = dateFin;
        this.prix = prix;
        this.nombre = nombre;
        this.reste = reste;
    }

    // --- Getters & Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public TypeSiege getTypeSiege() { return typeSiege; }
    public void setTypeSiege(TypeSiege typeSiege) { this.typeSiege = typeSiege; }

    public Vol getVol() { return vol; }
    public void setVol(Vol vol) { this.vol = vol; }

    public Date getDateFin() { return dateFin; }
    public void setDateFin(Date dateFin) { this.dateFin = dateFin; }

    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }

    public int getNombre() { return nombre; }
    public void setNombre(int nombre) { this.nombre = nombre; }

    public int getReste() { return reste; }
    public void setReste(int reste) { this.reste = reste; }

    // --- CRUD Methods ---
    public void create(Connection conn) throws SQLException {
        String sql = "INSERT INTO placeVol (id_vol, id_type_siege, date_fin, prix, nombre, reste) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, vol.getIdVol());
            pstmt.setInt(2, typeSiege.getIdType());
            pstmt.setDate(3, dateFin);
            pstmt.setDouble(4, prix);
            pstmt.setInt(5, nombre);
            pstmt.setInt(6, reste);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                this.id = rs.getInt(1);
            }
        }
    }

    public static PlaceVol read(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM placeVol WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new PlaceVol(
                    rs.getInt("id"),
                    TypeSiege.getElementById(conn, rs.getInt("id_type_siege")),
                    Vol.getById(conn, rs.getInt("id_vol")),
                    rs.getDate("date_fin"),
                    rs.getDouble("prix"),
                    rs.getInt("nombre"),
                    rs.getInt("reste")
                );
            }
        }
        return null;
    }

    public void update(Connection conn) throws SQLException {
        String sql = "UPDATE placeVol SET id_vol = ?, id_type_siege = ?, date_fin = ?, prix = ?, nombre = ?, reste = ? " +
                     "WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, vol.getIdVol());
            pstmt.setInt(2, typeSiege.getIdType());
            pstmt.setDate(3, dateFin);
            pstmt.setDouble(4, prix);
            pstmt.setInt(5, nombre);
            pstmt.setInt(6, reste);
            pstmt.setInt(7, id);
            pstmt.executeUpdate();
        }
    }

    public void delete(Connection conn) throws SQLException {
        String sql = "DELETE FROM placeVol WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public static List<PlaceVol> getAll(Connection conn) throws SQLException {
        List<PlaceVol> places = new ArrayList<>();
        String sql = "SELECT * FROM placeVol ORDER BY date_fin DESC";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                PlaceVol place = new PlaceVol(
                    rs.getInt("id"),
                    TypeSiege.getElementById(conn, rs.getInt("id_type_siege")),
                    Vol.getById(conn, rs.getInt("id_vol")),
                    rs.getDate("date_fin"),
                    rs.getDouble("prix"),
                    rs.getInt("nombre"),
                    rs.getInt("reste")
                );
                places.add(place);
            }
        }
        return places;
    }

    public void diminuerReste(Connection conn, int idPlace) throws SQLException {
        String sql = "UPDATE placeVol SET reste = reste - 1 WHERE id = ? AND reste > 0";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPlace);
            int updated = pstmt.executeUpdate();
            if (updated == 0) {
                throw new SQLException("Impossible de diminuer reste pour la place id=" + idPlace);
            }
        }
    }

    public boolean aEncoreReste(Connection conn, int idPlace) throws SQLException {
        String sql = "SELECT reste FROM placeVol WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPlace);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("reste") > 0;
                }
            }
        }
        return false;
    }
}
