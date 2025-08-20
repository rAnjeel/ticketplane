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

    public void payer(Connection conn) throws SQLException {
        System.out.println("=== DÉBUT MÉTHODE PAYER ===");

        if (this.vol == null || this.typeSiege == null) {
            throw new SQLException("Réservation invalide : vol ou type de siège manquant");
        }
        System.out.println("ID Réservation: " + this.idReservation);
        System.out.println("ID Vol: " + this.vol.getIdVol());
        System.out.println("ID Type Siège: " + this.typeSiege.getIdType());
        System.out.println("Date Réservation (String): " + this.dateReservation);

        // Convertir la date de réservation (String) -> java.sql.Date (AAAA-MM-JJ)
        Date dateResSql = toSqlDateFromStringTimestamp(this.dateReservation);
        if (dateResSql == null) {
            throw new SQLException("Format de date_reservation invalide: " + this.dateReservation);
        }
        System.out.println("Date Réservation (SQL Date utilisée): " + dateResSql);

        Double prixTrouve = null;

        // 1) Chercher un prix dans PlaceVol pour ce vol & type siège, valide à la date de réservation
        //    Règle: on prend un enregistrement dont date_fin >= date_réservation (valide jusqu’à date_fin)
        String sqlCheck = """
            SELECT prix
            FROM placevol
            WHERE id_vol = ?
            AND id_type_siege = ?
            AND date_fin >= ?
            ORDER BY date_fin ASC
            LIMIT 1
        """;
        System.out.println("\n--- RECHERCHE DU PRIX ---");
        System.out.println("SQL: " + sqlCheck);
        System.out.println("Paramètres: id_vol=" + this.vol.getIdVol()
                + ", id_type_siege=" + this.typeSiege.getIdType()
                + ", date_fin>=" + dateResSql);

        try (PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck)) {
            pstmtCheck.setInt(1, this.vol.getIdVol());
            pstmtCheck.setInt(2, this.typeSiege.getIdType());
            pstmtCheck.setDate(3, dateResSql);

            try (ResultSet rs = pstmtCheck.executeQuery()) {
                if (rs.next()) {
                    prixTrouve = rs.getDouble("prix");
                    System.out.println("✅ Prix trouvé: " + prixTrouve);
                } else {
                    System.out.println("❌ Aucun prix trouvé (on mettra seulement le statut=5)");
                }
            }
        }

        // 2) Mise à jour : toujours statut=5 ; si prix trouvé, on met aussi prix_total
        final String sqlUpdate = (prixTrouve != null)
                ? "UPDATE Reservation SET id_statut = ?, prix_total = ? WHERE id_reservation = ?"
                : "UPDATE Reservation SET id_statut = ? WHERE id_reservation = ?";

        System.out.println("\n--- MISE À JOUR RÉSERVATION ---");
        System.out.println("SQL: " + sqlUpdate);

        try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {
            pstmt.setInt(1, 5); // 5 = payé (selon ton référentiel)

            if (prixTrouve != null) {
                pstmt.setDouble(2, prixTrouve);
                pstmt.setInt(3, this.idReservation);
                System.out.println("Paramètres: id_statut=5, prix_total=" + prixTrouve + ", id_reservation=" + this.idReservation);
            } else {
                pstmt.setInt(2, this.idReservation);
                System.out.println("Paramètres: id_statut=5, id_reservation=" + this.idReservation);
            }

            int rows = pstmt.executeUpdate();
            System.out.println("✅ Lignes mises à jour: " + rows);
        }

        System.out.println("=== FIN MÉTHODE PAYER ===\n");
    }

    /**
     * Convertit une chaîne Timestamp (ex: "2025-08-27 12:00:00.0") en java.sql.Date (AAAA-MM-JJ).
     * Retourne null si la chaîne est vide/incorrecte.
     */
    private static java.sql.Date toSqlDateFromStringTimestamp(String dateTime) {
        if (dateTime == null || dateTime.isEmpty()) return null;
        try {
            String justDate = dateTime.contains(" ")
                    ? dateTime.substring(0, dateTime.indexOf(' '))
                    : dateTime;
            return java.sql.Date.valueOf(justDate);
        } catch (IllegalArgumentException e) {
            System.err.println("[toSqlDateFromStringTimestamp] Format invalide: " + dateTime);
            return null;
        }
    }
    
} 