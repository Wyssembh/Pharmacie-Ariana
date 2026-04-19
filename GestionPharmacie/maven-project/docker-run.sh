#!/bin/bash
set -e

# Autoriser Docker à accéder à l'affichage X11
xhost +local:docker

# Lancer les conteneurs
docker compose up --build

# Révoquer l'accès X11 après fermeture
xhost -local:docker
