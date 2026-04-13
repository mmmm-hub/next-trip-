# Next Trip

Application **Angular 21** + **Spring Boot 4** + **MongoDB** pour la recherche et la planification de voyages.

## Structure du dépôt

```
next-trip/
├── next-trip-backend/     # API REST (JWT, réservations, météo, vols, IA…)
├── next-trip-frontend/    # SPA Angular
├── docker-compose.yml     # Mongo + backend + frontend (nginx)
└── README.md
```

## Prérequis

- Java **17+**, Maven **3.9+**
- Node **20+**, npm
- MongoDB **6+** (local ou Docker)

## Démarrage local

### 1. MongoDB

```bash
docker run -d -p 27017:27017 --name nexttrip-mongo mongo:7
```

### 2. Backend

```bash
cd next-trip-backend
mvn spring-boot:run
```

- API : `http://localhost:8080`
- Variables utiles (voir `application.yml`) : `JWT_SECRET`, `OPENWEATHER_API_KEY`, `AMADEUS_API_KEY`, `AMADEUS_API_SECRET`

### 3. Frontend

```bash
cd next-trip-frontend
npm install
npm start
```

- UI : `http://localhost:4200`
- `environment.ts` pointe vers `http://localhost:8080/api`

### Compte administrateur

Les inscriptions créent un rôle **USER**. Pour un **ADMIN**, mettre à jour le document dans MongoDB :

```js
db.users.updateOne(
  { email: "vous@example.com" },
  { $set: { role: "ADMIN" } }
);
```

## Fonctionnalités principales (implémentées)

| Domaine | Détail |
|--------|--------|
| **Auth JWT** | `POST /api/auth/register`, `POST /api/auth/login` ; filtres JWT ; rôles USER / ADMIN |
| **Sécurité** | Destinations en lecture publique ; création / édition / suppression **ADMIN** ; wishlist / réservations / avis (POST) **authentifiés** |
| **Wishlist** | Persistance Mongo ; le front synchronise avec l’API si l’utilisateur est connecté (sinon localStorage) |
| **Réservations** | `POST /api/bookings`, `GET /api/bookings/me`, `GET /api/bookings/{id}/invoice` (facture JSON + notification) |
| **Météo** | `GET /api/weather/destination/{id}` (OpenWeather si `OPENWEATHER_API_KEY`, sinon mock) |
| **Vols** | `GET /api/flights/search` (Amadeus si clés, sinon mock) |
| **IA** | Préférences utilisateur + historique recherche ; `POST /api/search-history` |
| **Notifications** | Stockées en base après réservation ; `GET /api/notifications/me` |
| **Frontend** | Pages login / register, guards `auth` + `admin`, intercepteur Bearer, thème clair/sombre |

## Docker (stack complète)

À la racine du projet :

```bash
docker compose up --build
```

- Frontend (nginx + proxy `/api`) : **http://localhost**
- API directe (optionnel) : **http://localhost:8080**
- Mongo : port **27017**

Le build Angular production utilise `environment.prod.ts` (`apiUrl: '/api'`) pour passer par le proxy nginx vers le backend.

## Étapes d’extension recommandées

1. **Tests** : tests d’intégration Spring (`@SpringBootTest` + Testcontainers Mongo) ; tests Angular (Vitest) pour auth et wishlist.
2. **CI** : GitHub Actions — `mvn verify`, `npm run build`.
3. **HTTPS** : reverse proxy (Traefik / Caddy) devant nginx + certificats.
4. **Observabilité** : logs structurés, correlation-id, métriques Actuator.
5. **Paiement** : remplacer la simulation de réservation par un PSP (Stripe, etc.).

## Licence

Projet de démonstration — adapter selon votre contexte.
