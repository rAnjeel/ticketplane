package controlleurs;

import mg.itu.prom16.Annotations.*;
import mg.itu.prom16.ModelView;
import mg.itu.prom16.MySession;
import models.*;
import config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Controller
public class ParametreController {
    
    @Url("/admin/parametres")
    public ModelView listeParametres(MySession session) {
        ModelView mv = new ModelView("/admin/parametres.jsp");
        
        // Vérifier si l'utilisateur est admin
        Utilisateur user = (Utilisateur) session.get("user");
        if (user == null || !user.isAdmin()) {
            mv.setUrl("/auth/login.jsp");
            mv.addObject("error", "Accès refusé. Vous devez être administrateur pour accéder à cette page.");
            return mv;
        }
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            List<ParametreSysteme> parametres = ParametreSysteme.getAll(conn);
            mv.addObject("parametres", parametres);
        } catch (SQLException e) {
            e.printStackTrace();
            mv.addObject("error", "Erreur lors du chargement des paramètres: " + e.getMessage());
        }
        
        return mv;
    }
    
    @Url("/admin/parametres/save")
    @Post
    public ModelView saveParametre(@Param(name = "id") int id,
                                  @Param(name = "valeur") String valeur,
                                  @Param(name = "description") String description,
                                  MySession session) {
        ModelView mv = new ModelView();
        
        // Vérifier si l'utilisateur est admin
        Utilisateur user = (Utilisateur) session.get("user");
        if (user == null || !user.isAdmin()) {
            mv.setUrl("/auth/login.jsp");
            mv.addObject("error", "Accès refusé. Vous devez être administrateur pour modifier les paramètres.");
            return mv;
        }
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            ParametreSysteme parametre = null;
            
            // Récupérer le paramètre existant
            String sql = "SELECT * FROM parametres_systeme WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        parametre = new ParametreSysteme(
                            rs.getInt("id"),
                            rs.getString("code"),
                            rs.getString("valeur"),
                            rs.getString("description"),
                            rs.getTimestamp("date_modification")
                        );
                    }
                }
            }
            
            if (parametre == null) {
                throw new SQLException("Paramètre non trouvé");
            }
            
            // Mettre à jour les valeurs
            parametre.setValeur(valeur);
            parametre.setDescription(description);
            parametre.save(conn);
            
            mv.setUrl("/admin/parametres");
            mv.addObject("message", "Paramètre mis à jour avec succès");
        } catch (SQLException e) {
            e.printStackTrace();
            mv.setUrl("/admin/parametres");
            mv.addObject("error", "Erreur lors de la mise à jour du paramètre: " + e.getMessage());
        }
        
        return mv;
    }
} 