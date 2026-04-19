#!/bin/bash
set -e

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"

echo "=== Setup GestionPharmacie ==="

# 1. Installer les dépendances
echo "[1/4] Installation de Maven et MySQL..."
sudo apt-get install -y maven mysql-server 2>&1 | grep -E "install|already|Err" || true

# 2. Démarrer MySQL
echo "[2/4] Démarrage de MySQL..."
sudo service mysql start 2>/dev/null || sudo systemctl start mysql 2>/dev/null || true
sleep 2

# 3. Créer la base de données
echo "[3/4] Création de la base de données..."
sudo mysql -u root < "$PROJECT_DIR/pharmacie.sql" 2>/dev/null || \
mysql -u root < "$PROJECT_DIR/pharmacie.sql" 2>/dev/null || \
echo "  -> Base déjà existante ou accès root sans mot de passe requis"

# 4. Compiler et lancer
echo "[4/4] Compilation et lancement..."
cd "$PROJECT_DIR"
mvn clean javafx:run -q

echo "=== Terminé ==="
