# WhiteFox Project — New Developer Setup Guide

## Prerequisites (Install these first)

| Tool | Version | Download |
|---|---|---|
| Java JDK | 17 or 21 | adoptium.net |
| PostgreSQL | 13+ | postgresql.org/download |
| Flutter SDK | 3.x stable | flutter.dev |
| Chrome Browser | Latest | (for Flutter web apps) |
| Git | Any | git-scm.com |

---

## Step-by-Step Setup

### STEP 1 — Clone the repository

```bash
git clone <your-repo-url>
```

You should have these folders:
```
whitefoxbackend/       ← Spring Boot backend
admin-panel-main/      ← Admin Flutter app
store_App-main/        ← Store Flutter app
rider_wider-main/      ← Rider Flutter app
customer-main/         ← Customer Flutter app
hq-main/               ← HQ Flutter app
truck_driver-main/     ← Truck Driver Flutter app
```

---

### STEP 2 — Create the PostgreSQL database

Open pgAdmin or psql and run:

```sql
CREATE DATABASE whitefox;
```

> Default credentials expected: `postgres` / `postgres`
> If yours differs, see Step 3.

---

### STEP 3 — Configure DB credentials (skip if using postgres/postgres)

**Windows PowerShell:**
```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/whitefox"
$env:DB_USERNAME="your_username"
$env:DB_PASSWORD="your_password"
```

**macOS/Linux:**
```bash
export DB_URL=jdbc:postgresql://localhost:5432/whitefox
export DB_USERNAME=your_username
export DB_PASSWORD=your_password
```

---

### STEP 4 — Start the backend

```bash
cd whitefoxbackend

# Windows
.\mvnw.cmd spring-boot:run

# macOS/Linux
./mvnw spring-boot:run
```

Wait until you see:
```
╔══════════════════════════════════════════════════════╗
║           WhiteFox Database Seeded ✅                ║
╠══════════════════════════════════════════════════════╣
║  ADMIN   → admin@whitefox.com   / Admin@123          ║
║  HQ      → hq@whitefox.com      / Hq@123456          ║
║  CUSTOMER→ customer@gmail.com   / Customer@123       ║
╚══════════════════════════════════════════════════════╝
```

Backend runs at **http://localhost:8080**

> ✅ DB schema is created automatically
> ✅ Seed users created automatically (only once — not duplicated on restart)
> ✅ Service catalog seeded automatically

---

### STEP 5 — Start the Flutter apps

Open 6 separate terminals:

```bash
# Admin App (port 5500)
cd admin-panel-main && flutter pub get && flutter run -d chrome --web-port 5500

# Store App (port 5501)
cd store_App-main && flutter pub get && flutter run -d chrome --web-port 5501

# Rider App (port 5502)
cd rider_wider-main && flutter pub get && flutter run -d chrome --web-port 5502

# Customer App (port 5503)
cd customer-main && flutter pub get && flutter run -d chrome --web-port 5503

# HQ App (port 5504)
cd hq-main && flutter pub get && flutter run -d chrome --web-port 5504

# Truck Driver App (port 5505)
cd truck_driver-main && flutter pub get && flutter run -d chrome --web-port 5505
```

---

### STEP 6 — First-time setup (do in order)

1. Login to **Admin** (localhost:5500) as `admin@whitefox.com` / `Admin@123`
2. Create a **Store** from the Stores section
3. Create a **Store Manager** user linked to that store
4. Create a **Rider** from the Riders section
5. Create a **Truck Driver** from the Truck Logistics section

---

## Login Credentials

### Auto-created on first backend start

| App | Email | Password |
|---|---|---|
| Admin | admin@whitefox.com | Admin@123 |
| HQ | hq@whitefox.com | Hq@123456 |
| Customer (sample) | customer@gmail.com | Customer@123 |

### Created via Admin portal
Store Managers, Riders, and Truck Drivers must be created through the Admin portal.

---

## Port Reference

| App | Port |
|---|---|
| Backend API | 8080 |
| Admin | 5500 |
| Store | 5501 |
| Rider | 5502 |
| Customer | 5503 |
| HQ | 5504 |
| Truck Driver | 5505 |

---

## Common Issues

| Problem | Fix |
|---|---|
| Backend won't start (JAVA_HOME error) | Install JDK 17/21 and set JAVA_HOME |
| Flutter says "Connection refused" | Start the backend first |
| `whitefox does not exist` | Run `CREATE DATABASE whitefox;` in PostgreSQL |
| Login fails | Check backend started and seeder ran (look for the box in console) |
| QR scanner not working on Chrome | Use manual QR entry — camera scan works only on Android/iOS |
| Dashboard shows "Store ID not found" | Admin must link the store to the user account |
