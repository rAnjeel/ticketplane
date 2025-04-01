<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Réservation de vol - AirBooking</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .reservation-form {
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
        }
        .flight-details {
            background-color: #f8f9fa;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 30px;
        }
        .form-section {
            background-color: white;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 20px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .seat-card {
            transition: transform 0.2s ease, box-shadow 0.2s ease;
        }
        .seat-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 4px 8px rgba(0,0,0,0.15);
        }
        .seat-card.selected {
            border: 2px solid #007bff;
        }
    </style>
</head>
<body>
    <jsp:include page="../components/header.jsp" />

    <div class="container mt-4">
        <div class="reservation-form">
            <h2 class="mb-4">Réservation de vol</h2>

            <c:if test="${not empty error}">
                <div class="alert alert-danger" role="alert">
                    ${error}
                </div>
            </c:if>

            <div class="flight-details">
                <h4 class="mb-3">Détails du vol</h4>
                <div class="row">
                    <div class="col-md-6">
                        <p><strong>Départ:</strong> ${vol.villeDepart.nom} (${vol.villeDepart.pays})</p>
                        <p><strong>Date de départ:</strong> ${vol.dateDepart}</p>
                    </div>
                    <div class="col-md-6">
                        <p><strong>Arrivée:</strong> ${vol.villeArrivee.nom} (${vol.villeArrivee.pays})</p>
                        <p><strong>Date d'arrivée:</strong> ${vol.dateArrivee}</p>
                    </div>
                </div>
            </div>

            <form action="${pageContext.request.contextPath}/reservation/create" method="post" enctype="multipart/form-data">
                <input type="hidden" name="id_vol" value="${vol.idVol}">
                
                <div class="form-section">
                    <h4 class="mb-3">Type de siège</h4>
                    <div class="row">
                        <!-- Siège Économique -->
                        <div class="col-md-6 mb-3">
                            <div class="card seat-card" id="card-economique">
                                <div class="card-body">
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio" 
                                               name="typeSiege_idType" 
                                               value="1"
                                               id="typeEconomique"
                                               data-price="${prixEconomique}"
                                               onclick="updatePrice('${prixEconomique}'); selectCard('economique')"
                                               required>
                                        <label class="form-check-label" for="typeEconomique">
                                            <h5>Économique</h5>
                                        </label>
                                    </div>
                                    <p class="card-text mt-2">
                                        <strong>Prix:</strong> ${prixEconomique} Ar
                                    </p>
                                    <p class="card-text">
                                        <i class="fas fa-utensils me-2"></i>Repas standard
                                    </p>
                                    <p class="card-text">
                                        <i class="fas fa-suitcase me-2"></i>Bagage 23kg inclus
                                    </p>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Siège Business -->
                        <div class="col-md-6 mb-3">
                            <div class="card seat-card" id="card-business">
                                <div class="card-body">
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio" 
                                               name="typeSiege_idType" 
                                               value="2"
                                               id="typeBusiness"
                                               data-price="${prixBusiness}"
                                               onclick="updatePrice('${prixBusiness}'); selectCard('business')"
                                               required>
                                        <label class="form-check-label" for="typeBusiness">
                                            <h5>Business</h5>
                                        </label>
                                    </div>
                                    <p class="card-text mt-2">
                                        <strong>Prix:</strong> ${prixBusiness} Ar
                                    </p>
                                    <p class="card-text">
                                        <i class="fas fa-utensils me-2"></i>Repas premium
                                    </p>
                                    <p class="card-text">
                                        <i class="fas fa-suitcase me-2"></i>Bagages 2x32kg inclus
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <input type="hidden" id="prixTotal" name="prixTotal" value="">
                </div>

                <div class="form-section">
                    <h4 class="mb-3">Documents du passager</h4>
                    <div class="mb-3">
                        <label for="photoPasseport" class="form-label">Photo du passeport</label>
                        <input type="file" class="form-control" id="photoPasseport" 
                               name="photoPasseport" accept="image/*" required>
                        <div class="form-text">Veuillez fournir une photo claire de votre passeport</div>
                    </div>
                </div>

                <div class="d-grid gap-2">
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-check me-2"></i>Confirmer la réservation
                    </button>
                    <a href="${pageContext.request.contextPath}/vol/list" class="btn btn-secondary">
                        <i class="fas fa-times me-2"></i>Annuler
                    </a>
                </div>
            </form>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function updatePrice(price) {
            document.getElementById('prixTotal').value = parseFloat(price);
        }
        
        function selectCard(type) {
            // Enlever la classe "selected" de toutes les cartes
            document.querySelectorAll('.seat-card').forEach(card => {
                card.classList.remove('selected');
            });
            
            // Ajouter la classe "selected" à la carte sélectionnée
            document.getElementById('card-' + type).classList.add('selected');
        }
    </script>
</body>
</html> 