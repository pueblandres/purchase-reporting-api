# purchase-reporting-api

API REST profesional con Java 21 + Spring Boot para gestionar usuarios, productos, compras y reportes de consumo.

## Stack técnico

- Java 21
- Maven
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Spring Validation
- Spring Security (base preparada)
- Lombok
- MyBatis (reportes)
- Oracle DB (target)
- Swagger/OpenAPI
- JUnit 5 + Mockito
- JaCoCo

## Arquitectura

Arquitectura en capas:

- `controller`: endpoints REST.
- `service` / `service/impl`: lógica de negocio.
- `repository`: CRUD JPA.
- `entity`: modelo persistente.
- `dto/request` y `dto/response`: contrato de entrada/salida.
- `mapper`: mapeo DTO/entidades y mapeo MyBatis.
- `report`: DTOs de reportes agregados.
- `exception`: manejo global de excepciones.
- `config`: seguridad y OpenAPI.

## Módulos y endpoints

### Users

- `POST /api/users` crear usuario.
- `GET /api/users` listar usuarios.
- `GET /api/users/{id}` obtener por id.

### Products

- `POST /api/products` crear producto.
- `GET /api/products` listar productos.
- `GET /api/products/{id}` obtener por id.

### Purchases

- `POST /api/purchases` registrar compra con múltiples items.
- `GET /api/purchases` listar compras.
- `GET /api/purchases/{id}` obtener compra por id.
- `GET /api/purchases/between-dates?start=...&end=...` compras entre fechas.

### Reports

- `GET /api/reports/total-spent-by-user` total gastado por usuario.
- `GET /api/reports/top-products` productos más comprados.

## Configuración rápida

Editar `src/main/resources/application.yml` con tus credenciales Oracle:

```yaml
spring:
  datasource:
    url: jdbc:oracle:thin:@localhost:1521/XEPDB1
    username: purchase_user
    password: purchase_pass
```

## Ejecutar

```bash
mvn clean spring-boot:run
```

## Documentación Swagger

- UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api-docs`

## Tests y cobertura

```bash
mvn clean test
```

Reporte JaCoCo:

- `target/site/jacoco/index.html`
