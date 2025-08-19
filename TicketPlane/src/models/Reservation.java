package models;

import java.sql.*;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

import mg.itu.prom16.Annotations.RequestField;

public class Reservation {
    private int idReservation;

    private String dateReservation;

    @RequestField("vol")
    private Vol vol;

    private Utilisateur utilisateur;

    private StatutReservation statut;

    @RequestField("typeSiege")
    private TypeSiege typeSiege;

    @RequestField("prixTotal")
    private double prixTotal;

    private String codeReservation;

    @RequestField("photoPasseport")
    private String photoPasseport;

    private boolean estEnfant;

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

    public String getPhotoPasseport() {
        return photoPasseport;
    }

    public void setPhotoPasseport(String photoPasseport) {
        this.photoPasseport = photoPasseport;
    }

    public boolean isEstEnfant() {
        return estEnfant;
    }

    public void setEstEnfant(boolean estEnfant) {
        this.estEnfant = estEnfant;
    }

    public void create(Connection conn) throws SQLException {
        String sql = "INSERT INTO Reservation (date_reservation, id_vol, id_utilisateur, " +
                    "id_statut, id_type_siege, prix_total, code_reservation, photo_passeport, est_enfant) " +
                    "VALUES (CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            this.codeReservation = generateCode();
            pstmt.setInt(1, vol.getIdVol());
            pstmt.setInt(2, utilisateur.getIdUtilisateur());
            pstmt.setInt(3, statut.getIdStatut());
            pstmt.setInt(4, typeSiege.getIdType());
            pstmt.setDouble(5, prixTotal);
            pstmt.setString(6, codeReservation);
            pstmt.setString(7, photoPasseport);
            pstmt.setBoolean(8, estEnfant);
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

    public static List<Reservation> getByUtilisateur(Connection conn, int idUtilisateur, Integer idStatut) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        
        // SQL de base
        StringBuilder sql = new StringBuilder(
            "SELECT * FROM Reservation WHERE id_utilisateur = ?"
        );
        
        // Si un statut est fourni, on ajoute le filtre
        if (idStatut != null) {
            sql.append(" AND id_statut = ?");
        }
        
        sql.append(" ORDER BY date_reservation DESC");
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            pstmt.setInt(1, idUtilisateur);
            
            // Bind du statut si présent
            if (idStatut != null) {
                pstmt.setInt(2, idStatut);
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Reservation reservation = new Reservation();
                    reservation.setIdReservation(rs.getInt("id_reservation"));
                    
                    // Charger les objets associés
                    Vol vol = Vol.read(conn, rs.getInt("id_vol"));
                    TypeSiege typeSiege = TypeSiege.getElementById(conn, rs.getInt("id_type_siege"));
                    StatutReservation statut = StatutReservation.read(conn, rs.getInt("id_statut"));
                    
                    reservation.setVol(vol);
                    reservation.setTypeSiege(typeSiege);
                    reservation.setStatut(statut);
                    reservation.setPrixTotal(rs.getDouble("prix_total"));
                    reservation.setDateReservation(rs.getTimestamp("date_reservation").toString());
                    reservation.setPhotoPasseport(rs.getString("photo_passeport"));
                    
                    reservations.add(reservation);
                }
            }
        }
        
        return reservations;
    }


    public static Reservation getById(Connection conn, int idReservation) throws SQLException {
        String sql = "SELECT * FROM Reservation WHERE id_reservation = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idReservation);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Reservation reservation = new Reservation();
                    reservation.setIdReservation(rs.getInt("id_reservation"));
                    
                    // Charger les objets associés
                    Vol vol = Vol.read(conn, rs.getInt("id_vol"));
                    TypeSiege typeSiege = TypeSiege.getElementById(conn, rs.getInt("id_type_siege"));
                    StatutReservation statut = StatutReservation.read(conn, rs.getInt("id_statut"));
                    
                    reservation.setVol(vol);
                    reservation.setTypeSiege(typeSiege);
                    reservation.setStatut(statut);
                    reservation.setPrixTotal(rs.getDouble("prix_total"));
                    reservation.setDateReservation(rs.getTimestamp("date_reservation").toString());
                    reservation.setCodeReservation(rs.getString("code_reservation"));
                    reservation.setPhotoPasseport(rs.getString("photo_passeport"));
                    
                    return reservation;
                }
                return null;
            }
        }
    }

    public void update(Connection conn) throws SQLException {
        String sql = "UPDATE Reservation SET id_vol = ?, id_utilisateur = ?, " +
                "id_statut = ?, id_type_siege = ?, prix_total = ?, " +
                "photo_passeport = ? WHERE id_reservation = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, vol.getIdVol());
            pstmt.setInt(2, utilisateur.getIdUtilisateur());
            pstmt.setInt(3, statut.getIdStatut());
            pstmt.setInt(4, typeSiege.getIdType());
            pstmt.setDouble(5, prixTotal);
            pstmt.setString(6, photoPasseport);
            pstmt.setInt(7, idReservation);

            pstmt.executeUpdate();
        }
    }
    
} 