package controlleurs;

import mg.itu.prom16.Annotations.*;
import mg.itu.prom16.ModelView;
import models.Utilisateur;
import dao.UtilisateurDAOImpl;
import java.sql.SQLException;
import mg.itu.prom16.MySession;


@Controller
public class LoginController {
    
    private UtilisateurDAOImpl utilisateurDAO = new UtilisateurDAOImpl();
    
    @Url("/loginForm")
    public ModelView showForm() {
        ModelView mv = new ModelView("login.jsp");
        return mv;
    }
    
    @Url("/login-errors")
    @Get
    public ModelView createErrorModelView(@Param(name = "errorMessage") String errorMessage) {
        ModelView mv = new ModelView();
        mv.setUrl("/login.jsp");      
        mv.addObject("messageCredentials", errorMessage);        
        return mv;
    }

    @Url("/login")
    @Post
    @FormPath("/loginForm")
    public ModelView login(@RequestObject(value = "utilisateur") Utilisateur utilisateur, MySession session) {
        System.out.println(utilisateur.getEmail());
        System.out.println(utilisateur.getMdp());
        ModelView mv = new ModelView();
        try {
            boolean isValid = utilisateurDAO.verifyCredentials(utilisateur.getEmail(), utilisateur.getMdp());
            System.out.println("isValid: " + isValid);
            if (isValid) {
                Utilisateur userDetails = utilisateurDAO.findByEmail(utilisateur.getEmail());
                // Stockage des informations de session
                session.add("user", userDetails);
                session.add("isConnected", true);
                session.add("userRole", utilisateurDAO.findByEmail(utilisateur.getEmail()).getRole().getNom());
                
                mv.addObject("utilisateur", userDetails);
                mv.addObject("message", "Connexion réussie!");
                mv.setUrl("/");
            } else {
                mv.setUrl("/login-errors?errorMessage=Email ou mot de passe incorrect");
            }
        } catch (SQLException e) {
            mv.setUrl("/login-errors?errorMessage=Erreur lors de la connexion: " + e.getMessage());
        }
        
        return mv;
    }

    @Url("/logout")
    public ModelView logout(MySession session) {
        // Suppression des attributs de session
        session.delete("user");
        session.delete("isConnected");
        session.delete("userRole");
        
        ModelView mv = new ModelView();
        mv.addObject("messageCredentials", "Deconnexion reussie!");
        mv.setUrl("/");
        return mv;
    }
} 