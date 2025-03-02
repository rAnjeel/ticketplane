package models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TypeSiege {
    private int idType;
    private String nom;

    public TypeSiege() {}

    public TypeSiege(int idType, String nom) {
        this.idType = idType;
        this.nom = nom;
    }

    // Getters et Setters
    public int getIdType() { return idType; }
    public void setIdType(int idType) { this.idType = idType; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public static List<TypeSiege> getAll(Connection conn) throws SQLException {
        List<TypeSiege> types = new ArrayList<>();
        String sql = "SELECT * FROM TypeSiege";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                types.add(new TypeSiege(
                    rs.getInt("id_type"),
                    rs.getString("nom")
                ));
            }
        }
        return types;
    }
} 