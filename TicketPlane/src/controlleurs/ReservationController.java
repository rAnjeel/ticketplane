package controlleurs;

import mg.itu.prom16.Annotations.*;
import mg.itu.prom16.ModelView;
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
            List<TarifVol> tarifs = TarifVol.getTarifsByVol(conn, idVol);
            List<TypeSiege> typesSiege = TypeSiege.getAll(conn);
            
            mv.addObject("vol", vol);
            mv.addObject("tarifs", tarifs);
            mv.addObject("typesSiege", typesSiege);
        } catch (SQLException e) {
            e.printStackTrace();
            mv.addObject("error", "Erreur lors du chargement des données");
        }
        return mv;
    }

    @Url("/reservation/create")
    @Post
    public ModelView createReservation(@RequestObject(value = "reservation") Reservation reservation,
                                     @FileParamName(name = "photoPasseport") String photoPath) {
        ModelView mv = new ModelView();
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Créer la réservation
                reservation.create(conn);
                
                // Créer le passager avec la photo
                PassagerReservation passager = new PassagerReservation();
                passager.setIdReservation(reservation.getIdReservation());
                passager.setPhotoPasseport(photoPath);
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
            mv.addObject("error", "Erreur lors de la création de la réservation");
        }
        return mv;
    }
} 