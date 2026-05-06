# 🛠 Guide de Validation & Tests — Stack Observabilité

Ce document détaille les procédures pour vérifier le bon fonctionnement de l'instrumentation, de la collecte et de l'alerting dans l'architecture microservices.

---

## 1. Validation de l'Instrumentation (Actuator)
Chaque microservice doit exposer ses métriques au format Prometheus via Spring Boot Actuator.

**Tests à effectuer :**
```bash
# Vérifier l'exposition des métriques pour Customer Service
curl -s http://localhost:8081/actuator/prometheus | grep http_server_requests_seconds_count

# Vérifier l'exposition des métriques pour Account Service
curl -s http://localhost:8082/actuator/prometheus | grep jvm_memory_used_bytes

# Vérifier l'exposition des métriques pour Gateway Service
curl -s http://localhost:8888/actuator/prometheus | grep spring_cloud_gateway_routes_count
```
✅ **Critère de succès :** Les commandes doivent retourner une liste de métriques avec des valeurs numériques (ex: `http_server_requests_seconds_count{...} 50.0`).

---

## 2. Validation de la Collecte (Prometheus)
Vérifier que le serveur Prometheus récupère correctement les données de tous les services configurés.

**Procédure :**
1. Ouvrez votre navigateur sur [http://localhost:9090/targets](http://localhost:9090/targets).
2. Localisez les cibles (targets) définies.

✅ **Critère de succès :** Les 5 services (`customer-service`, `account-service`, `gateway-service`, `config-service`, `discovery-service`) doivent afficher l'état **UP** en vert.

---

## 3. Validation de la Visualisation (Grafana)
Vérifier que les données sont correctement agrégées dans les graphiques.

**Procédure :**
1. Accédez à [http://localhost:3100](http://localhost:3100) (Identifiants par défaut : `admin` / `devops2024`).
2. Allez dans le menu **Explore** et exécutez la requête PromQL pour la latence p99 :
   ```promql
   histogram_quantile(0.99, sum(rate(http_server_requests_seconds_bucket[5m])) by (le, job))
   ```

✅ **Critère de succès :** Un graphique doit s'afficher montrant les différentes courbes de latence par service.

---

## 4. Test des Règles d'Alerte (Alertmanager)
Validation du déclenchement automatique des alertes en cas de dégradation du service.

### Test A : Déclenchement de `HighErrorRate`
Générez artificiellement des erreurs 404 via la Gateway :
```bash
for i in $(seq 1 100); do 
  curl -s -o /dev/null -w "%{http_code}
" http://localhost:8888/api/v1/endpoint-invalide; 
done
```
**Vérification :** 
*   Consultez [http://localhost:9090/alerts](http://localhost:9090/alerts).
*   L'alerte `HighErrorRate` doit passer en état **PENDING** puis **FIRING** après 2 minutes.

### Test B : Déclenchement de `ServiceDown`
Simulez l'arrêt brutal d'un service :
```bash
docker stop enset-account-service
```
**Vérification :**
*   Vérifiez que le service passe en **DOWN** dans Prometheus.
*   L'alerte `ServiceDown` doit s'activer après 1 minute.
*   **Restauration :** `docker start enset-account-service`.

---

## 5. Validation des SLO (Service Level Objectives)
Mesurer la conformité réelle par rapport aux objectifs définis dans `slo.md`.

**Requête de calcul de disponibilité réelle :**
```promql
sum(rate(http_server_requests_seconds_count{status!~"5.."}[1h])) 
/ 
sum(rate(http_server_requests_seconds_count[1h]))
```
✅ **Objectif :** La valeur retournée doit être supérieure ou égale à **0.995** (99.5%).

---

## 📋 Rappel des Ports et Accès
| Service | URL Locale | Identifiants |
| :--- | :--- | :--- |
| **Discovery (Eureka)** | [http://localhost:8761](http://localhost:8761) | - |
| **Gateway** | [http://localhost:8888](http://localhost:8888) | - |
| **Prometheus** | [http://localhost:9090](http://localhost:9090) | - |
| **Grafana** | [http://localhost:3100](http://localhost:3100) | `admin` / `devops2024` |
| **Alertmanager** | [http://localhost:9093](http://localhost:9093) | - |
