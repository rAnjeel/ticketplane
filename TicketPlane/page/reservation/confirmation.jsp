<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Confirmation de réservation - AirBooking</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .confirmation-container {
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
        }
        .confirmation-card {
            background-color: white;
            border-radius: 8px;
            padding: 30px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            margin-bottom: 20px;
        }
        .reservation-code {
            background-color: #f8f9fa;
            padding: 15px;
            border-radius: 4px;
            font-size: 1.2em;
            font-weight: bold;
            color: #0d6efd;
            text-align: center;
            margin: 20px 0;
        }
    </style>
</head>
<body>
    <jsp:include page="../components/header.jsp" />

    <div class="container mt-4">
        <div class="confirmation-container">
            <div class="confirmation-card">
                <div class="text-center mb-4">
                    <i class="fas fa-check-circle text-success" style="font-size: 4em;"></i>
                    <h2 class="mt-3">Réservation confirmée !</h2>
                    <p class="text-muted">Votre réservation a été enregistrée avec succès.</p>
                </div>

                <div class="reservation-code">
                    Code de réservation : ${reservation.codeReservation}
                </div>

                <div class="row mb-4">
                    <div class="col-md-6">
                        <h5>Détails du vol</h5>
                        <p><strong>Départ:</strong> ${reservation.vol.villeDepart.nom}</p>
                        <p><strong>Arrivée:</strong> ${reservation.vol.villeArrivee.nom}</p>
                        <p><strong>Date de départ:</strong> ${reservation.vol.dateDepart}</p>
                        <p><strong>Date d'arrivée:</strong> ${reservation.vol.dateArrivee}</p>
                    </div>
                    <div class="col-md-6">
                        <h5>Détails de la réservation</h5>
                        <p><strong>Type de siège:</strong> ${reservation.typeSiege.nom}</p>
                        <p><strong>Prix total:</strong> ${reservation.prixTotal} €</p>
                        <p><strong>Statut:</strong> ${reservation.statut.nom}</p>
                    </div>
                </div>

                <div class="alert alert-info">
                    <i class="fas fa-info-circle me-2"></i>
                    Un email de confirmation vous sera envoyé avec ces informations.
                </div>

                <div class="d-grid gap-2">
                    <a href="${pageContext.request.contextPath}/vol/list" class="btn btn-primary">
                        <i class="fas fa-plane me-2"></i>Retour aux vols
                    </a>
                    <a href="${pageContext.request.contextPath}/profile" class="btn btn-outline-primary">
                        <i class="fas fa-user me-2"></i>Voir mes réservations
                    </a>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 