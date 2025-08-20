<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page import="models.PlaceVol" %>

<!DOCTYPE html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Insertion PlaceVol</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/gh/rAnjeel/ticketplane@main/assets/css/style.css" rel="stylesheet">
</head>
<body>
<jsp:include page="../components/header.jsp" />

<div class="container mt-5">
    <h2>Ajouter une place de vol</h2>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <% 
    PlaceVol formData = null;
    Object formDataObj = request.getAttribute("formData");
    if (formDataObj instanceof PlaceVol) {
        formData = (PlaceVol) formDataObj;
    }
    %>

    <form action="${pageContext.request.contextPath}/placevol/insert" method="post" class="mt-4">

        <!-- Sélection du vol -->
        <div class="row mb-3">
            <div class="col-md-6">
                <label for="vol" class="form-label">Vol</label>
                <select name="placevol.id_vol" class="form-select">
                    <c:forEach items="${vols}" var="vol">
                        <option value="${vol.idVol}"
                            <c:if test="${formData != null 
                                        and formData.vol != null 
                                        and formData.vol.idVol == vol.idVol}">
                                selected
                            </c:if>>
                            Vol ${vol.idVol}
                        </option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <!-- Sélection du type de siège -->
        <div class="row mb-3">
            <div class="col-md-6">
                <label for="typeSiege" class="form-label">Type de siège</label>
                <select name="placevol.type_siege" class="form-select">
                    <c:forEach items="${typesSiege}" var="type">
                        <option value="${type.idType}"
                            <c:if test="${formData != null 
                                        and formData.typeSiege != null 
                                        and formData.typeSiege.idType == type.idType}">
                                selected
                            </c:if>>
                            ${type.nom}
                        </option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <!-- Date fin de validité -->
        <div class="row mb-3">
            <div class="col-md-6">
                <label for="dateFin" class="form-label">Date fin</label>
                <input type="date" id="dateFin" name="placevol.date_fin" 
                       class="form-control" 
                       value="<%= formData != null ? formData.getDateFin() : "" %>" required>
            </div>
        </div>

        <!-- Prix -->
        <div class="row mb-3">
            <div class="col-md-6">
                <label for="prix" class="form-label">Prix (Ar)</label>
                <input type="number" id="prix" name="placevol.prix" step="0.01" 
                       class="form-control"
                       value="<%= formData != null ? formData.getPrix() : "" %>" required>
            </div>
        </div>

        <!-- Nombre de places -->
        <div class="row mb-3">
            <div class="col-md-6">
                <label for="nombre" class="form-label">Nombre de places</label>
                <input type="number" id="nombre" name="placevol.nombre" 
                       class="form-control"
                       value="<%= formData != null ? formData.getNombre() : "" %>" required>
            </div>
        </div>

        <button type="submit" class="btn btn-primary">Ajouter</button>
        <a href="${pageContext.request.contextPath}/placevol/list" class="btn btn-secondary">Retour</a>
    </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
