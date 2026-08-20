# Library Ultimate — Architecture & Use Cases

This document describes the modular architecture of the Library management
system, the **use cases** of the IAM feature, and the **data flow** between
classes at runtime. It is written at the code level so a developer can trace a
request end-to-end.

---

## 1. Project structure (Maven multi-module)

```
library-ultimate (parent POM — aggregator)
├── iam                       (feature aggregator POM)
│   ├── iam-domain            (pure business rules, NO framework)
│   ├── iam-application       (use cases / application services)
│   └── iam-infrastructure    (adapters: JPA, HTTP, security, templates)
└── bootstrap                 (ONLY runnable module — composition root)
    ├── LibraryApplication    (main() — wires everything, starts Javalin)
    ├── db/migration          (Flyway migrations)
    ├── templates/            (Pebble templates)
    └── static/               (CSS/JS)
```

**Dependency rule (hexagonal / clean architecture):**
```
bootstrap → iam-infrastructure → iam-application → iam-domain
                                        │                ▲
                                        └──────ports─────┘
```
Dependencies point **inward** (from infrastructure to domain). The domain
knows nothing about JPA, HTTP, or templates.

---

## 2. Class map by layer

### Domain (`iam-domain`) — zero framework dependencies
| Class | Responsibility |
|---|---|
| `User` | Aggregate root: identity, credentials (hash), enabled flag, role |
| `Role` | Groups a set of permissions; `hasPermission(code)` |
| `Permission` | Granular right (`resource.action`), menu metadata, module |
| `Module` | Top-level functional area (dashboard, iam, ...) |
| `LoadUser` (port-in) | Declares the `loadByUsername` use case |
| `UserPort`/`RolePort`/`PermissionPort`/`ModulePort` (port-out) | Persistence contracts |
| `PasswordHasher` (port-out) | Cryptography contract (hash + verify) |

### Application (`iam-application`) — use cases
| Class | Responsibility |
|---|---|
| `UserQueryService` | Implements `LoadUser`; loads a user by username |
| `NavigationService` | Builds the dynamic sidebar from the user's permissions |
| `PasswordService` | Wraps `PasswordHasher` port |
| `NavItem` / `NavSection` (DTO) | Immutable output DTOs for the template |

### Infrastructure (`iam-infrastructure`) — adapters
| Class | Responsibility |
|---|---|
| `UserPersistenceAdapter` etc. | Implement the output ports |
| `HibernateUserRepository` etc. | Thin Hibernate `Session` wrappers |
| `UserEntity` etc. | JPA entities (persistence model) |
| `BouncyCastleArgon2PasswordHasher` | Argon2id implementation |
| `LoginController` | Handles login form (GET + POST) |
| `DashboardController` | Renders the dashboard with dynamic menu |
| `SessionAuthFilter` | Session-based auth guard |
| `WebRoutes` | Registers routes + filter on Javalin |

---

## 3. Use-Case diagram

```mermaid
flowchart LR
    ActorUser[User / Employee] -->|submits credentials| UC1[Login]
    ActorUser -->|navigates| UC2[View Dashboard]
    Admin[Administrator] --> UC3[Manage roles]
    Admin --> UC4[List / edit / delete roles]
    ActorUser[User] --> UC5[Receive real-time notifications]

    UC1 --> S1[UserQueryService.loadByUsername]
    UC1 --> S2[PasswordService.verify]
    UC2 --> S3[NavigationService.buildNavigation]
    UC3 --> S4[RoleAdminService.createRole]
    UC4 --> S5[RoleAdminService.listRoles / updateRole / deleteRole]
    UC5 --> S6[SseNotificationService.publish]

    S1 --> P1[UserPort.findByUsername]
    S2 --> P2[PasswordHasher.verify]
    S3 --> M1[User.role.permissions]
    S4 & S5 --> P3[RolePort.save / delete]
    S4 & S5 --> P4[PermissionPort.findById]
    S6 --> P5[NotificationService.publish]

    P1 --> A1[UserPersistenceAdapter]
    A1 --> R1[HibernateUserRepository]
    P2 --> A2[BouncyCastleArgon2PasswordHasher]
    P3 --> A3[RolePersistenceAdapter]
    P4 --> A4[PermissionPersistenceAdapter]
```

---

## 4. Sequence: Login (authentication flow)

```mermaid
sequenceDiagram
    participant B as Browser
    participant L as LoginController
    participant UQ as UserQueryService
    participant UP as UserPort
    participant HA as Adapter (JPA)
    participant HR as HibernateUserRepository
    participant DB as H2 Database
    participant PS as PasswordService
    participant BC as Argon2 Hasher

    B->>L: POST /login (username, password)
    L->>UQ: loadByUsername(username)
    UQ->>UP: findByUsername(username)
    UP->>HA: findByUsername(username)
    HA->>HR: query session
    HR->>DB: SELECT users JOIN role/permissions
    DB-->>HR: row(s)
    HR-->>HA: UserEntity
    HA-->>UP: User (domain)
    UP-->>UQ: Optional<User>
    UQ-->>L: User
    L->>PS: verify(password, user.passwordHash)
    PS->>BC: verify(password, hash)
    BC-->>PS: boolean
    PS-->>L: boolean
    alt credentials valid
        L->>B: set session user, redirect /
    else invalid
        L->>B: redirect /login?error
    end
```

---

## 5. Sequence: Dashboard (dynamic navigation)

```mermaid
sequenceDiagram
    participant B as Browser
    participant F as SessionAuthFilter
    participant D as DashboardController
    participant NS as NavigationService
    participant U as User (domain)

    B->>F: GET /
    F->>F: session has user? (yes)
    F->>D: showDashboard(ctx)
    D->>D: user = ctx.sessionAttribute("user")
    D->>NS: buildNavigation(user)
    NS->>U: role.getPermissions()
    U-->>NS: set<Permission>
    NS-->>D: List<NavSection>
    D-->>B: render dashboard.peb (user, navSections)
```

---

## 5b. Sequence: Manage role (create) + real-time notification

```mermaid
sequenceDiagram
    participant B as Browser
    participant F as SessionAuthFilter
    participant RC as RoleController
    participant RAS as RoleAdminService
    participant RP as RolePort
    participant PP as PermissionPort
    participant A3 as RolePersistenceAdapter
    participant A4 as PermissionPersistenceAdapter
    participant DB as H2 Database
    participant NS as SseNotificationService

    B->>F: POST /iam/roles (name, description, permissionIds[])
    F->>RC: createRole(ctx) [requires roles.manage]
    RC->>RAS: createRole(name, description, permissionIds)
    RAS->>PP: findById(id) for each permission
    PP->>A4: findById(id)
    A4->>DB: SELECT permissions
    DB-->>A4: Permission row
    A4-->>PP: Permission (domain)
    PP-->>RAS: Permission
    RAS->>RP: save(role)
    RP->>A3: save(role)
    A3->>DB: INSERT roles + role_permissions
    DB-->>A3: saved
    A3-->>RP: Role
    RP-->>RAS: Role
    RAS->>NS: publish(role.created)
    NS->>B: SSE event (message)
    RAS-->>RC: Role
    RC-->>B: 302 redirect /iam/roles
```

## 5c. Data flow: role management (persistence)

```mermaid
flowchart LR
    subgraph Application
        RAS[RoleAdminService]
    end
    subgraph Infrastructure
        A3[RolePersistenceAdapter]
        A4[PermissionPersistenceAdapter]
        R3[HibernateRoleRepository]
        R4[HibernatePermissionRepository]
        M3[RoleMapper]
        E3[(H2 roles table)]
        E4[(H2 permissions table)]
    end
    subgraph Domain
        R[Role]
        P[Permission]
    end

    RAS -->|RolePort| A3
    RAS -->|PermissionPort| A4
    A3 --> R3
    A4 --> R4
    R3 --> E3
    R4 --> E4
    E3 --> R3
    R3 --> A3
    A3 -->|RoleMapper.toDomain| R
    R --> RAS
    E4 --> A4
    A4 -->|PermissionMapper.toDomain| P
    P --> RAS
```

---

## 5d. Sequence: subscribe to real-time notifications (SSE)

```mermaid
sequenceDiagram
    participant B as Browser
    participant WR as WebRoutes (SseHandler)
    participant F as SessionAuthFilter
    participant NS as SseNotificationService
    participant RAS as RoleAdminService

    B->>F: GET /api/notifications/stream [EventSource]
    F->>F: authenticated + has notifications.stream? (yes)
    F->>WR: connect client (SseHandler consumer)
    WR->>NS: addClient(client) → register + onClose
    Note over B,NS: connection stays open (long-lived)
    RAS->>NS: publish(event) e.g. role.updated
    NS->>NS: broadcast JSON to all clients
    NS-->>B: SSE "message" event
    B->>B: show toast in the Browser
```

---

## 6. Data flow diagram (persistence)

```mermaid
flowchart LR
    subgraph Application
        S[UserQueryService]
    end
    subgraph Infrastructure
        A[UserPersistenceAdapter]
        R[HibernateUserRepository]
        M[UserMapper]
        E[(H2 users table)]
    end
    subgraph Domain
        D[User]
    end

    S -->|UserPort| A
    A -->|findByUsername| R
    R -->|HQL/JPA| E
    E -->|UserEntity| R
    R -->|UserEntity| A
    A -->|UserMapper.toDomain| D
    D -->|User (domain)| S
```

---

## 7. Security flows

- **Password storage:** Argon2id via BouncyCastle (`BouncyCastleArgon2PasswordHasher`).
  Stored as `$argon2id$v=19$m=19456,t=2,p=1$<salt>$<hash>` with a random 16-byte
  salt per hash. Constant-time comparison on verify.
- **Authentication:** server-side HTTP session stores the `User`. No tokens in
  cookies. `SessionAuthFilter` rejects unauthenticated requests to protected paths.
- **Authorization:** RBAC. The sidebar is built dynamically from the user's
  role permissions (least privilege by construction).
- **Post-quantum note:** the project uses BouncyCastle and is structured so the
  TLS/transport layer can be upgraded to hybrid post-quantum key exchange
  (e.g. X25519Kyber768) without touching the domain.

---

## 8. Startup flow (`LibraryApplication`)

```mermaid
flowchart TD
    A[main] --> B[Build Hibernate SessionFactory]
    B --> C[Run Flyway migrations]
    C --> D[Build persistence adapters]
    D --> E[Seed admin password hash]
    E --> F[Build services + controllers]
    F --> G[Register Javalin routes]
    G --> H[Start Javalin on 0.0.0.0:8080]
    H --> I[Open browser at /login]
