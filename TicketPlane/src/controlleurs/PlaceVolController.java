package controlleurs;

import mg.itu.prom16.Annotations.*;
import mg.itu.prom16.ModelView;
import models.PlaceVol;
import models.TypeSiege;
import models.Vol;
import config.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Date;
import java.util.List;

@Controller
public class PlaceVolController {

    // --- Affichage du formulaire d'insertion ---
    @Url("/placevol/insertForm")
    public ModelView showInsertForm() {
        ModelView mv = new ModelView("/admin/insertPlaceVol.jsp");
        try (Connection conn = DatabaseConnection.getConnection()) {
            List<TypeSiege> types = TypeSiege.getAll(conn);
            List<Vol> vols = Vol.getAll(conn);
            mv.addObject("typesSiege", types);
            mv.addObject("vols", vols);
        } catch (SQLException e) {
            e.printStackTrace();
            mv.addObject("error", "Impossible de charger les données: " + e.getMessage());
        }
        return mv;
    }

    // --- Insertion d'un PlaceVol ---
    @Url("/placevol/insert")
    @Post
    @FormPath("/placevol/insertForm")
    public ModelView insertPlaceVol(
            @Param(name="placevol.type_siege") int idType,
            @Param(name="placevol.id_vol") int idVol,
            @Param(name="placevol.date_fin") String dateFin,
            @Param(name="placevol.prix") double prix,
            @Param(name="placevol.nombre") int nombre
    ) {
        ModelView mv = new ModelView();
        PlaceVol placeVol = new PlaceVol();

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Récupérer les entités associées
                TypeSiege typeSiege = TypeSiege.getElementById(conn, idType);
                Vol vol = Vol.getById(conn, idVol);

                if (typeSiege == null) {
                    throw new SQLException("Le type de siège sélectionné n'existe pas");
                }
                if (vol == null) {
                    throw new SQLException("Le vol sélectionné n'existe pas");
                }

                // Mapper les données dans l’objet PlaceVol
                placeVol.setTypeSiege(typeSiege);
                placeVol.setVol(vol);
                placeVol.setDateFin(Date.valueOf(dateFin));
                placeVol.setPrix(prix);
                placeVol.setNombre(nombre);

                // Sauvegarde en DB
                placeVol.create(conn);

                conn.commit();
                mv.setUrl("/placevol/insertForm");
                mv.addObject("message", "PlaceVol ajouté avec succès!");
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
            mv.setUrl("/placevol/insertForm");
            mv.addObject("error", "Erreur lors de l'insertion: " + e.getMessage());
            mv.addObject("formData", placeVol);
        }
        return mv;
    }
}
