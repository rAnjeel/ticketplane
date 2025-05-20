<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
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
                    <i class="fas fa-exclamation-circle"></i>
                    <c:out value="${error}" escapeXml="false" />
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
                    <h4 class="mb-3">Informations passagers</h4>
                    
                    <div class="alert alert-info mb-3">
                        <i class="fas fa-info-circle me-2"></i>
                        <strong>Réduction enfant:</strong> Un enfant est âgé de moins de ${ageMaxEnfant} ans 
                        et bénéficie d'une réduction de ${tauxReductionEnfant}% sur le prix du billet.
                    </div>
                    
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="nbAdultes" class="form-label">Nombre d'adultes</label>
                            <input type="number" class="form-control" id="nbAdultes" name="nbAdultes" 
                                   min="1" value="1" required onchange="calculerPrixTotal()">
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="nbEnfants" class="form-label">Nombre d'enfants</label>
                            <input type="number" class="form-control" id="nbEnfants" name="nbEnfants" 
                                   min="0" value="0" required onchange="calculerPrixTotal()">
                        </div>
                    </div>
                    
                    <div class="alert alert-success mt-2" id="resumePrix">
                        <strong>Prix total: <span id="affichagePrixTotal">0</span> Ar</strong>
                    </div>
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
        let prixBase = 0;
        const tauxReduction = parseInt("${tauxReductionEnfant}") || 0;

        function updatePrice(price) {
            prixBase = parseFloat(price);
            calculerPrixTotal();
        }
        
        function selectCard(type) {
            document.querySelectorAll('.seat-card').forEach(card => {
                card.classList.remove('selected');
            });
            document.getElementById('card-' + type).classList.add('selected');
        }
        
        function calculerPrixTotal() {
            const nbAdultes = parseInt(document.getElementById('nbAdultes').value) || 0;
            const nbEnfants = parseInt(document.getElementById('nbEnfants').value) || 0;

            if (!prixBase || prixBase === 0) {
                document.getElementById('affichagePrixTotal').textContent = '0.00';
                document.getElementById('prixTotal').value = '0.00';
                return;
            }

            const prixAdultes = prixBase * nbAdultes;
            const prixEnfants = prixBase * nbEnfants * (1 - tauxReduction/100);
            const prixTotal = prixAdultes + prixEnfants;

            document.getElementById('affichagePrixTotal').textContent = prixTotal.toLocaleString('fr-FR', {minimumFractionDigits: 2});
            document.getElementById('prixTotal').value = prixTotal.toFixed(2);
        }
        
        document.addEventListener('DOMContentLoaded', function() {
            // Initialiser le prix de base
            const selectedType = document.querySelector('input[name="typeSiege_idType"]:checked');
            if (selectedType) {
                prixBase = parseFloat(selectedType.dataset.price);
                calculerPrixTotal();
            }
            
            // Ajouter les écouteurs d'événements
            document.querySelectorAll('input[name="typeSiege_idType"]').forEach(radio => {
                radio.addEventListener('change', function() {
                    updatePrice(this.dataset.price);
                });
            });
            
            document.getElementById('nbAdultes').addEventListener('change', calculerPrixTotal);
            document.getElementById('nbEnfants').addEventListener('change', calculerPrixTotal);
        });
    </script>
</body>
</html> 