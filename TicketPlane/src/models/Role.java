package models;

public class Role {
    private int idRole;
    private String nom;

    public Role(int idRole, String nom) {
        this.idRole = idRole;
        this.nom = nom;
    }

    // Getters et Setters
    public int getIdRole() {
        return idRole;
    }

    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
} 