#!/bin/bash
# librora-avahi-setup.sh
#
# Configura el DNS/mDNS de Librora a nivel de sistema para que
# http://librora.local resuene en todos los dispositivos de la red.
#
# Este script usa `sudo` (con solicitud interactiva de contraseña), por lo que
# DEBE ejecutarse dentro de una terminal. El código de Java lo abre en una
# terminal automaticamente cuando detecta que no hay sudo sin contraseña.
#
# Secuencia:
#   1. Detecta si avahi-daemon está corriendo; si no, sudo systemctl start avahi-daemon
#   2. sudo avahi-set-host-name librora   (librora.local => esta máquina en toda la red)
#   3. avahi-publish-service -s Librora _http._tcp <puerto> path=/  (servicio HTTP)
#   4. Fallback: sudo >> /etc/hosts  (acceso host-only) si avahi/sudo falla
set -u

PORT="${LIBRORA_PORT:-8080}"
export PATH="/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:${PATH:-}"

echo "=== Configuración de mDNS para Librora (se requieren permisos de administrador) ==="
echo "Puerto HTTP: $PORT"
echo

PRIV_OK=1
sudo -n true 2>/dev/null || { echo "Se pedirá la contraseña de sudo para los pasos que lo requieran."; PRIV_OK=0; }

# 1. Detectar avahi-daemon; iniciarlo si no está corriendo.
if ! pgrep -x avahi-daemon >/dev/null 2>&1; then
    echo "[1/4] avahi-daemon no está corriendo. Iniciando con sudo..."
    if sudo systemctl start avahi-daemon; then
        sleep 2
    else
        echo "      ERROR: sudo systemctl start avahi-daemon falló."
        PRIV_OK=0
    fi
else
    echo "[1/4] avahi-daemon ya está corriendo."
fi

if ! pgrep -x avahi-daemon >/dev/null 2>&1 || ! command -v avahi-publish-service >/dev/null 2>&1; then
    echo "      avahi no está disponible; se usará /etc/hosts como fallback."
    PRIV_OK=-1
fi

if [ "$PRIV_OK" -ge 0 ]; then
    # 2. Establecer el hostname mDNS -> librora.local
    echo "[2/4] Estableciendo hostname mDNS: sudo avahi-set-host-name librora"
    if sudo avahi-set-host-name librora; then
        echo "      Hostname establecido: librora.local resolverá en toda la red."
    else
        echo "      ERROR: sudo avahi-set-host-name librora falló."
        PRIV_OK=0
    fi

    # 3. Publicar el servicio HTTP
    echo "[3/4] Publicando servicio HTTP: avahi-publish-service -s Librora _http._tcp $PORT path=/"
    pkill -f "avahi-publish-service -s Librora" >/dev/null 2>&1 || true
    nohup avahi-publish-service -s Librora _http._tcp "$PORT" path=/ >/dev/null 2>&1 &
    disown 2>/dev/null || true
    echo "      Servicio publicado: Librora._http._tcp.local en puerto $PORT"
fi

# 4. Fallback /etc/hosts (solo si avahi no pudo cubrirlo)
if [ "$PRIV_OK" -ne 1 ]; then
    echo "[4/4] Fallback: agregando entrada a /etc/hosts (acceso host-only)"
    ip="$(hostname -I 2>/dev/null | awk '{print $1}')"
    [ -z "$ip" ] && ip="127.0.0.1"
    if grep -q "librora\.local" /etc/hosts 2>/dev/null; then
        echo "      La entrada ya existe en /etc/hosts."
    else
        if sudo bash -c "printf '%s librora.local\n' '$ip' >> /etc/hosts"; then
            echo "      Entrada agregada: $ip librora.local"
        else
            echo "      ERROR: no se pudo escribir /etc/hosts."
            echo "      Ejecuta manualmente: sudo sh -c \"echo '$ip librora.local' >> /etc/hosts\""
        fi
    fi
else
    echo "[4/4] avahi configurado correctamente. No se necesita /etc/hosts."
fi

echo
echo "=== ¡Listo! Abre http://librora.local:$PORT desde otro dispositivo. ==="
echo "(Este terminal se cierra en 3 segundos...)"
sleep 3
