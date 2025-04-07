// Script pour gérer les modals d'édition
document.addEventListener('DOMContentLoaded', function() {
    const editModal = document.getElementById('editModal');
    if (editModal) {
        editModal.addEventListener('show.bs.modal', function(event) {
            const button = event.relatedTarget;
            const code = button.getAttribute('data-code');
            const valeur = button.getAttribute('data-valeur');
            const description = button.getAttribute('data-description');
            
            editModal.querySelector('#code').value = code;
            editModal.querySelector('#valeur').value = valeur;
            if (editModal.querySelector('#description')) {
                editModal.querySelector('#description').value = description;
            }
        });
    }
    
    // Fermer automatiquement les alertes après 5 secondes
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(function(alert) {
        setTimeout(function() {
            const closeButton = alert.querySelector('.btn-close');
            if (closeButton) {
                closeButton.click();
            }
        }, 5000);
    });
}); 