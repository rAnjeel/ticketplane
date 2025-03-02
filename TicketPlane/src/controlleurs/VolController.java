package controlleurs;

import mg.itu.prom16.Annotations.*;
import mg.itu.prom16.ModelView;
import models.Vol;
import models.VilleDesservie;
import config.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.List;

@Controller
public class VolController {
    
    @Url("/vol/insertForm")
    public ModelView showInsertForm() {
        ModelView mv = new ModelView("/admin/insertVol.jsp");
        try (Connection conn = DatabaseConnection.getConnection()) {
            List<VilleDesservie> villes = VilleDesservie.getAll(conn);
            mv.addObject("villes", villes);
        } catch (SQLException e) {
            e.printStackTrace();
            mv.addObject("error", "Erreur lors de la récupération des villes");
        }
        return mv;
    }
    
    @Url("/vol/insertVol")
    @Post
    @FormPath("/vol/insertForm")
    public ModelView insertVol(@RequestObject(value = "vol") Vol vol, @RequestObject(value = "ville_depart") VilleDesservie villeDepart, @RequestObject(value = "ville_arrivee") VilleDesservie villeArrivee) {
        System.out.println(vol.getDateDepart());
        System.out.println(vol.getDateArrivee());
        System.out.println(villeDepart.getIdVille());
        System.out.println(villeArrivee.getIdVille());
        ModelView mv = new ModelView();
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Récupérer les villes complètes à partir des IDs
             villeDepart = VilleDesservie.read(conn, villeDepart.getIdVille());
             villeArrivee = VilleDesservie.read(conn, villeArrivee.getIdVille());
            System.out.println(villeDepart.getNom());
            System.out.println(villeArrivee.getNom());
            
            // Mettre à jour les objets complets
            vol.setVilleDepart(villeDepart);
            vol.setVilleArrivee(villeArrivee);
            
            // Sauvegarder le vol
            vol.create(conn);
            
            mv.setUrl("/admin/dashboard");
            mv.addObject("message", "Vol ajouté avec succès!");
        } catch (SQLException e) {
            e.printStackTrace();
            mv.setUrl("/vol/insertForm");
            mv.addObject("error", "Erreur lors de l'insertion du vol: " + e.getMessage());
            mv.addObject("formData", vol);
        }
        return mv;
    }

    @Url("/vol/delete")
    public ModelView deleteVol(@Param(name = "id") int idVol) {
        ModelView mv = new ModelView();
        try (Connection conn = DatabaseConnection.getConnection()) {
            Vol vol = Vol.read(conn, idVol);
            if (vol != null) {
                vol.delete(conn);
                mv.addObject("message", "Vol supprimé avec succès!");
            } else {
                mv.addObject("error", "Vol non trouvé");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mv.addObject("error", "Erreur lors de la suppression du vol");
        }
        mv.setUrl("/vol/list");
        return mv;
    }

    @Url("/vol/list")
    public ModelView listVolsFrontOffice() {
        ModelView mv = new ModelView("/vols.jsp");
        try (Connection conn = DatabaseConnection.getConnection()) {
            List<Vol> vols = Vol.getAll(conn);
            List<VilleDesservie> villes = VilleDesservie.getAll(conn);
            mv.addObject("vols", vols);
            mv.addObject("villes", villes);
        } catch (SQLException e) {
            e.printStackTrace();
            mv.addObject("error", "Erreur lors de la récupération des vols");
        }
        return mv;
    }

    @Url("/vol/search")
    public ModelView searchVols(
            @Param(name = "villeDepart") String villeDepartStr,
            @Param(name = "villeArrivee") String villeArriveeStr,
            @Param(name = "dateDepart") String dateDepart,
            @Param(name = "dateArrivee") String dateArrivee) {
        ModelView mv = new ModelView("/vols.jsp");
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Conversion des String en Integer
            Integer villeDepart = null;
            Integer villeArrivee = null;
            
            try {
                if (villeDepartStr != null && !villeDepartStr.isEmpty()) {
                    villeDepart = Integer.parseInt(villeDepartStr);
                }
            } catch (NumberFormatException e) {
                // Ignorer l'erreur de parsing
            }
            
            try {
                if (villeArriveeStr != null && !villeArriveeStr.isEmpty()) {
                    villeArrivee = Integer.parseInt(villeArriveeStr);
                }
            } catch (NumberFormatException e) {
                // Ignorer l'erreur de parsing
            }
            
            List<Vol> vols = Vol.search(conn, villeDepart, villeArrivee, dateDepart, dateArrivee);
            List<VilleDesservie> villes = VilleDesservie.getAll(conn);
            mv.addObject("vols", vols);
            mv.addObject("villes", villes);
            
            // Conserver les paramètres de recherche pour le formulaire
            mv.addObject("searchVilleDepart", villeDepart);
            mv.addObject("searchVilleArrivee", villeArrivee);
            mv.addObject("searchDateDepart", dateDepart);
            mv.addObject("searchDateArrivee", dateArrivee);
        } catch (SQLException e) {
            e.printStackTrace();
            mv.addObject("error", "Erreur lors de la recherche des vols");
        }
        return mv;
    }
}
