# Guide des APIs

Toutes les APIs sont accessibles via la **Gateway (port 8888)**. Le routage est basé sur le nom du service enregistré dans Eureka.

## 1. Customer Service
Gère les informations relatives aux clients.

| Action | Méthode | Endpoint Gateway | Description |
| :--- | :--- | :--- | :--- |
| Lister les clients | `GET` | `/CUSTOMER-SERVICE/customers` | Récupère tous les clients. |
| Détails client | `GET` | `/CUSTOMER-SERVICE/customers/{id}` | Récupère un client par son ID. |
| Créer un client | `POST` | `/CUSTOMER-SERVICE/customers` | Ajoute un nouveau client. |

![Réponse API Customers](./images/localhost8888CUSTOMER-SERVICEcustomers.png)

## 2. Account Service
Gère les comptes bancaires et leurs opérations.

| Action | Méthode | Endpoint Gateway | Description |
| :--- | :--- | :--- | :--- |
| Lister les comptes | `GET` | `/ACCOUNT-SERVICE/accounts` | Récupère tous les comptes. |
| Détails compte | `GET` | `/ACCOUNT-SERVICE/accounts/{id}` | Récupère un compte avec les infos client. |
| Transactions | `GET` | `/ACCOUNT-SERVICE/accounts/{id}/operations` | Historique des opérations d'un compte. |

![Réponse API Accounts](./images/localhost8888ACCOUNT-SERVICEaccounts.png)

## 3. Monitoring & Actuator
Chaque microservice expose des points de terminaison de monitoring via Spring Boot Actuator.

*   **Health Check** : `/actuator/health`
*   **Info** : `/actuator/info`
*   **Metrics** : `/actuator/metrics`
*   **Refresh Config** : `/actuator/refresh` (POST uniquement)

---

## Tester avec cURL
Exemple pour récupérer les clients via la Gateway :
```bash
curl http://localhost:8888/CUSTOMER-SERVICE/customers
```

Exemple pour rafraîchir la configuration du Customer Service :
```bash
curl -X POST http://localhost:8081/actuator/refresh
```
