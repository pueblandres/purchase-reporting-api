#!/usr/bin/env bash
set -euo pipefail

# Uso:
#   ./scripts/resolve_merge_conflicts.sh <target-branch>
# Ejemplo:
#   ./scripts/resolve_merge_conflicts.sh origin/main

if [[ $# -ne 1 ]]; then
  echo "Uso: $0 <target-branch>"
  exit 1
fi

TARGET_BRANCH="$1"


echo "[1/5] Iniciando merge contra ${TARGET_BRANCH}..."
git merge "${TARGET_BRANCH}" || true

echo "[2/5] Aplicando resolución para archivos reportados en conflicto..."
git checkout --ours pom.xml
git checkout --ours src/main/java/com/example/purchasereportingapi/config/OpenApiConfig.java
git checkout --ours src/main/java/com/example/purchasereportingapi/exception/GlobalExceptionHandler.java
git checkout --ours src/main/resources/application.yml

echo "[3/5] Marcando archivos como resueltos..."
git add pom.xml \
  src/main/java/com/example/purchasereportingapi/config/OpenApiConfig.java \
  src/main/java/com/example/purchasereportingapi/exception/GlobalExceptionHandler.java \
  src/main/resources/application.yml

echo "[4/5] Verificando estado..."
git status --short

echo "[5/5] Creando commit de merge..."
git commit -m "chore: resolve merge conflicts taking scaffold baseline"

echo "Listo. Ahora puedes hacer: git push"
