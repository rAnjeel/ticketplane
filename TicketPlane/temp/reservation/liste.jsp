<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Mes Réservations - AirBooking</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@6.4.2/css/all.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/gh/rAnjeel/ticketplane@main/assets/css/style.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="../components/header.jsp" />
    
    <div class="container mt-5">
        <h2 class="mb-4">
            <i class="fas fa-ticket-alt"></i> Mes Réservations
        </h2>

        <c:if test="${not empty message}">
            <div class="alert alert-success" role="alert">
                <i class="fas fa-check-circle"></i> ${message}
            </div>
        </c:if>

        <c:if test="${not empty error}">
            <div class="alert alert-danger" role="alert">
                <i class="fas fa-exclamation-circle"></i> ${error}
            </div>
        </c:if>

        <c:if test="${empty reservations}">
            <div class="alert alert-info" role="alert">
                <i class="fas fa-info-circle"></i> Vous n'avez aucune réservation pour le moment.
            </div>
        </c:if>

        <div class="row">
            <c:forEach items="${reservations}" var="reservation">
                <div class="col-md-6 mb-4">
                    <div class="card h-100">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-center mb-3">
                                <h5 class="card-title">
                                    ${reservation.vol.villeDepart.nom} vers ${reservation.vol.villeArrivee.nom}
                                </h5>
                                <span class="badge bg-primary">Réservation #${reservation.id}</span>
                            </div>
                            
                            <div class="row mb-3">
                                <div class="col-6">
                                    <p class="mb-1"><strong>Vol:</strong></p>
                                    <p class="mb-0">Vol #${reservation.vol.idVol}</p>
                                    <p class="text-muted">${reservation.vol.dateDepart}</p>
                                </div>
                                <div class="col-6">
                                    <p class="mb-1"><strong>Statut:</strong></p>
                                    <c:choose>
                                        <c:when test="${reservation.statut.idStatut == 1}">
                                            <span class="badge bg-success text-dark">Confirmée</span>
                                        </c:when>
                                        <c:when test="${reservation.statut.idStatut == 2}">
                                            <span class="badge bg-warning">En attente</span>
                                        </c:when>
                                        <c:when test="${reservation.statut.idStatut == 3}">
                                            <span class="badge bg-danger">Annulée</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-secondary">${reservation.statut.nom}</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                            
                            <div class="mb-3">
                                <div class="row">
                                    <div class="col-6">
                                        <p class="mb-1"><strong>Type de siège:</strong></p>
                                        <p class="mb-0">${reservation.typeSiege.nom}</p>
                                    </div>
                                    <div class="col-6">
                                        <p class="mb-1"><strong>Prix total:</strong></p>
                                        <p class="mb-0"><span class="badge bg-info text-dark">${reservation.prixTotal} Ar</span></p>
                                    </div>
                                </div>
                            </div>
                            
                            <div class="mb-3">
                                <p class="mb-1"><strong>Date de réservation:</strong></p>
                                <p class="mb-0">${reservation.dateReservation}</p>
                            </div>

                            <div class="text-end">
                                <c:if test="${reservation.statut.idStatut == 1}">
                                    <a href="${pageContext.request.contextPath}/reservation/annuler?id=${reservation.id}" 
                                       class="btn btn-outline-danger btn-sm me-2" 
                                       onclick="return confirm('Êtes-vous sûr de vouloir annuler cette réservation?');">
                                        <i class="fas fa-times"></i> Annuler
                                    </a>
                                </c:if>
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