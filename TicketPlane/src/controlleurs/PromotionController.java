package controlleurs;

import mg.itu.prom16.Annotations.*;
import mg.itu.prom16.ModelView;
import models.Promotion;
import models.Vol;
import config.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Controller
public class PromotionController {

    // Formulaire d’ajout de promotion
    @Url("/promotion/insertForm")
    public ModelView showInsertForm() {
        ModelView mv = new ModelView("/admin/insertPromotion.jsp");
        try (Connection conn = DatabaseConnection.getConnection()) {
            List<Vol> vols = Vol.getAll(conn);
            mv.addObject("vols", vols);
        } catch (SQLException e) {
            e.printStackTrace();
            mv.addObject("error", "Erreur lors du chargement des données: " + e.getMessage());
        }
        return mv;
    }

    // Insertion d’une nouvelle promotion
    @Url("/promotion/insert")
    @Post
    @FormPath("/promotion/insertForm")
    public ModelView insertPromotion(@RequestObject(value = "promotion") Promotion promo,
                                     @RequestObject(value = "vol") Vol vol) {
        ModelView mv = new ModelView();
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Charger le vol complet à partir de l’ID
                vol = Vol.read(conn, vol.getIdVol());
                if (vol == null) {
                    throw new SQLException("Le vol sélectionné n'existe pas");
                }
                promo.setVol(vol);

                // Sauvegarder la promo
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
            mv.addObject("error", "Erreur lors de l'insertion de la promotion: " + e.getMessage());
            mv.addObject("formData", promo);
        }
        return mv;
    }

    // Suppression d’une promotion
    @Url("/promotion/delete")
    public ModelView deletePromotion(@Param(name = "id") int idPromotion) {
        ModelView mv = new ModelView();
        try (Connection conn = DatabaseConnection.getConnection()) {
            Promotion promo = Promotion.read(conn, idPromotion);
            if (promo != null) {
                promo.delete(conn);
                mv.addObject("message", "Promotion supprimée avec succès!");
            } else {
                mv.addObject("error", "Promotion non trouvée");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mv.addObject("error", "Erreur lors de la suppression de la promotion");
        }
        mv.setUrl("/promotion/list");
        return mv;
    }

    // Liste des promotions
    @Url("/promotion/list")
    public ModelView listPromotions() {
        ModelView mv = new ModelView("/promotions.jsp");
        try (Connection conn = DatabaseConnection.getConnection()) {
            List<Promotion> promotions = Promotion.getAll(conn);
            List<Vol> vols = Vol.getAll(conn);
            mv.addObject("promotions", promotions);
            mv.addObject("vols", vols);
        } catch (SQLException e) {
            e.printStackTrace();
            mv.addObject("error", "Erreur lors de la récupération des promotions: " + e.getMessage());
        }
        return mv;
    }

    // Recherche de promotions (optionnel)
    @Url("/promotion/search")
    public ModelView searchPromotions(@Param(name = "idVol") String idVolStr) {
        ModelView mv = new ModelView("/promotions.jsp");
        try (Connection conn = DatabaseConnection.getConnection()) {
            Integer idVol = null;
            try {
                if (idVolStr != null && !idVolStr.isEmpty()) {
                    idVol = Integer.parseInt(idVolStr);
                }
            } catch (NumberFormatException e) {
                // ignore
            }

            List<Promotion> promotions = Promotion.search(conn, idVol);
            List<Vol> vols = Vol.getAll(conn);

            mv.addObject("promotions", promotions);
            mv.addObject("vols", vols);
            mv.addObject("searchIdVol", idVol);
        } catch (SQLException e) {
            e.printStackTrace();
            mv.addObject("error", "Erreur lors de la recherche des promotions");
        }
        return mv;
    }
}
