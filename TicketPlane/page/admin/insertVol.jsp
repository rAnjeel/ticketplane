<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page import="models.Vol" %>

<!DOCTYPE html>
<html>
<head>
    <title>Insertion Vol</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/gh/rAnjeel/ticketplane@main/assets/css/style.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
</head>
<body>
    <jsp:include page="../components/header.jsp" />
    <div class="container mt-5">
        <h2>Ajouter un nouveau vol</h2>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger" role="alert">
                ${error}
            </div>
        </c:if>

        <%-- Messages de validation --%>
        <c:if test="${not empty validationErrors}">
            <div class="alert alert-danger error-message">
                <c:forEach items="${validationErrors}" var="error">
                    <div class="field-error">
                        <strong>${error.key}</strong>
                        <ul>
                            <c:forEach items="${error.value}" var="message">
                                <li>${message}</li>
                            </c:forEach>
                        </ul>
                    </div>
                </c:forEach>
            </div>
        </c:if>
        
        <% 
        Vol formData = null;
        Object formDataObj = request.getAttribute("formData");
        if (formDataObj != null && formDataObj instanceof Vol) {
            formData = (Vol) formDataObj;
        }
        %>
        
        <form action="${pageContext.request.contextPath}/vol/insertVol" method="post" class="mt-4">
            <div class="row mb-3">
                <div class="col-md-6">
                    <label for="villeDepart" class="form-label">Ville de départ</label>
                    <select name="ville_depart.id_ville" id="villeDepart" class="form-select" required>
                        <option value="">Sélectionnez une ville</option>
                        <c:forEach items="${villes}" var="ville">
                            <option value="${ville.idVille}">
                                ${ville.nom} (${ville.pays})
                            </option>
                        </c:forEach>
                    </select>
                </div>
                
                <div class="col-md-6">
                    <label for="villeArrivee" class="form-label">Ville d'arrivée</label>
                    <select name="ville_arrivee.id_ville" id="villeArrivee" class="form-select" required>
                        <option value="">Sélectionnez une ville</option>
                        <c:forEach items="${villes}" var="ville">
                            <option value="${ville.idVille}">
                                ${ville.nom} (${ville.pays})
                            </option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <div class="row mb-3">
                <div class="col-md-6">
                    <label for="dateDepart" class="form-label">Date et heure de départ</label>
                    <input type="text" class="form-control datetimepicker" id="dateDepart" 
                           name="vol.date_depart" value="<%= formData != null ? formData.getDateDepart() : "" %>">
                </div>
                
                <div class="col-md-6">
                    <label for="dateArrivee" class="form-label">Date et heure d'arrivée</label>
                    <input type="text" class="form-control datetimepicker" id="dateArrivee" 
                           name="vol.date_arrivee" value="<%= formData != null ? formData.getDateArrivee() : "" %>">
                </div>
            </div>

            <div class="row">
                <div class="col-12">
                    <button type="submit" class="btn btn-primary">Ajouter le vol</button>
                    <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-secondary">Retour</a>
                </div>
            </div>
        </form>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
    <script>
        // Configuration du sélecteur de date/heure
        flatpickr(".datetimepicker", {
            enableTime: true,
            dateFormat: "Y-m-d H:i",
            time_24hr: true,
            minDate: "today",
        });

        // Validation pour empêcher la sélection de la même ville
        document.getElementById('villeArrivee').addEventListener('change', function() {
            const villeDepart = document.getElementById('villeDepart').value;
            if (this.value === villeDepart) {
                alert("La ville d'arrivée doit être différente de la ville de départ");
                this.value = "";
            }
        });

        document.getElementById('villeDepart').addEventListener('change', function() {
            const villeArrivee = document.getElementById('villeArrivee').value;
            if (this.value === villeArrivee) {
                alert("La ville de départ doit être différente de la ville d'arrivée");
                this.value = "";
            }
        });
    </script>
</body>
</html> 