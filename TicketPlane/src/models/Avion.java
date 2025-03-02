package models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

import mg.itu.prom16.Annotations.RequestField;

public class Avion {
    @RequestField("idAvion")
    private int idAvion;

    @RequestField("immatriculation")
    private int immatriculation;

    @RequestField("date_fabrication")
    private Date dateFabrication;

    @RequestField("ville_base")
    private VilleDesservie villeBase;

    @RequestField("modele")
    private Modele modele;

    // Constructeur
    public Avion() {}
    
    public Avion(int idAvion, int immatriculation, Date dateFabrication, VilleDesservie villeBase, Modele modele) {
        this.idAvion = idAvion;
        this.immatriculation = immatriculation;
        this.dateFabrication = dateFabrication;
        this.villeBase = villeBase;
        this.modele = modele;
    }

    // Getters et Setters
    public int getIdAvion() {
        return idAvion;
    }

    public void setIdAvion(int idAvion) {
        this.idAvion = idAvion;
    }

    public int getImmatriculation() {
        return immatriculation;
    }

    public void setImmatriculation(int immatriculation) {
        this.immatriculation = immatriculation;
    }

    public Date getDateFabrication() {
        return dateFabrication;
    }

    public void setDateFabrication(Date dateFabrication) {
        this.dateFabrication = dateFabrication;
    }

    public VilleDesservie getVilleBase() {
        return villeBase;
    }

    public void setVilleBase(VilleDesservie villeBase) {
        this.villeBase = villeBase;
    }

    public Modele getModele() {
        return modele;
    }

    public void setModele(Modele modele) {
        this.modele = modele;
    }

    // Méthodes CRUD
    public void create(Connection conn) throws SQLException {
        String sql = "INSERT INTO Avion (immatriculation, date_fabrication, id_ville_base, id_modele) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, immatriculation);
            pstmt.setDate(2, dateFabrication);
            pstmt.setInt(3, villeBase.getIdVille());
            pstmt.setInt(4, modele.getIdModele());
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                this.idAvion = rs.getInt(1);
            }
        }
    }

    public static Avion read(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM Avion WHERE id_avion = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Avion avion = new Avion(
                    rs.getInt("id_avion"),
                    rs.getInt("immatriculation"),
                    rs.getDate("date_fabrication"),
                    VilleDesservie.read(conn, rs.getInt("id_ville_base")),
                    Modele.read(conn, rs.getInt("id_modele"))
                );
                avion.setIdAvion(rs.getInt("id_avion"));
                return avion;
            }
        }
        return null;
    }

    public void update(Connection conn) throws SQLException {
        String sql = "UPDATE Avion SET immatriculation = ?, date_fabrication = ?, id_ville_base = ?, id_modele = ? WHERE id_avion = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, immatriculation);
            pstmt.setDate(2, dateFabrication);
            pstmt.setInt(3, villeBase.getIdVille());
            pstmt.setInt(4, modele.getIdModele());
            pstmt.setInt(5, idAvion);
            pstmt.executeUpdate();
        }
    }

    public void delete(Connection conn) throws SQLException {
        String sql = "DELETE FROM Avion WHERE id_avion = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idAvion);
            pstmt.executeUpdate();
        }
    }

    public static List<Avion> getAll(Connection conn) throws SQLException {
        List<Avion> avions = new ArrayList<>();
        String sql = "SELECT * FROM Avion";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Avion avion = new Avion(
                    rs.getInt("id_avion"),
                    rs.getInt("immatriculation"),
                    rs.getDate("date_fabrication"),
                    VilleDesservie.read(conn, rs.getInt("id_ville_base")),
                    Modele.read(conn, rs.getInt("id_modele"))
                );
                avion.setIdAvion(rs.getInt("id_avion"));
                avions.add(avion);
            }
        }
        return avions;
    }
} 