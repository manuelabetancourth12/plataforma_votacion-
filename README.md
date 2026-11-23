# Plataforma de Votación en Vivo de Alta Disponibilidad

Sistema distribuido orientado a eventos (EDA) desarrollado con **Java 17**, **Spring Boot 3.2.3**, **Apache Kafka**, **RabbitMQ**, **PostgreSQL 15**, **Nginx** y **Docker Compose**.

## Arquitectura Híbrida

* **Portal Frontend Unificado (Nginx en puerto 80):** Punto de entrada único (`http://localhost`) que sirve la interfaz web de votación y actúa como proxy inverso hacia los servicios internos (`/vote`, `/ws-global`, `/ws-regional`).
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

## Microservicios y Componentes

| Servicio | Puerto Host (Prod) | Puerto Host (Dev) | Responsabilidad |
|---|---|---|---|
| `frontend` | `80` | `80` | Proxy inverso Nginx y servidor web para la interfaz de votación unificada. |
| `voting_api` | *(Interno)* | `8080` | Gateway REST (`POST /vote`), valida elegibilidad local, IPs bloqueadas y publica en `raw_votes`. |
| `global_dashboard_worker` | *(Interno)* | `8081` | Dashboard web global en tiempo real vía WebSockets (`/ws-global`). |
| `regional_dashboard_worker` | *(Interno)* | `8082` | Dashboard web regional en tiempo real vía WebSockets (`/ws-regional`). |
| `voter_registration_service` | *(Interno)* | *(Interno)* | Carga `voters.json` al arrancar y publica votantes elegibles en `eligible_voters`. |
| `vote_processor` | *(Interno)* | *(Interno)* | Conteo global stateful con unicidad por `voterId` y difusión vía RabbitMQ Fanout. |
| `regional_rollup_service` | *(Interno)* | *(Interno)* | Conteo regional stateful aplicando el patrón *Strategy* y difusión vía RabbitMQ Topic. |
| `bot_detector_service` | *(Interno)* | *(Interno)* | Detección de ráfagas por IP usando patrón *State*, emite alertas en Kafka y guarda en PostgreSQL. |
| `analytics_archiver` | *(Interno)* | *(Interno)* | Persistencia transaccional en PostgreSQL (`audit_votes`) actualizando el último voto del usuario. |

## Cómo ejecutar

### 1. Levantar la plataforma completa
Asegúrate de tener **Docker Desktop** en ejecución y corre:

```bash
docker compose up --build -d
```
* **Portal Principal:** [http://localhost](http://localhost) (Acceso a votación, resultados globales y regionales unificados).
* **Servicios directos:** `voting-api` (`8080`), `global-dashboard` (`8081`), `regional-dashboard` (`8082`).
* **RabbitMQ Management:** [http://localhost:15672](http://localhost:15672) (`guest` / `guest`).

### 2. Ejecutar prueba End-to-End
```powershell
.\scripts\test-e2e.ps1
```

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
