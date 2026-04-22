# Resolución de conflictos de merge

Si GitHub muestra conflictos en:

- `pom.xml`
- `src/main/java/com/example/purchasereportingapi/config/OpenApiConfig.java`
- `src/main/java/com/example/purchasereportingapi/exception/GlobalExceptionHandler.java`
- `src/main/resources/application.yml`

usa este flujo en local.

## Opción rápida (recomendada)

```bash
git fetch origin
git checkout <tu-rama>
./scripts/resolve_merge_conflicts.sh origin/main
git push
```

> El script toma **la versión de tu rama** (`--ours`) para los 4 archivos conflictivos,
> los marca como resueltos y crea el commit de merge.

## Opción manual

```bash
git fetch origin
git checkout <tu-rama>
git merge origin/main

git checkout --ours pom.xml
git checkout --ours src/main/java/com/example/purchasereportingapi/config/OpenApiConfig.java
git checkout --ours src/main/java/com/example/purchasereportingapi/exception/GlobalExceptionHandler.java
git checkout --ours src/main/resources/application.yml

git add pom.xml \
  src/main/java/com/example/purchasereportingapi/config/OpenApiConfig.java \
  src/main/java/com/example/purchasereportingapi/exception/GlobalExceptionHandler.java \
  src/main/resources/application.yml

git commit -m "chore: resolve merge conflicts taking scaffold baseline"
git push
```

## Validación posterior

```bash
mvn test
```

Si tu entorno no tiene acceso a Maven Central y falla por `403`, valida en CI o en una red con acceso externo.
