package controlleurs;

import mg.itu.prom16.Annotations.*;
import mg.itu.prom16.ModelView;
import models.Promotion;
import models.TypeSiege;
import config.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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
    public ModelView insertPromotion(@RequestObject(value = "promotion") Promotion promo,
                                    @RequestObject(value = "type_siege") TypeSiege typeSiege) {
        ModelView mv = new ModelView();
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Charger le TypeSiege complet
                typeSiege = TypeSiege.getElementById(conn, typeSiege.getIdType());
                if (typeSiege == null) {
                    throw new SQLException("Le type de siège sélectionné n'existe pas");
                }
                promo.setTypeSiege(typeSiege);

                // Sauvegarde en DB
                promo.create(conn);

                conn.commit();
                mv.setUrl("/promotion/list");
                mv.addObject("message", "Promotion ajoutée avec succès!");
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mv.setUrl("/promotion/insertForm");
            mv.addObject("error", "Erreur lors de l'insertion: " + e.getMessage());
            mv.addObject("formData", promo);
        }
        return mv;
    }

}
