#  Service Level Objectives (SLO) — E-Banking Stack
---

##  1. Introduction & Méthodologie
Ce document définit les objectifs de niveau de service (SLO) pour la stack microservices. Nous utilisons la **méthode RED** (Rate, Errors, Duration) pour mesurer la performance et la fiabilité.

> **Objectif principal :** Garantir une expérience utilisateur optimale sur le front-end Angular tout en maintenant la stabilité du backend Spring Boot.

---

##  2. SLI définis (Service Level Indicators)

### A. Disponibilité (Success Rate)
Proportion de requêtes HTTP réussies (codes 2xx, 3xx, 4xx) par rapport au total, excluant les erreurs serveur (5xx).
- **Requête PromQL :**
```promql
1 - (sum(rate(http_server_requests_seconds_count{status=~"5.."}[5m])) / sum(rate(http_server_requests_seconds_count[5m])))
```
![SLI Availability](../docs/images/les404dansPrometheussumratehttp_server_requests_seconds_count.png)

### B. Latence (p99 Duration)
Temps de réponse en millisecondes sous lequel 99% des requêtes sont traitées.
- **Requête PromQL :**
```promql
histogram_quantile(0.99, sum(rate(http_server_requests_seconds_bucket[5m])) by (le, job)) * 1000
```
![SLI Latency](../docs/images/requetepourhistogram_quantile.png)

---

## 🏆 3. SLO (Service Level Objectives)

| Indicateur | Cible (30j) | Tolérance d'erreur | Justification Métier |
| :--- | :--- | :--- | :--- |
| **Disponibilité** | **≥ 99,5%** | 0,5% (216 min/mois) | Critique pour les transactions bancaires. |
| **Latence p99** | **≤ 500ms** | 0,5% de requêtes lentes | Éviter les timeouts sur l'interface Angular. |

---

##  4. Calcul de l'Error Budget (Budget d'Erreur)

Le budget d'erreur est la quantité d'indisponibilité que nous acceptons de tolérer avant de stopper les nouveaux déploiements.

### Fenêtre glissante de 30 jours :
- **Total minutes :** 43 200 min
- **Indisponibilité tolérée (0,5%) :** **3 heures 36 minutes**

### Distribution de la latence (Visualisation des Buckets) :
Pour respecter notre SLO de latence, nous surveillons la répartition des requêtes dans les buckets :
![Latency Buckets](../docs/images/http_server_requests_seconds_bucketgraph.png)

---

##  5. État actuel & Consommation du Budget

| Métrique | Valeur Actuelle | Budget Restant | Statut |
| :--- | :--- | :--- | :--- |
| **Disponibilité** | 100% | 100% | ✅ OK |
| **Latence p99** | ~45ms | 100% | ✅ OK |

**Preuve de performance (Histogramme) :**
![Histogram Graph](../docs/images/requetepourhistogram_quantilegraph.png)

---

##  6. Politique de Gel (Error Budget Policy)

Si le budget d'erreur tombe sous les seuils suivants :
1. **Budget < 20% (Restant < 43 min) :** Alerte Orange. Analyse post-mortem obligatoire pour chaque incident.
2. **Budget < 10% (Restant < 21 min) :** Gel des déploiements de fonctionnalités. Seuls les correctifs de sécurité et de stabilité sont autorisés.
3. **Budget = 0% :** Arrêt total des changements. Priorité absolue à la dette technique et à la fiabilité.

---

##  7. Dashboard de Monitoring Global

Le dashboard Grafana centralise tous les indicateurs clés pour une visibilité immédiate.

![Grafana Dashboard](../docs/images/grafana-explore-p99-latency.png)

---

##  8. Alerting & Réactivité

| Alerte | Condition (PromQL) | Sévérité | Action attendue |
| :--- | :--- | :--- | :--- |
| **ServiceDown** | `up == 0` | 🔴 Critical | Intervention immédiate (On-call) |
| **HighErrorRate** | `ErrorRate > 5%` | 🔴 Critical | Rollback du dernier déploiement |
| **HighLatency** | `p99 > 500ms` | 🟡 Warning | Analyse de la base de données / cache |
| **JVM Memory** | `Used > 85%` | 🟡 Warning | Analyse des fuites mémoire / Scaling |

![JVM Memory Metric](../docs/images/%20httplocalhost8082actuatorprometheusgrepjvm_memory_used_bytes.png)

---
