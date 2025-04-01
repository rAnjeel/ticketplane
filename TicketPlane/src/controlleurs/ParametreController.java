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

    @Post
    @Url("/api/parametres/update")
    @Restapi
    public ModelView updateParametreApi(@Param(name = "id") int id,
                                      @Param(name = "valeur") String valeur,
                                      @Param(name = "description") String description) {
        ModelView modelView = new ModelView();
        
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
                modelView.addObject("status", "error");
                modelView.addObject("message", "Paramètre non trouvé");
                return modelView;
            }
            
            // Mettre à jour les valeurs
            parametre.setValeur(valeur);
            if (description != null) {
                parametre.setDescription(description);
            }
            parametre.save(conn);
            
            modelView.addObject("status", "success");
            modelView.addObject("message", "Paramètre mis à jour avec succès");
            modelView.addObject("parametre", parametre);
        } catch (SQLException e) {
            e.printStackTrace();
            modelView.addObject("status", "error");
            modelView.addObject("message", "Erreur lors de la mise à jour: " + e.getMessage());
        }
        
        return modelView;
    }

    @Post
    @Url("/api/parametres/bulkupdate")
    @Restapi
    public ModelView updateMultipleParametresApi(@Param(name = "ageEnfant") String ageEnfant,
                                               @Param(name = "nbSiegesPromo") String nbSiegesPromo,
                                               @Param(name = "tauxReductionEnfant") String tauxReductionEnfant,
                                               @Param(name = "tauxReductionPromo") String tauxReductionPromo) {
        ModelView modelView = new ModelView();
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                // Mettre à jour l'âge maximum enfant
                if (ageEnfant != null) {
                    updateParametreValeur(conn, "AGE_MAX_ENFANT", ageEnfant);
                }
                
                // Mettre à jour le nombre de sièges en promotion
                if (nbSiegesPromo != null) {
                    updateParametreValeur(conn, "NB_SIEGES_PROMO", nbSiegesPromo);
                }
                
                // Mettre à jour le taux de réduction enfant
                if (tauxReductionEnfant != null) {
                    updateParametreValeur(conn, "TAUX_REDUCTION_ENFANT", tauxReductionEnfant);
                }
                
                // Mettre à jour le taux de réduction promotion
                if (tauxReductionPromo != null) {
                    updateParametreValeur(conn, "TAUX_REDUCTION_PROMO", tauxReductionPromo);
                }
                
                conn.commit();
                
                List<ParametreSysteme> parametres = ParametreSysteme.getAll(conn);
                modelView.addObject("status", "success");
                modelView.addObject("message", "Paramètres mis à jour avec succès");
                modelView.addObject("parametres", parametres);
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            modelView.addObject("status", "error");
            modelView.addObject("message", "Erreur lors de la mise à jour: " + e.getMessage());
        }
        
        return modelView;
    }

    private void updateParametreValeur(Connection conn, String code, String valeur) throws SQLException {
        String sql = "UPDATE parametres_systeme SET valeur = ?, date_modification = CURRENT_TIMESTAMP WHERE code = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, valeur);
            pstmt.setString(2, code);
            pstmt.executeUpdate();
        }
    }

    @Post
    @Url("/api/parametres/age-enfant")
    @Restapi
    public ModelView updateAgeEnfant(@Param(name = "valeur") String valeur) {
        ModelView modelView = new ModelView();
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            updateParametreValeur(conn, "AGE_MAX_ENFANT", valeur);
            
            ParametreSysteme parametre = ParametreSysteme.getByCode(conn, "AGE_MAX_ENFANT");
            modelView.addObject("status", "success");
            modelView.addObject("message", "Âge maximum enfant mis à jour avec succès");
            modelView.addObject("parametre", parametre);
        } catch (SQLException e) {
            e.printStackTrace();
            modelView.addObject("status", "error");
            modelView.addObject("message", "Erreur lors de la mise à jour: " + e.getMessage());
        }
        
        return modelView;
    }

    @Post
    @Url("/api/parametres/sieges-promo")
    @Restapi
    public ModelView updateSiegesPromo(@Param(name = "valeur") String valeur) {
        ModelView modelView = new ModelView();
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            updateParametreValeur(conn, "NB_SIEGES_PROMO", valeur);
            
            ParametreSysteme parametre = ParametreSysteme.getByCode(conn, "NB_SIEGES_PROMO");
            modelView.addObject("status", "success");
            modelView.addObject("message", "Nombre de sièges en promotion mis à jour avec succès");
            modelView.addObject("parametre", parametre);
        } catch (SQLException e) {
            e.printStackTrace();
            modelView.addObject("status", "error");
            modelView.addObject("message", "Erreur lors de la mise à jour: " + e.getMessage());
        }
        
        return modelView;
    }

    @Post
    @Url("/api/parametres/reduction-enfant")
    @Restapi
    public ModelView updateReductionEnfant(@Param(name = "valeur") String valeur) {
        ModelView modelView = new ModelView();
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            updateParametreValeur(conn, "TAUX_REDUCTION_ENFANT", valeur);
            
            ParametreSysteme parametre = ParametreSysteme.getByCode(conn, "TAUX_REDUCTION_ENFANT");
            modelView.addObject("status", "success");
            modelView.addObject("message", "Taux de réduction enfant mis à jour avec succès");
            modelView.addObject("parametre", parametre);
        } catch (SQLException e) {
            e.printStackTrace();
            modelView.addObject("status", "error");
            modelView.addObject("message", "Erreur lors de la mise à jour: " + e.getMessage());
        }
        
        return modelView;
    }

    @Post
    @Url("/api/parametres/reduction-promo")
    @Restapi
    public ModelView updateReductionPromo(@Param(name = "valeur") String valeur) {
        ModelView modelView = new ModelView();
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            updateParametreValeur(conn, "TAUX_REDUCTION_PROMO", valeur);
            
            ParametreSysteme parametre = ParametreSysteme.getByCode(conn, "TAUX_REDUCTION_PROMO");
            modelView.addObject("status", "success");
            modelView.addObject("message", "Taux de réduction promotion mis à jour avec succès");
            modelView.addObject("parametre", parametre);
        } catch (SQLException e) {
            e.printStackTrace();
            modelView.addObject("status", "error");
            modelView.addObject("message", "Erreur lors de la mise à jour: " + e.getMessage());
        }
        
        return modelView;
    }

    @Post
    @Url("/api/parametres/heures-reservation")
    @Restapi
    public ModelView updateHeuresReservation(@Param(name = "valeur") String valeur) {
        ModelView modelView = new ModelView();
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            updateParametreValeur(conn, "HEURES_AVANT_VOL_RESERVATION", valeur);
            
            ParametreSysteme parametre = ParametreSysteme.getByCode(conn, "HEURES_AVANT_VOL_RESERVATION");
            modelView.addObject("status", "success");
            modelView.addObject("message", "Délai de réservation mis à jour avec succès");
            modelView.addObject("parametre", parametre);
        } catch (SQLException e) {
            e.printStackTrace();
            modelView.addObject("status", "error");
            modelView.addObject("message", "Erreur lors de la mise à jour: " + e.getMessage());
        }
        
        return modelView;
    }

    @Post
    @Url("/api/parametres/heures-annulation")
    @Restapi
    public ModelView updateHeuresAnnulation(@Param(name = "valeur") String valeur) {
        ModelView modelView = new ModelView();
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            updateParametreValeur(conn, "HEURES_AVANT_VOL_ANNULATION", valeur);
            
            ParametreSysteme parametre = ParametreSysteme.getByCode(conn, "HEURES_AVANT_VOL_ANNULATION");
            modelView.addObject("status", "success");
            modelView.addObject("message", "Délai d'annulation mis à jour avec succès");
            modelView.addObject("parametre", parametre);
        } catch (SQLException e) {
            e.printStackTrace();
            modelView.addObject("status", "error");
            modelView.addObject("message", "Erreur lors de la mise à jour: " + e.getMessage());
        }
        
        return modelView;
    }
} 