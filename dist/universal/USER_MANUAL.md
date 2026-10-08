# Librora — User Manual

**Version:** 0.1.0-beta
**Last updated:** September 2026

---

## Table of Contents

1. [Overview](#overview)
2. [Quick Start](#quick-start)
3. [Accessing the Application](#accessing-the-application)
4. [How to Connect from Any Device](#how-to-connect-from-any-device)
5. [Mobile Access Guide](#mobile-access-guide)
6. [Default Credentials](#default-credentials)
7. [Application Architecture](#application-architecture)
8. [Feature Flows](#feature-flows)
   - [Catalog](#1-catalog)
   - [Reservations](#2-reservations)
   - [Stock Management](#3-stock-management)
   - [Clients](#4-clients)
   - [Accounting](#5-accounting)
   - [User Management & Roles](#6-user-management--roles)
   - [Configuration](#7-configuration)
9. [Data & Storage](#data--storage)
10. [Troubleshooting](#troubleshooting)

---

## Overview

**Librora** is a comprehensive library management system. It handles books, authors, publishers, editions, and formats — plus reservations, stock tracking, client management, accounting, and user administration — all in a single web application.

The app runs locally on your machine (or server) and is accessed through a web browser. There is no external hosting or cloud dependency required.

---

## Quick Start

### Prerequisites

- Java 25 or later (included in the AppImage — no separate install needed)
- A modern web browser (Chrome, Firefox, Edge, Safari)

### Running the AppImage (Linux)

```bash
# Make it executable (one time)
chmod +x Librora-0.1.0-beta-x86_64.AppImage

# Run it
./Librora-0.1.0-beta-x86_64.AppImage
```

The application starts automatically and opens your default browser to the dashboard.

### First Run

On first launch:
1. A local H2 database is created at `./data/library.mv.db`
2. Flyway migrations run automatically to create all tables and constraints
3. A self-signed TLS certificate is generated for **this machine** (`data/librora-tls.p12`)
4. The initial `admin` password is set (see [Default Credentials](#default-credentials))
5. The server starts on **HTTPS 8443** and **HTTP 8080** at the same time
6. The app announces itself on the local network so other devices can find it automatically
7. Your browser opens `https://librora.local:8443/login`

> **First-time certificate warning:** the browser will say the connection is not
> private. Click **Advanced → Continue to librora.local**. The browser remembers
> the decision, so it is asked only once. The same warning does not appear when
> you connect through the machine's IP address, because the certificate covers
> both.

### Subsequent Runs

On subsequent launches:
1. The database is detected → migrations are skipped automatically
2. The app connects to the existing database seamlessly
3. The app re-advertises itself on the local network

---

## Accessing the Application

### On the Same Machine

After starting, look at the console output. The app logs the URL it's running at:

```
Application started at https://192.168.1.42:8443/
Access from any device on the same network: https://librora.local:8443
```

You can open `https://librora.local:8443/` or `https://librora.local:8443/` in your browser.

### From Any Device on the Same Network

No need to know the IP address! The app uses **mDNS** (multicast DNS) to announce itself on your local Wi-Fi or Ethernet network. Any device that supports mDNS can find it automatically.

**Simply type this URL in any browser on the same network:**

```
https://librora.local:8443
```

That's it. No IP address needed.

### What is mDNS?

mDNS (multicast DNS) is a technology that lets devices on a local network find each other by name instead of IP address — just like how `localhost` always points to your own machine. `librora.local` always points to whichever device is running Librora, regardless of what IP address that device has.

---

## How to Connect from Any Device

### If `librora.local` Works (Most Devices)

Open a browser and type:

```
https://librora.local:8443
```

This works on:
- ✅ Windows 10/11 (with Bonjour Print Services or Apple Bonjour installed — comes free with iTunes or iCloud)
- ✅ macOS (built-in support)
- ✅ Linux (with Avahi, usually pre-installed)
- ✅ Android (most modern phones)
- ⚠️ iOS (may need to be on the same network and WiFi)

### If `librora.local` Doesn't Work

Some older devices or network configurations don't support mDNS. In that case, use the IP address instead:

**On the server PC (where Librora is running), find the IP:**

```bash
# Linux
hostname -I
# or
ip addr show | grep "inet "

# macOS
ifconfig | grep "inet "

# Windows
ipconfig | findstr "IPv4"
```

**Then on the other device, type:**

```
https://<IP_ADDRESS>:8443
```

Example: `https://192.168.1.42:8443`

### Important About IP Changes

The computer's IP address can change when:
- The router restarts
- The device reconnects to Wi-Fi
- The DHCP lease expires

**This is why mDNS is the recommended method** — `librora.local` always works regardless of IP changes.

---

## Mobile Access Guide

### Accessing from a Phone or Tablet

1. **Start the app** on the host computer (the one running Librora)
2. **Make sure your phone and the host computer are on the same Wi-Fi network**
3. **Open your phone's browser** (Chrome, Safari, Firefox, etc.)
4. **Type this address:**
   ```
   https://librora.local:8443
   ```
5. **Log in** with credentials (see [Default Credentials](#default-credentials))

### If It Doesn't Load on Your Phone

Try these alternatives:

1. **Check that both devices are on the same Wi-Fi** (not one on Wi-Fi and one on mobile data)
2. **Try the IP address instead:**
   - Find the IP on the host computer (see [How to Connect from Any Device](#if-librora-local-doesnt-work))
   - Type `https://<IP>:8443` in your phone's browser
3. **Restart the app** — sometimes the mDNS announcement takes a moment
4. **Restart your phone's Wi-Fi** — sometimes the phone needs to refresh its network cache

### Browsing the Catalog from Mobile

The public catalog can be browsed without logging in:
- Visit `https://librora.local:8443/catalog` directly from your phone's browser
- This shows all available books with their availability status
- Perfect for library visitors who want to check what's available before visiting

### Making it Easy to Access Again

Once you find the correct URL, **bookmark it** in your phone's browser for instant access next time.

---

## Default Credentials

Librora runs in **single-superuser mode**: one machine, one owner, no employees.

| Role | Username | Password |
|------|----------|----------|
| Owner (full access) | `admin` | `admin123` |

There are no employee accounts. The `employee` row that ships in the database
has no usable password and **cannot log in**.

**Change the password on first login** (Configuration panel). Once you do, it is
kept: restarting Librora will **not** put `admin123` back.

If you want a different initial username or password, export
`LIBRORA_ADMIN_USER` / `LIBRORA_ADMIN_PASSWORD` before the very first launch.

### Who Can Do What?

| Action | Owner (`admin`) | Guest |
|--------|-----------------|-------|
| Browse catalog | ✅ | ✅ |
| Reserve books | ✅ | ✅ (with DNI) |
| Manage stock | ✅ | ❌ |
| Manage clients | ✅ | ❌ |
| Accounting | ✅ | ❌ |
| User management | ✅ | ❌ |
| Configuration | ✅ | ❌ |

---

## Application Architecture

Librora is a **modular monolith** built with Java, Javalin (web framework), Hibernate ORM, Pebble (templating), and H2 (embedded database).

### Modules

| Module | Purpose |
|--------|---------|
| **Books** | Works, editions, authors, publishers, formats, categories, audit history |
| **Catalog** | Public catalog browsing, search, availability |
| **Reservations** | Borrowing, returning, renewals, guest reservations, payments |
| **Stock** | Inventory items, locations, movements, stock tracking |
| **Clients** | Client management, memberships, fees |
| **Accounting** | Expenses, payments, payroll, balance, financial reports |
| **IAM** | Users, roles, permissions, login, sessions, dashboard |
| **Config** | System settings, module-level configuration |

### Data Flow

```
Browser → Javalin Web Server (HTTPS 8443 / HTTP 8080) → Controller → Service/UseCase → Hibernate → H2 Database
```

All data is stored locally in `./data/library.mv.db` (H2 in embedded mode). No external database server is required.

---

## Feature Flows

### 1. Catalog

**Public access** — no login required for browsing.

#### Browse the Catalog
1. Navigate to `/catalog` from the top navigation or directly in browser
2. View all books with their availability status
3. Click on any book to see details: editions, authors, and available copies
4. Use search and filters to find specific works

#### Catalog for Mobile Users
- Access `https://librora.local:8443/catalog` directly from any mobile browser
- The catalog page is responsive and works on phones and tablets
- No login required — perfect for guests wanting to browse the collection

### 2. Reservations

**Requires login** (employee or admin).

#### Reserve a Book
1. Browse the catalog and find a book with available copies
2. Click **Reserve** on the desired edition
3. Select the client (or guest) who will borrow the book
4. Set the reservation dates (start date, due date)
5. Confirm the reservation — a payment record may be created depending on configuration

#### Manage Reservations
1. Navigate to the Reservations section from the sidebar
2. View all active, overdue, and completed reservations
3. Filter by status, client, or date range
4. Renew or return books directly from the reservation list
5. Track payments associated with each reservation

#### Reservation Flow Diagram
```
Browse Catalog → Select Book → Choose Client → Set Dates → Confirm
       ↓
  Payment (if applicable)
       ↓
  Active Reservation
       ↓
  ┌─────────────────────┐
  │ Due Date Arrives    │
  └────────┬────────────┘
           ↓
    ┌──────┴──────┐
    ↓             ↓
  Renew        Return
    ↓             ↓
  New Due     Completed
  Date        (+ payment if overdue)
```

### 3. Stock Management

**Requires admin or employee with stock permissions.**

#### Add Stock Items
1. Navigate to Stock → Items
2. Click **Create Stock Item**
3. Select the edition and quantity
4. Assign a storage location
5. Save — the item is now trackable in inventory

#### Track Movements
1. Navigate to Stock → Movements
2. View all stock movements (additions, removals, transfers)
3. Filter by date, location, or item
4. Stock movement history is maintained for audit purposes

#### Locations
1. Navigate to Stock → Locations
2. Create and manage physical storage locations (shelves, rooms, boxes)
3. Assign locations to stock items

### 4. Clients

**Requires admin or client management permissions.**

#### Add a Client
1. Navigate to Clients → Clients
2. Click **Create Client**
3. Fill in client details: name, contact info, membership type
4. Save — the client can now borrow books and make reservations

#### View Client Details
1. Click any client name in the list
2. See their profile, active reservations, payment history, and membership status
3. Edit client information as needed

### 5. Accounting

**Requires admin permissions.**

#### Record Expenses
1. Navigate to Accounting → Expenses
2. Click **Create Expense**
3. Fill in: category, amount, date, payment method, description
4. Save — the expense is recorded and reflected in the balance

#### Process Payments
1. Navigate to Accounting → Payments
2. Click **Create Payment**
3. Select client, amount, payment method, and reservation (if applicable)
4. Save — payment is recorded and linked to the client and reservation

#### View Balance
1. Navigate to Accounting → Balance
2. Select a date range
3. View the financial summary: income, expenses, and net balance

#### Accounting Flow Diagram
```
Expense ──→ ┌──────────┐
             │ Balance   │ ←── Payroll
Payment ──→ │ Sheet     │ ←── Income
             └──────────┘
                 ↓
           Financial
            Reports
```

### 6. User Management & Roles

**Requires admin permissions.**

#### Manage Users
1. Navigate to IAM → Users
2. View all registered users
3. Create new users with username, password, and role assignment
4. Edit user details, toggle status, or reset passwords

#### Manage Roles
1. Navigate to IAM → Roles
2. View all roles and their permissions
3. Create custom roles with specific permission sets
4. Assign permissions to roles (e.g., `catalog.read`, `stock.create`, `accounting.payments.create`)

#### Permission System
Permissions are granular, defined as `module.action`:
- `catalog.read` — View catalog
- `reservation.create` — Make reservations
- `stock.create` — Add stock items
- `accounting.payments.create` — Record payments
- `users.manage` — Manage user accounts
- `config.read` — Access configuration panel

### 7. Configuration

**Requires admin permissions.**

#### System Settings
1. Navigate to Config (gear icon in top bar, admin only)
2. View and edit module-level settings
3. Configure:
   - Client membership fees (yearly/monthly)
   - Currency settings
   - Reservation policies (loan duration, renewal limits)
   - Notification preferences
   - And more...

---

## Data & Storage

### Database Location

All application data is stored in a single file:

```
./data/library.mv.db
```

**Important:** Do not delete or corrupt this file. It contains all your data (books, users, reservations, financial records, etc.).

### Backups

To back up your data, simply copy the `./data/` directory to a safe location.

To restore from a backup:
1. Stop the app
2. Replace the `./data/` directory with your backup copy
3. Start the app again

### File Structure

```
data/
├── library.mv.db        ← H2 database (all app data)
└── uploads/             ← Uploaded images (book covers, documents)
```

---

## Troubleshooting

### "Port 8080 (or 8443) is already in use"

This means another instance of Librora is already running. You have two options:

1. **Use the running instance** — open your browser to `https://librora.local:8443` or the URL shown in the console
2. **Stop the running instance** and restart:
   ```bash
   pkill -f "bootstrap-0.1.0-beta.jar"
   sleep 2
   ./Librora-0.1.0-beta-x86_64.AppImage
   ```

### "Database locked" or "File already in use"

Ensure only one instance of Librora is running. The H2 database does not support concurrent access from multiple processes.

### Browser doesn't open automatically

Some environments (headless servers, VMs) don't support automatic browser launching. Check the console output for the URL and open it manually.

### AppImage won't run

Make sure the AppImage is executable:
```bash
chmod +x Librora-0.1.0-beta-x86_64.AppImage
```

If you get an error about missing FUSE, run with:
```bash
APPIMAGE_EXTRACT_AND_RUN=1 Librora-0.1.0-beta-x86_64.AppImage
```

### Can't access from phone/tablet

1. **Both devices must be on the same Wi-Fi network** — not one on Wi-Fi and one on mobile data
2. **Try `https://librora.local:8443`** first, then try the IP address if that fails
3. **Restart the app** — mDNS announcements can take a few seconds to propagate
4. **On Windows**, Bonjour needs to be installed (comes free with iTunes, iCloud, or [Bonjour Print Services](https://support.apple.com/kb/DL999))
5. **On the server PC**, check that port 8443 (HTTPS) and 8080 (HTTP) are not blocked by the firewall:
   ```bash
   # Linux
   sudo ufw allow 8443/tcp
   # Windows (PowerShell, as Administrator)
   New-NetFirewallRule -DisplayName "Librora" -Direction Inbound -LocalPort 8443 -Protocol TCP -Action Allow
   ```

### mDNS doesn't work on my device

mDNS support varies by device and operating system:

| Device | mDNS Support |
|--------|-------------|
| Windows 10/11 | Needs Bonjour (free from Apple) |
| macOS | Built-in ✅ |
| Linux (Avahi) | Usually built-in ✅ |
| Android 8+ | Usually built-in ✅ |
| iOS | Usually works on same network ⚠️ |

If mDNS doesn't work, just use the IP address as described in [How to Connect from Any Device](#if-librora-local-doesnt-work).

### Reset to defaults

To reset all data and start fresh:
```bash
pkill -f "bootstrap-0.1.0-beta.jar"
rm -rf ./data
./Librora-0.1.0-beta-x86_64.AppImage
```

This will recreate the database, re-run migrations, and re-seed default accounts.

---

## Keyboard Shortcuts

No keyboard shortcuts are currently defined in this version.

---

## Support

For issues, bugs, or questions, check the console output for error messages. The app logs all significant events including startup, errors, and shutdown.
