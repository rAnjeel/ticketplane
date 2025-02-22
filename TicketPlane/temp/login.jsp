<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
<%@ page import="models.Utilisateur" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AirBooking - Connexion</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@6.4.2/css/all.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/gh/rAnjeel/ticketplane-assets@main/assets/css/style.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/gh/rAnjeel/ticketplane-assets@main/assets/css/login.css" rel="stylesheet">
</head>
<body>
    <div class="login-container">
        <div class="container">
            <div class="login-card">
                <a href="${pageContext.request.contextPath}/" class="btn btn-outline-dark mb-4">
                    <i class="fas fa-home"></i> Retour à l'accueil
                </a>
                
                <h1 class="login-title">Connexion</h1>
                
                <% 
                String messageCredentials = (String) request.getAttribute("messageCredentials");
                System.out.println(request.getAttribute("messageCredentials"));
                if (messageCredentials != null && !messageCredentials.isEmpty()) {
                    String messageClass = messageCredentials.contains("incorrect") || messageCredentials.contains("Erreur") 
                        ? "error-message" : "success-message";
                %>
                    <div class="message <%= messageClass %>">
                        <%= messageCredentials %>
                    </div>
                <% } %>

                <%
                Object validationErrorsObj = request.getAttribute("validationErrors");
                if (validationErrorsObj != null && validationErrorsObj instanceof HashMap) {
                    HashMap<String, ArrayList<String>> validationErrors = (HashMap<String, ArrayList<String>>) validationErrorsObj;
                    if (!validationErrors.isEmpty()) {
                %>
                    <div class="message error-message">
                        <%
                        for (Map.Entry<String, ArrayList<String>> error : validationErrors.entrySet()) {
                        %>
                            <div class="field-error">
                                <strong><%= error.getKey() %></strong>
                                <ul>
                                    <%
                                    for (String message : error.getValue()) {
                                    %>
                                        <li><%= message %></li>
                                    <%
                                    }
                                    %>
                                </ul>
                            </div>
                        <%
                        }
                        %>
                    </div>
                <%
                    }
                }
                %>

                <form action="${pageContext.request.contextPath}/login" method="post">
                    <% 
                    Utilisateur formData=null; 
                    Object formDataObj=request.getAttribute("formData"); 
                    if (formDataObj !=null && formDataObj instanceof Utilisateur) {
                         formData=(Utilisateur) formDataObj; 
                        } 
                    %>
                    <div class="form-group">
                        <label for="email">
                            <i class="fas fa-envelope"></i> Email
                        </label>
                        <input type="text" class="form-control" id="email" name="utilisateur.email"  
                            value="<%= formData != null ? formData.getEmail() : "" %>">
                    </div>

                    <div class="form-group">
                        <label for="mdp">
                            <i class="fas fa-lock"></i> Mot de passe
                        </label>
                        <input type="password" class="form-control" id="mdp" name="utilisateur.mdp"
                            value="<%= formData != null ? formData.getMdp() : "" %>">
                    </div>

                    <button type="submit" class="btn btn-login">
                        <i class="fas fa-sign-in-alt"></i> Se connecter
                    </button>
                </form>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 