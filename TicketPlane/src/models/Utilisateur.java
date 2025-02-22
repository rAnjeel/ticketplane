package models;
import mg.itu.prom16.Annotations.*;

public class Utilisateur {
    @RequestField("idUtilisateur")
    private int idUtilisateur;

    @RequestField("email")
    @FormField(name = "email")
    @Email
    private String email;
    
    @RequestField("mdp")
    @FormField(name = "mdp")
    @Required
    private String mdp;

    @RequestField("role")
    private Role role;

    public Utilisateur() {
    }

    public Utilisateur(int idUtilisateur, String email, String mdp, Role role) {
        this.idUtilisateur = idUtilisateur;
        this.email = email;
        this.mdp = mdp;
        this.role = role;
    }
    
    // Getters et Setters
    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
} 