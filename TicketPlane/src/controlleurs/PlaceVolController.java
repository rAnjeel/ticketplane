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

    @Url("/placevol/reallocate")
    public ModelView reallocateReservations(@Param(name="date") String dateStr) {
        ModelView mv = new ModelView();
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                Date date = Date.valueOf(dateStr);

                // 1. Récupérer le nombre de réservations groupées par vol
                String sqlReservations = """
                    SELECT r.id_vol, COUNT(*) as total
                    FROM reservations r
                    JOIN vols v ON r.id_vol = v.id
                    WHERE r.status = 1 AND v.date_depart < ?
                    GROUP BY r.id_vol
                """;

                try (var pstmt = conn.prepareStatement(sqlReservations)) {
                    pstmt.setDate(1, date);
                    try (var rs = pstmt.executeQuery()) {
                        while (rs.next()) {
                            int idVol = rs.getInt("id_vol");
                            int total = rs.getInt("total");

                            // 2. Trouver le prochain PlaceVol pour ce vol après la date donnée
                            String sqlNextPlaceVol = """
                                SELECT * FROM placevol
                                WHERE id_vol = ? AND date_fin > ?
                                ORDER BY date_fin ASC LIMIT 1
                            """;

                            try (var pstmt2 = conn.prepareStatement(sqlNextPlaceVol)) {
                                pstmt2.setInt(1, idVol);
                                pstmt2.setDate(2, date);
                                try (var rs2 = pstmt2.executeQuery()) {
                                    if (rs2.next()) {
                                        int idPlaceVol = rs2.getInt("id");
                                        int nombreActuel = rs2.getInt("nombre");

                                        // 3. Mise à jour du nombre
                                        String sqlUpdate = "UPDATE placevol SET nombre = ? WHERE id = ?";
                                        try (var pstmt3 = conn.prepareStatement(sqlUpdate)) {
                                            pstmt3.setInt(1, nombreActuel + total);
                                            pstmt3.setInt(2, idPlaceVol);
                                            pstmt3.executeUpdate();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                conn.commit();
                mv.addObject("message", "Réallocation effectuée avec succès à la date " + dateStr);
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
            mv.addObject("error", "Erreur lors de la réallocation : " + e.getMessage());
        }
        mv.setUrl("/placevol/list"); // ou une autre vue selon ton choix
        return mv;
    }

}
