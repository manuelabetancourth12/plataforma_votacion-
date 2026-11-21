# Plataforma de Votación en Vivo de Alta Disponibilidad

Sistema distribuido orientado a eventos (EDA) desarrollado con **Java 17**, **Spring Boot 3.2.3**, **Apache Kafka**, **RabbitMQ**, **PostgreSQL 15** y **Docker Compose**.

## Arquitectura Híbrida

* **Apache Kafka (Fuente de Verdad y Log):**
  * `eligible_voters` *(compactado)*: KTable de votantes elegibles.
  * `raw_votes` *(compactado, clave = `voterId`)*: Flujo de votos para conteo, análisis de fraude y auditoría, conservando el último voto por usuario.
  * `security_alerts`: Alertas emitidas por detección de anomalías por IP.
* **RabbitMQ (Distribución en Tiempo Real):**
  * `live_results_global` *(Fanout Exchange)*: Difusión cada 1 s a los dashboards globales.
  * `live_results_regional` *(Topic Exchange, routing keys `results.<region>`)*: Difusión cada 5 s a los dashboards regionales.
* **PostgreSQL (Red interna `db_internal`, no expuesta al host):**
  * `audit_votes`: Registro auditable del último voto por votante.
  * `security_incidents`: Registro forense de incidentes de fraude por IP.

## Microservicios

| Servicio | Puerto | Responsabilidad |
|---|---|---|
| `voter_registration_service` | - | Carga `voters.json` al arrancar y publica votantes elegibles en `eligible_voters`. |
| `voting_api` | `8080` | Gateway REST (`POST /vote`), valida elegibilidad local, IPs bloqueadas y publica en `raw_votes`. |
| `vote_processor` | - | Conteo global stateful con unicidad por `voterId` y difusión vía RabbitMQ Fanout. |
| `regional_rollup_service` | - | Conteo regional stateful aplicando el patrón *Strategy* y difusión vía RabbitMQ Topic. |
| `bot_detector_service` | - | Detección de ráfagas por IP usando patrón *State* (`NORMAL`, `SOSPECHOSA`, `BLOQUEADA`), emite alertas en Kafka y guarda en PostgreSQL. |
| `analytics_archiver` | - | Persistencia transaccional en PostgreSQL (`audit_votes`) actualizando el último voto del usuario. |
| `global_dashboard_worker` | `8081` | Dashboard web global en tiempo real vía WebSockets (`/ws-global`, `dashboard.html`). |
| `regional_dashboard_worker` | `8082` | Dashboard web regional en tiempo real vía WebSockets (`/ws-regional`, `dashboard.html`). |

## Cómo ejecutar

### 1. Levantar la plataforma completa con Docker Compose
Asegúrate de tener **Docker Desktop** en ejecución y corre:

```bash
docker compose up --build -d
```

### 2. Ejecutar prueba End-to-End
```powershell
.\scripts\test-e2e.ps1
```

### 3. Abrir Dashboards en vivo
* **Global:** http://localhost:8081/dashboard.html
* **Regional:** http://localhost:8082/dashboard.html
* **RabbitMQ Management:** http://localhost:15672 (`guest` / `guest`)

## Pruebas Unitarias Aisladas (JUnit 5 + Mockito)
Cada microservicio ejecuta sus pruebas en memoria sin requerir Kafka, RabbitMQ ni PostgreSQL:

```powershell
mvn -f voting_api\pom.xml test
mvn -f vote_processor\pom.xml test
mvn -f regional_rollup_service\pom.xml test
mvn -f bot_detector_service\pom.xml test
mvn -f analytics_archiver\pom.xml test
mvn -f voter_registration_service\pom.xml test
mvn -f global_dashboard_worker\pom.xml test
mvn -f regional_dashboard_worker\pom.xml test
```
