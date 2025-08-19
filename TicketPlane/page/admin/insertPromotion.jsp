<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page import="models.Promotion" %>

<!DOCTYPE html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insertion Promotion</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/gh/rAnjeel/ticketplane@main/assets/css/style.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
</head>
<body>
<jsp:include page="../components/header.jsp" />

<div class="container mt-5">
    <h2>Ajouter une promotion</h2>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <% 
    Promotion formData = null;
    Object formDataObj = request.getAttribute("formData");
    if (formDataObj instanceof Promotion) {
        formData = (Promotion) formDataObj;
    }
    %>

    <form action="${pageContext.request.contextPath}/promotion/insert" method="post" class="mt-4">
        <div class="row mb-3">
            <div class="col-md-6">
                <label for="typeSiege" class="form-label">Type de siège</label>
                <select name="promotion.type_siege" class="form-select">
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

        <div class="row mb-3">
            <div class="col-md-6">
                <label for="dateDebut" class="form-label">Date début</label>
                <input type="date" id="dateDebut" name="promotion.date_debut" 
                       class="form-control" 
                       value="<%= formData != null ? formData.getDateDebut() : "" %>" required>
            </div>
            <div class="col-md-6">
                <label for="dateFin" class="form-label">Date fin</label>
                <input type="date" id="dateFin" name="promotion.date_fin" 
                       class="form-control" 
                       value="<%= formData != null ? formData.getDateFin() : "" %>" required>
            </div>
        </div>

        <div class="row mb-3">
            <div class="col-md-6">
                <label for="pourcentage" class="form-label">Pourcentage (%)</label>
                <input type="number" id="pourcentage" name="promotion.pourcentage" step="0.01" 
                       class="form-control"
                       value="<%= formData != null ? formData.getPourcentage() : "" %>" required>
            </div>
            <div class="col-md-6">
                <label for="nombre" class="form-label">Nombre de sièges</label>
                <input type="number" id="nombre" name="promotion.nombre" 
                       class="form-control"
                       value="<%= formData != null ? formData.getNombre() : "" %>" required>
            </div>
        </div>

        <button type="submit" class="btn btn-primary">Ajouter</button>
        <a href="${pageContext.request.contextPath}/promotion/list" class="btn btn-secondary">Retour</a>
    </form>
</div>

</body>
</html>
