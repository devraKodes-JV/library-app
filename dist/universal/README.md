# Librora 0.1.0-beta — Guía rápida

Gestión de biblioteca para **una sola máquina, un solo dueño, sin empleados**.
La aplicación es autónoma: **no necesita instalar Java, ni base de datos, ni
proxy, ni conexión a internet**.

## 1. Instalar

| Sistema | Archivo | Cómo instalarlo |
|---|---|---|
| Linux | `Librora-0.1.0-beta-x86_64.AppImage` | `chmod +x` y doble clic. No se instala nada. |
| Windows | `Librora-0.1.0-beta.exe` | Ejecutar el instalador. Incluye su propio Java. |
| macOS (Apple Silicon) | `Librora-0.1.0-beta-aarch64.dmg` | Abrir el `.dmg` y arrastrar a *Aplicaciones*. |
| macOS (Intel) | `Librora-0.1.0-beta-x86_64.dmg` | Ídem. |

## 2. Primer arranque

Al abrirlo aparece una ventana con el progreso:

1. Se crea la base de datos `data/library.mv.db`.
2. Se genera un **certificado HTTPS propio de tu máquina** (`data/librora-tls.p12`).
3. Se levanta **HTTPS en el 8443** y **HTTP en el 8080**.
4. El navegador se abre solo en `https://librora.local:8443/login`.

**Avisos del sistema** (normales, y solo la primera vez):

- **macOS** — Gatekeeper bloquea la app. Clic derecho sobre `Librora.app` →
  *Abrir* → *Abrir*.
- **Windows** — SmartScreen muestra «Windows protected your PC». *Más información*
  → *Ejecutar de todas formas*.
- **Navegador** — «La conexión no es privada», porque el certificado es
  autofirmado. *Avanzado* → *Continuar a librora.local*.

> Si entrás por la **IP** de la máquina en lugar de `librora.local`, el aviso
> **no aparece**: el certificado cubre también las IP locales.

## 3. Credenciales

| Usuario | Contraseña |
|---|---|
| `admin` | `admin123` |

Solo existe esta cuenta. **Cambiá la contraseña en el primer inicio** desde
*Configuración → Usuarios*: a partir de ahí se conserva y reiniciar la
aplicación **no** vuelve a poner `admin123`.

Para empezar con otras credenciales, exportá `LIBRORA_ADMIN_USER` y
`LIBRORA_ADMIN_PASSWORD` antes del primer arranque.

## 4. Uso diario

1. Abrir la aplicación.
2. Esperar la ventana verde **«Servidor activo»**.
3. El navegador se abre solo. Si no, usar **«Abrir navegador»** en esa ventana.
4. Para **apagar** el servidor, cerrar la ventana de Librora.

Desde el móvil u otro equipo de la misma red:

- `https://librora.local:8443` — si `librora.local` resuelve
- `https://IP-DEL-EQUIPO:8443` — siempre funciona

## 5. Copias de seguridad

Todo el estado está en la carpeta `data/`, junto al ejecutable:

| Archivo | Contenido |
|---|---|
| `data/library.mv.db` | Base de datos (catálogo, stock, clientes, caja) |
| `data/uploads/` | Portadas y adjuntos |
| `data/librora-tls.p12` + `.pass` | Certificado y clave de HTTPS |
| `data/librora.log` | Registro de ejecución |

**Para respaldar:** cerrar Librora y copiar la carpeta `data/` completa.
**Para restaurar:** copiarla de vuelta en el mismo lugar.

## 6. Problemas frecuentes

| Síntoma | Qué hacer |
|---|---|
| La ventana no muestra nada | Es el arranque; el logo y la barra de progreso aparecerán en unos segundos. |
| «Error al iniciar» | Leer `data/librora.log`.Casi siempre es que el puerto 8080 ya está ocupado por otra instancia. |
| El navegador no se abre | Usar el botón *Abrir navegador*, o copiar la dirección. |
| Aviso de certificado | Es esperado. *Avanzado → Continuar*. |
| Faltan estilos | Entrar siempre por `https://`. |
| `librora.local` no resuelve | Entrar por `https://IP-DEL-EQUIPO:8443`. Funciona igual. |
| Quiero empezar de cero | Cerrar Librora y borrar la carpeta `data/`. **Se pierde toda la información.** |

## 7. Configuración opcional

Variables de entorno (ninguna es obligatoria):

| Variable | Por defecto | Para qué |
|---|---|---|
| `LIBRORA_PORT` | `8080` | Puerto HTTP |
| `LIBRORA_HTTPS_PORT` | `8443` | Puerto HTTPS |
| `LIBRORA_TLS` | `true` | `false` = solo HTTP |
| `CSRF_HMAC_SECRET` | aleatorio | Fijalo para no invalidar sesiones al reiniciar |
| `LIBRORA_ADMIN_USER` | `admin` | Usuario inicial (solo primer arranque) |
| `LIBRORA_ADMIN_PASSWORD` | `admin123` | Contraseña inicial (solo primer arranque) |

---

Documentación completa de funcionalidades en `USER_MANUAL.md`.
