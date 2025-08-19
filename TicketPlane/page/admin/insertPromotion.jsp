<form action="/promotion/create" method="post">
  <div class="mb-3">
    <label for="type_siege">Type de siège</label>
    <select name="type_siege" id="type_siege" class="form-control" required>
      <!-- rempli dynamiquement depuis la table TypeSiege -->
      <option value="1">Économique</option>
      <option value="2">Business</option>
      <option value="3">VIP</option>
    </select>
  </div>

  <div class="mb-3">
    <label for="date_debut">Date début</label>
    <input type="date" name="date_debut" id="date_debut" class="form-control" required>
  </div>

  <div class="mb-3">
    <label for="date_fin">Date fin</label>
    <input type="date" name="date_fin" id="date_fin" class="form-control" required>
  </div>

  <div class="mb-3">
    <label for="pourcentage">Pourcentage</label>
    <input type="number" step="0.01" name="pourcentage" id="pourcentage" class="form-control" required>
  </div>

  <div class="mb-3">
    <label for="nombre">Nombre</label>
    <input type="number" name="nombre" id="nombre" class="form-control" required>
  </div>

  <button type="submit" class="btn btn-primary">Enregistrer</button>
</form>
