<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
    <nav class="navbar navbar-expand-lg navbar-dark" style="background-color: rgb(70, 136, 161);">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/">
                <i class="fas fa-plane-departure me-2"></i>
                AirBooking
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link active" href="${pageContext.request.contextPath}/">Accueil</a>
                    </li>
                    <% if(session.getAttribute("userRole") != null && session.getAttribute("userRole").equals("ADMIN")) { %>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/admin/database">Database</a>
                    </li>
                    <% } %>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="volsDropdown" role="button"
                            data-bs-toggle="dropdown" aria-expanded="false">
                            Vols
                        </a>
                        <ul class="dropdown-menu" aria-labelledby="volsDropdown">
                            <li>
                                <a class="dropdown-item" href="${pageContext.request.contextPath}/vol/list">
                                    <i class="fas fa-list me-2"></i>Liste des vols
                                </a>
                            </li>
                            <li>
                                <a class="dropdown-item" href="${pageContext.request.contextPath}/reservation/mesReservations">
                                    <i class="fas fa-list me-2"></i>Mes reservations
                                </a>
                            </li>
                            <% if(session.getAttribute("user") != null) { %>
                                <li>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/reservation/vol">
                                        <i class="fas fa-ticket-alt me-2"></i>Réserver un vol
                                    </a>
                                </li>
                            <% } %>
                            <% if(session.getAttribute("userRole") != null && 
                                  session.getAttribute("userRole").equals("ADMIN")) { %>
                                <li>
                                    <hr class="dropdown-divider">
                                </li>
                                <li>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/vol/insertForm">
                                        <i class="fas fa-plus me-2"></i>Ajouter un vol
                                    </a>
                                </li>
                            <% } %>
                        </ul>
                    </li>
                </ul>
                <ul class="navbar-nav">
                    <% if(session.getAttribute("user") != null) { %>
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                                data-bs-toggle="dropdown" aria-expanded="false">
                                <i class="fas fa-user me-1"></i>
                                ${sessionScope.user.email}
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdown">
                                <li>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/profile">
                                        <i class="fas fa-id-card me-2"></i>Profile
                                    </a>
                                </li>
                                <li>
                                    <hr class="dropdown-divider">
                                </li>
                                <li>
                                    <a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout">
                                        <i class="fas fa-sign-out-alt me-2"></i>Déconnexion
                                    </a>
                                </li>
                            </ul>
                        </li>
                    <% } else { %>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/loginForm">
                                <i class="fas fa-sign-in-alt me-1"></i>
                                Connexion
                            </a>
                        </li>
                    <% } %>
                </ul>
            </div>
        </div>
    </nav>