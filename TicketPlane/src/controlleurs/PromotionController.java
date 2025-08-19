package controlleurs;

import mg.itu.prom16.Annotations.*;
import mg.itu.prom16.ModelView;
import models.Promotion;
import models.TypeSiege;
import config.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.sql.Date;


@Controller
public class PromotionController {

    @Url("/promotion/insertForm")
    public ModelView showInsertForm() {
        ModelView mv = new ModelView("/admin/insertPromotion.jsp");
        try (Connection conn = DatabaseConnection.getConnection()) {
            List<TypeSiege> types = TypeSiege.getAll(conn);
            mv.addObject("typesSiege", types);
        } catch (SQLException e) {
            e.printStackTrace();
            mv.addObject("error", "Impossible de charger les types de siège: " + e.getMessage());
        }
        return mv;
    }

    @Url("/promotion/insert")
    @Post
    @FormPath("/promotion/insertForm")
    public ModelView insertPromotion(
            @Param(name="promotion.type_siege") int idType,
            @Param(name="promotion.date_debut") String dateDebut,
            @Param(name="promotion.date_fin") String dateFin,
            @Param(name="promotion.pourcentage") double pourcentage,
            @Param(name="promotion.nombre") int nombre
    ) {
        ModelView mv = new ModelView();
        Promotion promo = new Promotion();

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Récupérer le TypeSiege choisi
                TypeSiege typeSiege = TypeSiege.getElementById(conn, idType);
                if (typeSiege == null) {
                    throw new SQLException("Le type de siège sélectionné n'existe pas");
                }

                // Mapper les données dans l’objet Promotion
                promo.setTypeSiege(typeSiege);
                promo.setDateDebut(Date.valueOf(dateDebut));
                promo.setDateFin(Date.valueOf(dateFin));
                promo.setPourcentage(pourcentage);
                promo.setNombre(nombre);

                // Sauvegarde en DB
                promo.create(conn);

                conn.commit();
                mv.setUrl("/promotion/list");
                mv.addObject("message", "Promotion ajoutée avec succès!");
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
            mv.setUrl("/promotion/insertForm");
            mv.addObject("error", "Erreur lors de l'insertion: " + e.getMessage());
            mv.addObject("formData", promo);
        }
        return mv;
    }
}
