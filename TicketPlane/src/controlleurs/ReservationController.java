package controlleurs;

import mg.itu.prom16.Annotations.*;
import mg.itu.prom16.FileParam;
import mg.itu.prom16.ModelView;
import mg.itu.prom16.MySession;
import models.*;
import config.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Controller
public class ReservationController {    
    @Url("/reservation/vol")
    public ModelView showReservationForm(@Param(name = "id") int idVol) {
        ModelView mv = new ModelView("/reservation/form.jsp");
        try (Connection conn = DatabaseConnection.getConnection()) {
            Vol vol = Vol.read(conn, idVol);
            if (vol == null) {
                mv.addObject("error", "Vol introuvable avec l'identifiant " + idVol);
                return mv;
            }
            
            List<TarifVol> tarifs = TarifVol.getTarifsByVol(conn, idVol);
            
            // Extraire et ajouter spécifiquement les prix économique et business
            Double prixEconomique = null;
            Double prixBusiness = null;
            
            // IDs des types de sièges (1: Économique, 2: Business) - à ajuster selon votre structure de données
            final int ID_ECONOMIQUE = 1;
            final int ID_BUSINESS = 2;
            
            for (TarifVol tarif : tarifs) {
                int idType = tarif.getIdTypeSiege();
                if (idType == ID_ECONOMIQUE) {
                    prixEconomique = tarif.getPrix();
                } else if (idType == ID_BUSINESS) {
                    prixBusiness = tarif.getPrix();
                }
            }
            
            mv.addObject("vol", vol);
            mv.addObject("prixEconomique", prixEconomique);
            mv.addObject("prixBusiness", prixBusiness);
        } catch (SQLException e) {
            e.printStackTrace();
            // Message d'erreur plus détaillé
            String errorMessage = "Erreur lors du chargement des données: " + e.getMessage();
            // Ajout de détails spécifiques selon le type d'erreur
            if (e.getSQLState() != null) {
                errorMessage += " (Code SQL: " + e.getSQLState() + ")";
            }
            if (e.getCause() != null) {
                errorMessage += ". Cause: " + e.getCause().getMessage();
            }
            mv.addObject("error", errorMessage);
            mv.addObject("errorDetails", "Veuillez contacter l'administrateur si le problème persiste.");
        } catch (Exception e) {
            e.printStackTrace();
            mv.addObject("error", "Erreur inattendue: " + e.getMessage());
            mv.addObject("errorDetails", "Une erreur système s'est produite. Veuillez réessayer plus tard.");
        }
        return mv;
    }

    @Url("/reservation/mesReservations")
    public ModelView listeReservationsUtilisateur(MySession session) {
        ModelView mv = new ModelView("/reservation/liste.jsp");
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Récupérer l'utilisateur connecté
            Utilisateur utilisateur = (Utilisateur) session.get("user");
            
            if (utilisateur == null) {
                // Rediriger vers la page de connexion si l'utilisateur n'est pas connecté
                mv.setUrl("/auth/login.jsp");
                mv.addObject("error", "Veuillez vous connecter pour accéder à vos réservations");
                return mv;
            }
            
            // Récupérer les réservations de l'utilisateur
            List<Reservation> reservations = Reservation.getByUtilisateur(conn, utilisateur.getIdUtilisateur());
            
            mv.addObject("reservations", reservations);
            mv.addObject("utilisateur", utilisateur);
            
        } catch (SQLException e) {
            e.printStackTrace();
            // Message d'erreur détaillé
            String errorMessage = "Erreur lors du chargement des réservations: " + e.getMessage();
            if (e.getSQLState() != null) {
                errorMessage += " (Code SQL: " + e.getSQLState() + ")";
            }
            if (e.getCause() != null) {
                errorMessage += ". Cause: " + e.getCause().getMessage();
            }
            mv.addObject("error", errorMessage);
        }
        
        return mv;
    }

    @Url("/reservation/create")
    @Post
    public ModelView createReservation(@Param(name = "id_vol") int id_vol,
                                     @Param(name = "typeSiege_idType") int typeSiege_idType,
                                     @FileParamName(name = "photoPasseport") FileParam photoPasseport,
                                     MySession session) {
        ModelView mv = new ModelView();
        Reservation reservation = new Reservation();
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Récupérer les objets à partir des IDs
                Vol vol = Vol.read(conn, id_vol);
                TypeSiege typeSiege = TypeSiege.getElementById(conn, typeSiege_idType);
                Utilisateur utilisateur = (Utilisateur) session.get("user");
                
                // Vérifier que les objets existent
                if (vol == null || typeSiege == null || utilisateur == null) {
                    throw new SQLException("Données manquantes pour la réservation");
                }
                
                // Définir le statut par défaut (En attente - ID 1)
                StatutReservation statut = StatutReservation.read(conn, 1);
                
                // Calculer le prix total à partir du tarif correspondant
                TarifVol tarif = TarifVol.getTarifByVolAndType(conn, id_vol, typeSiege_idType);
                if (tarif == null) {
                    throw new SQLException("Tarif introuvable pour ce vol et ce type de siège");
                }
                
                // Configuration de l'objet réservation
                reservation.setVol(vol);
                reservation.setUtilisateur(utilisateur);
                reservation.setTypeSiege(typeSiege);
                reservation.setStatut(statut);
                reservation.setPrixTotal(tarif.getPrix());
                reservation.setPhotoPasseport(photoPasseport.getFileName());
                
                // Création de la réservation en base de données
                reservation.create(conn);
                                
                conn.commit();
                
                // Configuration de la réponse - Rediriger vers la liste des réservations
                mv.addObject("message", "Votre réservation a été créée avec succès!");
                mv.setUrl("/reservation/mes-reservations");

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mv.setUrl("/reservation/form.jsp");
            
            // Message d'erreur détaillé
            String errorMessage = "Erreur lors de la création de la réservation: " + e.getMessage();
            if (e.getSQLState() != null) {
                errorMessage += " (Code SQL: " + e.getSQLState() + ")";
            }
            if (e.getCause() != null) {
                errorMessage += ". Cause: " + e.getCause().getMessage();
            }
            mv.addObject("error", errorMessage);
        }
        
        return mv;
    }
} 