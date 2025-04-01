<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Liste des Vols - AirBooking</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@6.4.2/css/all.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/gh/rAnjeel/ticketplane@main/assets/css/style.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="components/header.jsp" />
    
    <div class="container mt-5">
        <h2 class="mb-4">
            <i class="fas fa-plane"></i> Vols disponibles
        </h2>

        <div class="row mb-4">
            <div class="col-md-12">
                <div class="card">
                    <div class="card-body">
                        <form class="row g-3" action="${pageContext.request.contextPath}/vol/search" method="get">
                            <div class="col-md-3">
                                <label for="villeDepart" class="form-label">Ville de départ</label>
                                <select name="villeDepart" id="villeDepart" class="form-select">
                                    <option value="">Toutes les villes</option>
                                    <c:forEach items="${villes}" var="ville">
                                        <option value="${ville.idVille}" ${ville.idVille == searchVilleDepart ? 'selected' : ''}>
                                            ${ville.nom} (${ville.pays})
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-3">
                                <label for="villeArrivee" class="form-label">Ville d'arrivée</label>
                                <select name="villeArrivee" id="villeArrivee" class="form-select">
                                    <option value="">Toutes les villes</option>
                                    <c:forEach items="${villes}" var="ville">
                                        <option value="${ville.idVille}" ${ville.idVille == searchVilleArrivee ? 'selected' : ''}>
                                            ${ville.nom} (${ville.pays})
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-2">
                                <label for="dateDepart" class="form-label">Date de départ</label>
                                <input type="date" class="form-control" id="dateDepart" name="dateDepart" 
                                       value="${searchDateDepart}">
                            </div>
                            <div class="col-md-2">
                                <label for="dateArrivee" class="form-label">Date d'arrivée</label>
                                <input type="date" class="form-control" id="dateArrivee" name="dateArrivee" 
                                       value="${searchDateArrivee}">
                            </div>
                            <div class="col-md-2 d-flex align-items-end">
                                <button type="submit" class="btn btn-primary w-100">
                                    <i class="fas fa-search"></i> Rechercher
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <c:if test="${empty vols}">
            <div class="alert alert-info" role="alert">
                <i class="fas fa-info-circle"></i> Aucun vol disponible pour le moment.
            </div>
        </c:if>

        <div class="row">
            <c:forEach items="${vols}" var="vol">
                <div class="col-md-6 mb-4">
                    <div class="card h-100">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-center mb-3">
                                <h5 class="card-title">
                                    ${vol.villeDepart.nom} vers ${vol.villeArrivee.nom}
                                </h5>
                                <span class="badge bg-primary">Vol ${vol.idVol}</span>
                            </div>
                            
                            <div class="row mb-3">
                                <div class="col-6">
                                    <p class="mb-1"><strong>Départ:</strong></p>
                                    <p class="mb-0">${vol.villeDepart.nom} (${vol.villeDepart.pays})</p>
                                    <p class="text-muted">${vol.dateDepart}</p>
                                </div>
                                <div class="col-6">
                                    <p class="mb-1"><strong>Arrivée:</strong></p>
                                    <p class="mb-0">${vol.villeArrivee.nom} (${vol.villeArrivee.pays})</p>
                                    <p class="text-muted">${vol.dateArrivee}</p>
                                </div>
                            </div>
                            
                            <div class="mb-3">
                                <p class="mb-1"><strong>Tarifs disponibles:</strong></p>
                                <div class="d-flex flex-wrap">
                                    <c:forEach items="${vol.tarifs}" var="tarif">
                                        <div class="me-3 mb-2">
                                            <span class="badge bg-info text-dark">
                                                <c:forEach items="${typesSiege}" var="type">
                                                    <c:if test="${type.idType == tarif.idTypeSiege}">
                                                        ${type.nom}
                                                    </c:if>
                                                </c:forEach>
                                                : ${tarif.prix} Ar
                                            </span>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>

                            <div class="text-end">
                                <a href="${pageContext.request.contextPath}/reservation/vol?id=${vol.idVol}" 
                                   class="btn btn-primary">
                                    <i class="fas fa-ticket-alt"></i> Réserver
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 