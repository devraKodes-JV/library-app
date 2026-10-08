# Library Ultimate - Project Agents

## Build
- `mvn install -DskipTests -f pom.xml` — full build (produces `bootstrap-0.1.0-beta.jar`)
- JAR location: `bootstrap/target/bootstrap-0.1.0-beta.jar`

## AppImage Build (Critical Procedure)
The AppImage bundles a **complete** JVM runtime. A runtime that is missing
`lib/runtime/lib/` (modules, `libjvm.so`, …) cannot start Java — this was the
root cause of the broken AppImage. Build it with:

```
./build-appimage.sh
```

What the script does:
1. `mvn install -DskipTests` — builds the shaded JAR.
2. `jpackage --type app-image --app-version 0.1.0-beta` with a **clean** input dir
   containing only the shaded JAR (prevents the launcher `Librora.cfg` from getting
   a duplicate `app.classpath` that points at the dependency-less `original-*.jar`).
3. Wires in the custom `AppRun` (avahi/mDNS setup — see below), `Librora.desktop`
   (`Version=0.1.0-beta`) and the icon.
4. `mksquashfs . new.img -comp xz -b 131072 -noappend` (xz, NOT zstd — the AppImage
   runtime only supports xz/zlib internally).
5. Concatenate the ELF header (first 193728 bytes, the AppImage runtime stub) + the
   new squashfs: `cat appimage-runtime.elf new.squashfs > Librora-0.1.0-beta-x86_64.AppImage`.
6. Ship the result to `dist/linux/` and `dist/universal/`.

Verify after building: `./Librora-0.1.0-beta-x86_64.AppImage --appimage-offset` → `193728`,
and confirm `squashfs-root/lib/runtime/lib/` is populated.

## Key Features
- Port check: `tryBindPort()` in `ServerWindow.java` prevents duplicate instances
- Flyway conditional: skips migrations if `./data/library.mv.db` exists
- mDNS (Linux, via `AppRun`):
  1. `pgrep -x avahi-daemon`; if not running, `sudo -n systemctl start avahi-daemon`
  2. `sudo -n avahi-set-host-name librora` — sets hostname so `librora.local` resolves via mDNS on all devices
  3. `avahi-publish-service -s Librora _http._tcp 8080 path=/` — publishes HTTP service via system daemon
  4. Fallback: `sudo -n ... >> /etc/hosts` adds `IP librora.local` if any sudo/avahi step fails (host-only access)
  - (When run as a plain `java -jar` instead of the AppImage, the Java fallback
    `ServerWindow.startMdnsAvahi()` / `LibraryApplication.ensureHostsEntry()` runs the same sequence.)
- mDNS (macOS/Windows): `startMdnsJmdns()` uses JmDNS 3.6.3 — `librora.local` resolves without installing anything
- Hosts entry: `ensureHostsEntry()` in `LibraryApplication.java` auto-adds `IP librora.local` to `/etc/hosts` on startup (fallback; requires sudo; logs instructions if denied)
- JmDNS 3.6.3 via `jmdns.version` property in root pom.xml
- Configurable Jetty host: `config.jetty.host = "0.0.0.0"` in `JavalinStart.java`

## HTTPS (self-signed, per machine)
`TlsSupport.java` (bootstrap/config) gives the AppImage TLS without any external proxy
or CA. On first run it generates a per-machine self-signed certificate with BouncyCastle
(`bcpkix-jdk18on`) and stores it in `./data/librora-tls.p12` (password in
`./data/librora-tls.pass`, both chmod 600). SANs cover `localhost`, `librora.local`, the
hostname and every local interface IP, so the cert is valid for all access paths.
Browsers show a one-time "unknown issuer" warning — expected for a self-signed beta.

Both listeners run at the same time: **HTTP 8080** and **HTTPS 8443** (`AppConfig.HTTPS_PORT`).
`ServerWindow` opens the HTTPS URL and publishes `_https._tcp` over mDNS so other devices
discover it.

Gotcha: **Javalin only creates its default HTTP connector when no connector was
registered** (`JettyServer.start()` skips it if `server.connectors` is non-empty). So
`TlsSupport.install()` registers the HTTP connector explicitly too — dropping that call
silently removes port 8080.

Environment variables (all optional):
| Variable | Default | Effect |
| --- | --- | --- |
| `LIBRORA_TLS` | `true` | `false` = HTTP only; Javalin then uses its own default connector |
| `LIBRORA_HTTPS_PORT` | `8443` | HTTPS port |
| `LIBRORA_TLS_KEYSTORE` | `./data/librora-tls.p12` | keystore path |
| `LIBRORA_TLS_PASSWORD` | generated | keystore password (avoids the `librora-tls.pass` file) |

If the HTTPS port is already taken, `ServerWindow` calls `TlsSupport.disable()` and the app
starts HTTP-only rather than failing. The CSP in `SecurityHeadersFilter` adds
`upgrade-insecure-requests` + HSTS only on real HTTPS requests, so plain HTTP on 8080 is
not broken by the header.

## Single-superuser mode
Librora targets one machine, one owner, no employees. `seedDefaultPasswords()` only
touches `LIBRORA_ADMIN_USER` (default `admin`) and **only while the stored hash is still
the `R2FpdGVzdZWNyZXQ` placeholder from `V2__seed.sql`**. Resetting unconditionally
would silently revert the owner's chosen password to the default on every restart.
The `employee` row from the seed is left with its placeholder hash, so it exists in the
DB but **cannot log in**. It is not deleted on purpose: `V2__seed.sql` has already been
applied on existing installs and editing it would break their Flyway checksums.

## Swing launcher UI (`ServerWindow.java`)
- The logo is 1448x1086. A plain `JLabel` renders it at natural size, which in a
  vertical `BoxLayout` grows to 1086px and pushes every other component out of the
  window — the launcher then looks empty. `scaleToFit()` draws it into an 88x88
  `BufferedImage` and the label pins preferred/min/max size.
- Do **not** set a `preferredSize` on the panel in `column()`: `pack()` then freezes
  the height before components are added and the content is clipped. Height comes from
  `pack()`, width is pinned to `CONTENT_WIDTH + insets`.
- Do **not** use `<html>` labels in the launcher. An HTML `JLabel` reports a preferred
  width for the whole unwrapped line, and `CardLayout` sizes the window to the widest
  card. Use one `fixedLabel()` per line instead.
- The progress bar is determinate over `STARTUP_STEPS`; `step()` advances it.
- The browser opens `https://librora.local:8443/login` (`buildAccessUrl()`), falling
  back to the LAN IP only when `librora.local` does not resolve.

## Logging
`LibraryApplication` has a `static {}` block that sets `org.slf4j.simpleLogger.logFile`
to `./data/librora.log` **before** any `LoggerFactory.getLogger()` runs (the `log`
field is declared after it, and static initializers run in textual order). Windows and
macOS builds have no console, so that file is the only way to diagnose a user's machine.
Adding any logger earlier in the class will silently log only to stderr.

## Releases: Windows / macOS
`.github/workflows/build-installers.yml` builds on `workflow_dispatch`, PRs to `main`,
and `v*` tags: `Librora-<version>.exe` (jpackage + WiX 3.14) and `.dmg` for
`aarch64` (macos-14) and `x86_64` (macos-13). The 1448x1086 source icon must be
converted per platform first (PowerShell/`System.Drawing` → `.ico`, `sips`+`iconutil`
→ `.icns`); jpackage will not accept it directly.

