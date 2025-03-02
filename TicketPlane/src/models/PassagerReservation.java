package models;

import java.sql.*;

public class PassagerReservation {
    private int idPassager;
    private int idReservation;
    private String photoPasseport;

    // Getters et Setters
    public int getIdPassager() {
        return idPassager;
    }

    public void setIdPassager(int idPassager) {
        this.idPassager = idPassager;
    }

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public String getPhotoPasseport() {
        return photoPasseport;
    }

    public void setPhotoPasseport(String photoPasseport) {
        this.photoPasseport = photoPasseport;
    }

    public void create(Connection conn) throws SQLException {
        String sql = "INSERT INTO PassagerReservation (id_reservation, photo_passeport) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, idReservation);
            pstmt.setString(2, photoPasseport);
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                this.idPassager = rs.getInt(1);
            }
        }
    }
} 