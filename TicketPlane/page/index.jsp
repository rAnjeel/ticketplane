<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>AirBooking - Votre Partenaire de Voyage</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@6.4.2/css/all.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/gh/rAnjeel/ticketplane@main/assets/css/style.css" rel="stylesheet">
</head>

<body>
  <jsp:include page="components/header.jsp" />

  <section class="hero-section">
    <div class="container">
      <h1 class="display-4">Voyagez en Toute Sérénité</h1>
      <p class="lead">Découvrez nos destinations et réservez votre prochain voyage</p>
      <a href="${pageContext.request.contextPath}/vol/list" class="btn btn-light btn-lg mt-3">
        Voir les Vols Disponibles
      </a>
    </div>
  </section>

  <section class="py-5">
    <div class="container">
      <h2 class="text-center section-title">Pourquoi Nous Choisir ?</h2>
      <div class="row g-4">
        <div class="col-md-4">
          <div class="card feature-card h-100 p-4">
            <div class="card-body text-center">
              <i class="fas fa-globe feature-icon"></i>
              <h3 class="h5">Destinations Multiples</h3>
              <p class="text-muted">Des vols vers plus de 100 destinations à travers le monde.</p>
            </div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="card feature-card h-100 p-4">
            <div class="card-body text-center">
              <i class="fas fa-shield-alt feature-icon"></i>
              <h3 class="h5">Sécurité Garantie</h3>
              <p class="text-muted">Votre sécurité est notre priorité absolue.</p>
            </div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="card feature-card h-100 p-4">
            <div class="card-body text-center">
              <i class="fas fa-tag feature-icon"></i>
              <h3 class="h5">Prix Compétitifs</h3>
              <p class="text-muted">Les meilleurs tarifs pour vos voyages.</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>

</html>