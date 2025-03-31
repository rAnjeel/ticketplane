package controlleurs;

import mg.itu.prom16.Annotations.*;
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

    @Url("/reservation/create")
    @Post
    public ModelView createReservation(@RequestObject(value = "reservation") Reservation reservation,
                                     @FileParamName(name = "photoPasseport") String photoPath,
                                     MySession session) {
        ModelView mv = new ModelView();
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Définir l'utilisateur connecté depuis la session
                Utilisateur utilisateur = (Utilisateur) session.get("user");
                reservation.setUtilisateur(utilisateur);
                
                // Récupérer le statut par défaut (En attente)
                StatutReservation statut = StatutReservation.read(conn, 1); // ID 1 = En attente
                reservation.setStatut(statut);
                
                // Définir le chemin de la photo dans l'objet reservation
                reservation.setPhotoPasseport(photoPath);
                
                // Créer la réservation
                reservation.create(conn);
                
                // Créer le passager avec la réservation associée
                PassagerReservation passager = new PassagerReservation();
                passager.setIdReservation(reservation.getIdReservation());
                // La photo est déjà stockée dans la réservation, pas besoin de la dupliquer
                passager.create(conn);
                
                conn.commit();
                mv.setUrl("/reservation/confirmation.jsp");
                mv.addObject("reservation", reservation);
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mv.setUrl("/reservation/form.jsp");
            mv.addObject("error", "Erreur lors de la création de la réservation: " + e.getMessage());
        }
        return mv;
    }
} 