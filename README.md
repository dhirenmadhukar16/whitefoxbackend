# WhiteFox Backend 🦊

Spring Boot REST API for the WhiteFox laundry service platform.

---

## Prerequisites

| Tool | Version |
|---|---|
| Java | 17+ (21 recommended) |
| PostgreSQL | 13+ |
| Maven | 3.8+ (or use `./mvnw`) |

---

## Quick Start

### 1. Create the database
```sql
CREATE DATABASE whitefox;
```
Default credentials expected: **username** `postgres`, **password** `postgres`.  
Override via environment variables (see below).

### 2. Run the backend
```bash
./mvnw spring-boot:run
```
The server starts on **http://localhost:8080**

### 3. That's it!
On first boot, the `DatabaseSeeder` automatically creates:

| Role | Email | Password |
|---|---|---|
| 🔑 Super Admin | `admin@whitefox.com` | `Admin@123` |
| 🏭 HQ Admin | `hq@whitefox.com` | `Hq@123456` |
| 👤 Sample Customer | `customer@gmail.com` | `Customer@123` |

> ⚠️ **Stores, Store Managers, Riders, and Truck Drivers are NOT pre-seeded.**  
> Create them through the Admin portal after logging in as `admin@whitefox.com`.

---

## Environment Variables

Override defaults without editing any files:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/whitefox
export DB_USERNAME=postgres
export DB_PASSWORD=yourpassword
export JWT_SECRET=your-super-secret-key
export RAZORPAY_KEY_ID=rzp_test_xxx
export RAZORPAY_KEY_SECRET=your_razorpay_secret
```

---

## Flutter Apps

| App | Port | Who uses it |
|---|---|---|
| Admin | 5500 | Company admin to manage everything |
| Store | 5501 | Store managers |
| Rider | 5502 | Pickup & delivery riders |
| Customer | 5503 | End customers |
| HQ | 5504 | Head quarter operations |
| Truck Driver | 5505 | Truck drivers for store↔HQ transport |

---

## Full Workflow

```
Customer App   → Places order
Store App      → Confirms pickup, assigns rider
Rider App      → Does pickup, generates pickup bill
Store App      → Verifies bill → confirms order → generates QR codes (1 per garment)
Truck App      → Scans QR at store → SENT_TO_HQ  (automatic)
HQ App         → Scans incoming QR → RECEIVED_AT_HQ (automatic)
HQ App         → Processes garments through cleaning stages
Truck App      → Scans QR at HQ → SENT_TO_STORE (automatic)
Store App      → Scans returning garments → RECEIVED_AT_STORE_AFTER_PROCESSING (automatic)
Store App      → Assigns delivery rider
Rider App      → Delivers to customer
```

---

## API Base URL

All endpoints: `http://localhost:8080`

| Prefix | Description |
|---|---|
| `/api/v1/auth` | Login, register |
| `/api/admin` | Admin operations |
| `/api/store-ops` | Store operations |
| `/api/store-app` | Store app helpers (riders, assignments) |
| `/api/tracking` | QR scanning & garment tracking |
| `/api/hq` | HQ operations |
| `/api/truck-logistics` | Truck driver operations |
| `/api/customer` | Customer-facing APIs |
