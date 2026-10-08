# Librora - Library Management System

A modern, modular library management system built with Java 25, Javalin, Hibernate, and Pebble templates.

## Overview

Librora is a comprehensive library management application designed to handle the complete lifecycle of library operations: catalog management, stock tracking, client management, reservations, loans, returns, and accounting.

### Key Features

- **Catalog Management**: Works, editions, authors, publishers, categories, languages, formats
- **Stock Management**: Physical items tracking with states (AVAILABLE, RESERVED, BORROWED, REVIEW, DAMAGED, RETIRED)
- **Client Management**: CASUAL/MEMBER types with different loan policies
- **Reservation System**: Full workflow from reservation to return with deposit management
- **Accounting**: Payments, expenses, payroll, refunds, payment methods
- **Admin Dashboard**: Configuration, user management, audit logs
- **Public Catalog**: Guest reservations with DNI validation
- **Real-time Notifications**: SSE-based notification system

---

## Instalación (beta distribuible)

Librora se distribuye como **una aplicación de escritorio autosuficiente**: cada
instalador incluye su propio runtime de Java. El usuario final **no necesita
instalar Java, ni base de datos, ni proxy, ni conexión a internet**.

| Sistema | Artefacto | Notas |
|---|---|---|
| Linux x86_64 | `Librora-0.1.0-beta-x86_64.AppImage` | `chmod +x` y ejecutar. Sin instalación. |
| Windows x86_64 | `Librora-0.1.0-beta.exe` | Instalador con JRE embebido (WiX). |
| macOS arm64 | `Librora-0.1.0-beta-aarch64.dmg` | Arrastrar a *Aplicaciones*. |
| macOS Intel | `Librora-0.1.0-beta-x86_64.dmg` | Arrastrar a *Aplicaciones*. |

Los tres formatos abren una ventana con el progreso de arranque y, al terminar,
abren el navegador en la interfaz web.

### Avisos de seguridad del sistema operativo

Son inevitables al no haber una CA que firme el ejecutable:

- **macOS**: Gatekeeper bloqueará la primera apertura.
  Clic derecho sobre `Librora.app` → *Abrir* → *Abrir* en el aviso. Una sola vez.
- **Windows**: SmartScreen muestra «Windows protected your PC».
  Clic en *Más información* → *Ejecutar de todas formas*.
- **Navegador**: la primera visita a `https://librora.local:8443` muestra
  "La conexión no es privada" porque el certificado es autofirmado.
  Clic en *Avanzado* → *Continuar a librora.local*. Se recuerda para siempre.

---

## Primer arranque

1. Se abre la ventana **Librora** con una barra de progreso por pasos
   (puertos → almacenamiento → base de datos → usuario admin → servidor web →
   red local → tareas en segundo plano).
2. La primera vez se crea `data/library.mv.db` y se generan las migraciones de Flyway.
3. Se genera un **certificado TLS autofirmado propio de esa máquina**
   en `data/librora-tls.p12` (clave protegida en `data/librora-tls.pass`, permisos 600).
   El certificado cubre `librora.local`, `localhost`, el nombre del equipo y
   todas las IP locales, así que la URL funciona desde el navegador y desde el móvil.
4. Se levanta **HTTPS en el puerto 8443** y **HTTP en el 8080** a la vez.
5. Se abre el navegador en `https://librora.local:8443/login`.

### Credenciales iniciales

| Usuario | Contraseña |
|---|---|
| `admin` | `admin123` |

La contraseña inicial **solo se aplica si el usuario todavía tiene el hash
placeholder del seed**. En cuanto la cambiás, se conserva para siempre: reiniciar
la aplicación **no** la vuelve a `admin123`.

> **Cambiá la contraseña en el primer inicio** (pantalla de Usuarios).

---

## Uso diario

1. Doble clic en el icono / ejecutable.
2. Esperar a la ventana verde **"Servidor activo"**.
3. El navegador se abre solo en `https://librora.local:8443/login`. Si no se abre,
   el botón **"Abrir navegador"** de la ventana hace lo mismo y **"Copiar dirección"**
   deja la URL en el portapapeles.

Para **cerrar** el servidor hay que cerrar la ventana de Librora: al cerrarla se
apagan los listeners, el mDNS y la base de datos de forma ordenada.

### Desde otros dispositivos de la red

Con la aplicación abierta, desde el móvil o desde otro equipo de la misma red:

- `https://librora.local:8443` — requiere que `librora.local` resuelva (ver abajo)
- `https://IP_DEL_EQUIPO:8443` — siempre funciona, sin configurar nada

El certificado es válido también para la IP, así que no aparece el aviso del
navegador al entrar por IP.

---

## Modo superusuario único

Librora está preparado para el escenario **una sola máquina, un solo dueño, sin empleados**.

- Existe un único usuario con todos los permisos: **`admin`**.
- No se siembra ningún usuario `employee`: la aplicación no crea personal que no existe.
- El usuario inicial se puede renombrar con las variables `LIBRORA_ADMIN_USER` /
  `LIBRORA_ADMIN_PASSWORD` **antes del primer arranque** (después ya no tiene efecto,
  porque la contraseña se considera elegida).
- Para cambiar la contraseña en caliente: **Configuración → Usuarios**.

Consecuencias prácticas:

- **No hay que gestionar empleados ni roles**: el admin lo puede hacer todo.
- **El acceso desde otros equipos es opcional**: si el equipo está aislado y solo
  se usa en local, se puede apagar la exposición de red. La app escucha en
  `0.0.0.0` porque es lo que permite el uso desde el móvil; para uso estrictamente
  local basta con no abrir el puerto en el firewall.
- **La seguridad de la red recae en el dueño**: conviene no exponer el 8443 a
  internet (usar VPN si hace falta acceso remoto).

### Copias de seguridad

Todo el estado vive en la carpeta `data/`, junto al ejecutable:

| Archivo | Contenido |
|---|---|
| `data/library.mv.db` | Base de datos H2 (todo el catálogo, stock, clientes, caja) |
| `data/uploads/` | Portadas y adjuntos |
| `data/librora-tls.p12` / `.pass` | Certificado y clave privada de HTTPS |
| `data/librora.log` | Registro de ejecución |

**Para respaldar: cerrar Librora y copiar la carpeta `data/`.** Ese es el backup
completo. Restaurar es copiar `data/` de vuelta en la misma ubicación.

---

## Red: cómo funciona `librora.local`

`librora.local` es un nombre `.local` de mDNS. La resolución depende del sistema:

1. **AppImage en Linux**: el script `AppRun` intenta, en este orden,
   arrancar `avahi-daemon`, poner el hostname en `librora` con
   `sudo avahi-set-host-name librora`, y publicar los servicios
   `_http._tcp` y `_https._tcp`. Sin `sudo` sin contraseña cae a **JmDNS**
   (responder mDNS en Java puro, sin root) y ofrece abrir una terminal para
   ejecutar el setup con `sudo`.
2. **Windows / macOS**: se usa **JmDNS** directamente, sin instalar nada.
3. **Último recurso**: entrada en `/etc/hosts` apuntando a la IP local
   (solo funciona en la propia máquina).

Si `librora.local` no resuelve, la app usa automáticamente la IP local y todo
sigue funcionando; el certificado cubre ambas.

---

## Configuración

Todo se ajusta por variables de entorno. **Ninguna es obligatoria.**

| Variable | Por defecto | Para qué sirve |
|---|---|---|
| `LIBRORA_PORT` | `8080` | Puerto HTTP |
| `LIBRORA_HTTPS_PORT` | `8443` | Puerto HTTPS |
| `LIBRORA_TLS` | `true` | `false` = solo HTTP, sin certificado |
| `LIBRORA_TLS_KEYSTORE` | `./data/librora-tls.p12` | Ruta del keystore |
| `LIBRORA_TLS_PASSWORD` | generado | Contraseña del keystore (evita el `.pass`) |
| `LIBRORA_ADMIN_USER` | `admin` | Usuario inicial (solo primer arranque) |
| `LIBRORA_ADMIN_PASSWORD` | `admin123` | Contraseña inicial (solo primer arranque) |
| `CSRF_HMAC_SECRET` | aleatorio por arranque | Secreto de firma CSRF: fijarlo para no invalidar sesiones al reiniciar |
| `ALLOWED_API_ORIGINS` | vacío | Orígenes extra permitidos en la CSP |

En Linux se pueden fijar en un `.env` y exportar antes de lanzar, por ejemplo:

```bash
export CSRF_HMAC_SECRET="un-secreto-largo-y-aleatorio"
export LIBRORA_ADMIN_PASSWORD="mi-contrasena-robusta"
./Librora-0.1.0-beta-x86_64.AppImage
```

---

## Diagnóstico

- **Registro**: todo queda en `data/librora.log`. Es el primer lugar donde mirar.
- **Ventana en blanco o "Error al iniciar"**: el mensaje aparece en la ventana; el
  detalle está en el log. Causa habitual: el puerto 8080 ya ocupado por otra
  instancia de Librora.
- **Los estilos no cargan**: entrar por `https://` (nunca por `http://` si esperabas TLS).
- **Aviso de certificado**: es esperado, ver *Avisos de seguridad* más arriba.
- **Cerrar y reabrir**: si algo queda a medias, cerrar la ventana y relanzar.
  Borrar `data/` reinstala desde cero (**se pierde toda la información**).

---

## Compilar desde el código

Requiere JDK 25 y Maven 3.9+.

```bash
# JAR ejecutable
mvn install -DskipTests
java -jar bootstrap/target/bootstrap-0.1.0-beta.jar

# AppImage de Linux (además de los anteriores: mksquashfs, python3
# y el stub ELF del runtime de AppImage)
./build-appimage.sh
```

### Releases multiplataforma

`.github/workflows/build-installers.yml` genera los instaladores de Windows y
macOS en cada `push` de tag `v*` (y manualmente con *workflow_dispatch*):

```bash
git tag v0.1.0-beta
git push --tags
```

Los artefactos (`.exe`, `.dmg` arm64 y x86_64) quedan disponibles para descargar
desde la pestaña **Actions** del repo. En Windows usa `jpackage` con WiX 3; en
macOS genera un `.app` que se empaqueta en `.dmg` y se firma ad-hoc para que
Gatekeeper permita aprobarlo una vez.

---

## Architecture

### Module Structure (Maven Multi-module)

```
library-ultimate/
├── bootstrap/                 # Application entry point, Javalin config, Flyway
├── kernel/                    # Shared kernel: web, security, loan policies, code generation
├── iam/                       # Identity & Access Management (users, roles, permissions)
├── books/                     # Catalog domain: works, editions, authors, publishers
├── stock/                     # Physical inventory: stock items, locations, movements
├── client/                    # Client management: clients, types, loan policies
├── reservation/               # Reservations: create, fulfill, return, renew, cancel
├── accounting/                # Financial: payments, expenses, payroll, refunds
├── config/                    # System settings, configuration UI
├── catalog/                   # Public catalog view, guest reservations
└── security/                  # Security audit, encryption, CSRF, rate limiting
```

### Technology Stack

| Layer | Technology |
|-------|------------|
| Language | Java 25 |
| Build | Maven 3.9+ |
| Web Framework | Javalin 7.2 |
| ORM | Hibernate 6.6 |
| Database | H2 (file-based, PostgreSQL compatible mode) |
| Migrations | Flyway 11.7 |
| Templates | Pebble 3.2 |
| Frontend | Bootstrap 5.3, HTMX 2.0, jQuery 3.7 |
| Security | BCrypt, HMAC CSRF, Rate Limiting, CSP |

---

## Domain Model

### Core Entities

#### Work
Represents a literary work (book title). Contains metadata: title, description, categories, languages.

#### Edition
A specific publication of a Work. Contains: ISBN, publisher, format, language, publication year, pages, **dailyPrice**, edition number.

#### StockItem
Physical copy of an Edition. States:
- `AVAILABLE` - On shelf, can be reserved
- `RESERVED` - Reserved, awaiting pickup
- `BORROWED` - Checked out to client
- `REVIEW` - Returned, under inspection
- `DAMAGED` - Damaged, needs repair
- `RETIRED` - Removed from circulation

#### Client
Library member. Types:
- **CASUAL**: 7 days loan, max 3 active reservations, 1 renewal
- **MEMBER**: 21 days loan, max 5 active reservations, 3 renewals

Fields: DNI (unique), fullName, email, phone, address, type, status, memberUntil, membershipPaid, birthDate, notes.

#### Reservation
Links Client + StockItem + Edition. States:
- `DEPOSIT_PENDING` - Created, deposit not paid
- `ACTIVE` - Deposit paid, awaiting pickup
- `OVERDUE` - Past due date
- `RETURNED` - Completed
- `CANCELLED` - Cancelled

Financial: `dailyPrice`, `loanDays`, `totalAmount = dailyPrice × loanDays`, `depositAmount = 50% of totalAmount`.

---

## Reservation Workflow

```
AVAILABLE → RESERVED → BORROWED → AVAILABLE
    │           │           │
    │           │           └── Return (stock state → AVAILABLE/REVIEW)
    │           │
    │           └── Fulfill (pickup) → Payment of deposit
    │
    └── Create Reservation (client + stock item + loan days)
         → Calculates total = dailyPrice × loanDays
         → Deposit = 50% of total
         → Sets pickup deadline (configurable hours)
         → Sets due date = today + loanDays
```

### Client Auto-creation
If a client doesn't exist when reserving (catalog or admin), they're auto-created with:
- Type: CASUAL
- Status: ACTIVE
- Empty optional fields (fullName, email, phone, address)
- `clientDataIncomplete` flag set → modal prompts for completion on next visit

---

## Membership System

### Upgrade CASUAL → MEMBER
- Requires payment confirmation
- Sets `memberUntil` = now + selected months
- `membershipPaid` = true
- Benefits apply immediately

### Expiry
- Background scheduler runs every 60 minutes
- Finds MEMBER clients with `membershipPaid = false` AND `memberUntil < today`
- Demotes to CASUAL, clears `memberUntil`

---

## Configuration (Admin)

Accessible at `/config` (requires `config.read` permission).

### Settings Categories

| Category | Key | Description |
|----------|-----|-------------|
| Loan | `loan.defaultLoanDays` | Default loan period (days) |
| Loan | `loan.maxRenewalsCasual` | Max renewals for CASUAL |
| Loan | `loan.maxRenewalsMember` | Max renewals for MEMBER |
| Loan | `loan.pickupDeadlineHours` | Hours to pick up after reservation |
| Loan | `loan.lateFeePerDay` | Late fee per day |
| Loan | `loan.depositPercentage` | Deposit % of total |
| Reservation | `reservation.maxActiveCasual` | Max active reservations CASUAL (3) |
| Reservation | `reservation.maxActiveMember` | Max active reservations MEMBER (5) |
| Membership | `membership.defaultMonths` | Default membership duration |
| Membership | `membership.pricePerMonth` | Monthly membership fee |
| Catalog | `catalog.currency` | Currency code (EUR, USD, etc.) |
| Catalog | `catalog.currencySymbol` | Currency symbol (€, $, etc.) |
| Catalog | `catalog.itemsPerPage` | Pagination size |

Settings stored in `settings` table, editable via UI with proper input types (selects for booleans/enums, numbers for amounts).

---

## Public Catalog (`/catalog`)

- Lists editions with available stock > 0
- Shows: title, edition number, publisher, available count
- "Reserve" button → `/catalog/reserve/{editionId}`

### Guest Reservation Flow
1. Enter DNI (validated: 8 digits + letter, e.g., `12345678A`)
2. Enter loan days (1-30)
3. Submit → Creates/finds client, creates reservation
4. Success page with reservation code

**Validation**: Real-time client-side (JS) + server-side (ValidationException → flash message)

---

## Admin Interface

### Navigation Sections
- **Dashboard** (`/`) - Overview stats
- **Catalog** - Works, Editions, Authors, Publishers, Categories
- **Stock** - Items, Locations, Movements
- **Clients** - List, Create, Edit, Upgrade to Member
- **Reservations** - List, Create, Fulfill, Return, Renew, Cancel
- **Accounting** - Payments, Expenses, Payroll, Refunds, Payment Methods
- **Config** - System settings
- **Admin** - Users, Roles, Permissions, Modules, Audit Log

### Reservations List (`/reservations`)
Shows: Code, Client DNI, Edition (Work + Edition #), Status, Dates, Amounts

### Create Reservation (`/reservations/new`)
- Client dropdown (ACTIVE clients)
- Stock item dropdown (AVAILABLE items)
- Loan days input
- Notes optional
- **Validation**: Reservation limit enforced per client type

### Fulfill Reservation Wizard (`/reservations/{id}/fulfill`)
1. **Client Step** - Auto-skipped if client has complete data
2. **Payment Step** - Select payment method, confirm deposit
3. **Confirmation** - Complete fulfillment

### Return Reservation (`/reservations/{id}/return`)
- Preview shows: overdue days, late fee, deposit refund
- Condition selection: GOOD / WORN / DAMAGED
- Updates stock state accordingly

---

## Accounting

### Payment Methods (`/accounting/payment-methods`)
CRUD with auto-generated codes (e.g., `PMT-A1B2C3`)

### Record Payment (`/accounting/payments/new`)
- Client selection
- Amount, date, payment method
- Optional reservation linkage

### Expenses, Payroll, Refunds
Full CRUD with audit trails

---

## Security

### Authentication
- Session-based (Javalin sessions)
- BCrypt password hashing
- Default users: `admin` / `employee` (passwords reset on startup)

### Authorization
- Role-based permissions (Module → Permission → Role → User)
- `@RequirePermission` equivalent via `requireCan(ctx, "permission.code")`

### CSRF Protection
- Double-submit cookie pattern
- Token in form hidden field + cookie
- Validated on all POST/PUT/DELETE

### Rate Limiting
- Token bucket per IP
- Configurable limits (default: 100 req/min)

### CSP (Content Security Policy)
- Nonce-based for inline scripts/styles
- Meta tag: `<meta name="csp-nonce" content="{{ securityNonce }}">`
- `initHtmxCsp()` adds nonce to HTMX-processed elements

### Audit Logging
- All security events logged: LOGIN_SUCCESS, LOGIN_FAILED, UNAUTHORIZED_ACCESS, etc.
- Viewable at `/admin/audit`

---

## Notifications (SSE)

- Endpoint: `/api/notifications/stream`
- Requires authenticated session (`withCredentials: true`)
- Event types: `reservation.created`, `reservation.overdue`, `membership.expiring`, etc.
- Auto-reconnect with 5s delay
- Toast notifications via `notificationToast` container

---

## Development

### Prerequisites
- Java 25+
- Maven 3.9+

### Build
```bash
mvn -DskipTests clean package -T 4
```

### Run
```bash
java -jar bootstrap/target/bootstrap-1.0.0.jar
```

- Starts on `http://localhost:8080`
- H2 database at `./data/library.mv.db`
- Flyway migrations apply automatically

### Database Console
H2 console available at `/h2-console` (if enabled in config)
- JDBC URL: `jdbc:h2:file:./data/library;MODE=PostgreSQL`
- User: `sa`
- Password: (empty)

---

## API Endpoints Summary

### Public
| Method | Path | Description |
|--------|------|-------------|
| GET | `/catalog` | Public catalog view |
| GET | `/catalog/reserve/{editionId}` | Guest reservation form |
| POST | `/catalog/reserve/{editionId}` | Create guest reservation |
| GET | `/catalog/reservation/success` | Success page |

### Authenticated (require login)
| Method | Path | Description |
|--------|------|-------------|
| GET | `/reservations` | List reservations |
| GET | `/reservations/new` | Create reservation form |
| POST | `/reservations` | Create reservation |
| GET | `/reservations/{id}` | View reservation |
| POST | `/reservations/{id}/fulfill` | Fulfill (pickup) |
| POST | `/reservations/{id}/return` | Return item |
| POST | `/reservations/{id}/renew` | Renew loan |
| POST | `/reservations/{id}/cancel` | Cancel reservation |

### Admin
| Method | Path | Description |
|--------|------|-------------|
| GET | `/config` | Settings UI |
| POST | `/config` | Save settings |
| GET | `/admin/*` | User/role/permission management |
| GET | `/api/notifications/stream` | SSE notifications |

---

## Deployment Notes

### Production Checklist
- [ ] Set `CSRF_HMAC_SECRET` environment variable
- [ ] Set `https=true` for secure cookies
- [ ] Configure proper database (PostgreSQL recommended)
- [ ] Set `spring.profiles.active=prod` if using Spring
- [ ] Configure reverse proxy (nginx) with TLS
- [ ] Set up log aggregation
- [ ] Configure backup strategy for H2/PostgreSQL

### Docker (example)
```dockerfile
FROM eclipse-temurin:25-jre
COPY bootstrap/target/bootstrap-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## Deployment Architecture

### Server-Client Model (Closed Local Network)

Librora is designed as a **centralized server application** for closed environments (single physical location such as a library, school, or office).

**Architecture:**
```
┌──────────────────────────────────────────────┐
│         PC Principal del Local               │
│  ┌────────────────────────────────────────┐  │
│  │         Librora Server                 │  │
│  │  (Javalin + H2 Database + Flyway)      │  │
│  │  Listening on 0.0.0.0:8080            │  │
│  │  H2 DB at ./data/library.mv.db         │  │
│  └────────────────────────────────────────┘  │
│           ▲ Port 8080                        │
└───────────┼──────────────────────────────────┘
            │
     ┌──────┼──────┬──────────┐
     │      │      │          │
     ▼      ▼      ▼          ▼
 ┌──────┐ ┌──────┐ ┌──────┐ ┌──────┐
 │PC-   │ │PC-   │ │Tablet│ │Phone │
 │Client│ │Client│ │(Web) │ │(Web) │
 └──────┘ └──────┘ └──────┘ └──────┘
  Client1  Client2  Client3  Client4
```

### How It Works

1. **Server**: Librora runs on the **main PC** of the local (the "server"). It hosts:
   - The web application (Javalin server on port `8080`)
   - The H2 database (`./data/library.mv.db`)
   - All business logic, authentication, and authorization

2. **Clients**: Any device (PC, tablet, phone) on the **same local network** can access Librora by navigating to:
```
http://librora.local:8080
```
This uses mDNS (multicast DNS) which automatically resolves to the server's current IP address. **Linux** sets the hostname to `librora` via avahi (no installation needed). **macOS/Windows** use JmDNS 3.6.3.

    If `librora.local` doesn't work on a particular device (older Windows without Bonjour, etc.), use the server's IP address directly:
    ```
    http://<SERVER_IP>:8080
    Example: http://192.168.1.100:8080
    ```
    Find the IP on the server PC with `hostname -I` (Linux/macOS) or `ipconfig | findstr "IPv4"` (Windows).

3. **Database**: The H2 file database lives **only on the server PC**. Clients access data through HTTP API calls, not directly to the database.

3. **Database**: The H2 file database lives **only on the server PC**. Clients access data through HTTP API calls, not directly to the database.

### Network Requirements

| Requirement | Details |
|-------------|---------|
| **Network** | Local area network (LAN) — Wi-Fi or Ethernet |
| **Server Port** | TCP port `8080` (configurable in `AppConfig.PORT`) |
| **Server Access** | `http://librora.local:8080` (mDNS) or `http://<SERVER_IP>:8080` |
| **Client Access** | Any device with a web browser on the same network |
| **Firewall** | Port `8080` must be open on server PC firewall (inbound) |
| **Internet** | **NOT required** — works fully offline in closed network |
| **mDNS** | Used for automatic name resolution (`librora.local`) |
| **Java on Clients** | **NOT required** — clients only need a web browser |

### Firewall Configuration (Server PC)

**Windows:**
```powershell
New-NetFirewallRule -DisplayName "Librora" -Direction Inbound -LocalPort 8080 -Protocol TCP -Action Allow
```

**Linux:**
```bash
sudo ufw allow 8080/tcp
# or
sudo iptables -A INPUT -p tcp --dport 8080 -j ACCEPT
```

**Router (optional):** If clients are on a different subnet, ensure port `8080` is routed to the server PC's IP.

### Finding the Server IP

**Recommended method — mDNS:**

From any device on the same network, simply open a browser and go to:
```
http://librora.local:8080
```
The name `librora.local` automatically resolves to the current IP address of the server PC. This works regardless of IP changes.

**Alternative — direct IP:**

**On the server PC:**
```bash
# Linux/macOS
hostname -I
# or
ip addr show | grep "inet "

# Windows
ipconfig | findstr "IPv4"
```

### Accessing from the Host PC

On **Linux**, Librora runs `sudo avahi-set-host-name librora` on startup (you'll be prompted for your password). This makes `librora.local` resolve via mDNS on all devices automatically. If sudo is not available, it falls back to `/etc/hosts` entry. On **macOS** and **Windows**, it uses JmDNS 3.6.3 directly (no installation needed).

mDNS works on **all platforms without installing anything** — Librora registers itself on the network via JmDNS (pure Java) and `librora.local` resolves automatically on Windows, Linux, macOS, Android, and iOS via multicast DNS.

**From a client PC:**
```bash
ping <server-name>
ping <server-ip>
```

### Startup Behavior

When Librora starts on the server PC:
1. H2 database initializes at `./data/library.mv.db`
2. Flyway migrations apply automatically
3. Javalin server starts on `0.0.0.0:8080` (all network interfaces)
4. Librora announces itself on the local network via mDNS (`librora.local`)
5. Default browser opens on the server (if supported)
6. Clients can now connect via `http://librora.local:8080` or `http://<server-ip>:8080`

---

## Roadmap

### Future: Native Desktop Wrapper (Tauri)

Librora's current architecture (Java backend + web frontend) is ideal for wrapping in a native desktop shell using **Tauri** (Rust + Cargo):

| Aspect | Current (Beta) | Future (Tauri) |
|--------|-----------------|-----------------|
| Backend | Java 25 + Javalin | Same (unchanged) |
| Frontend | Browser-based | Embedded WebView2 |
| Packaging | JAR / AppImage | .exe / .msi / .dmg |
| Distribution | Manual server setup | Single installer |
| Client Access | Via IP in browser | Direct app launch |
| Auto-start | Manual | System service |
| Resource Usage | JVM on server | Native shell + JVM |

**Why Tauri:**
- **Lightweight**: Rust binary (~5MB) vs Electron (~150MB)
- **Native**: Uses system WebView2 (Windows), WebKit (macOS), WebKitGTK (Linux)
- **Secure**: Rust memory safety for the shell layer
- **Familiar**: HTML/CSS/JS frontend stays the same

**Architecture with Tauri:**
```
┌──────────────────────────────────────────────┐
│  Tauri App (Rust Shell)                     │
│  ┌──────────────┐  ┌────────────────────┐   │
│  │ WebView2     │──│  Tauri Command     │   │
│  │ (Frontend)   │  │  (spawn JVM,       │   │
│  │              │  │   manage lifecycle) │   │
│  └──────────────┘  └────────┬───────────┘   │
│                             │                │
└─────────────────────────────┼────────────────┘
                              │
                              ▼
                    ┌─────────────────┐
                    │  Java JVM       │
                    │  (Javalin +     │
                    │   Hibernate)    │
                    │  Port 8080      │
                    └─────────────────┘
```

**Build with GitHub Actions:**
- Auto-build on push to `main`
- Matrix builds for Windows (x86_64, ARM64), macOS (Intel, Apple Silicon), Linux (x86_64, ARM64)
- Artifacts: `.exe`, `.msi`, `.dmg`, `.AppImage`
- Auto-release on version tag

---

## License

Internal project - Librora v0.1-beta
