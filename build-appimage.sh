#!/bin/bash
# build-appimage.sh - reproducible build of the Librora AppImage.
#
# Builds the shaded JAR, generates a COMPLETE jpackage application image
# (with a full bundled JVM runtime), wires in the custom AppRun/mDNS setup,
# packs it as an xz-compressed squashfs, and prepends the AppImage ELF
# runtime stub to produce a valid type-2 AppImage.
#
# Prerequisites: mvn, jpackage (JDK 25), mksquashfs, python3.
set -euo pipefail

ROOT="$(cd "$(dirname "$0")" && pwd)"
# Single source of truth: the Maven project version (currently 0.1.0-beta).
APP_VERSION="$(python3 -c 'import re;m=re.search(r"<version>([^<]+)</version>",open("'"$ROOT"'/pom.xml").read());print(m.group(1))')"
APP_NAME="Librora"
APP_IMAGE_NAME="${APP_NAME}-${APP_VERSION}-x86_64.AppImage"
PORT="${LIBRORA_PORT:-8080}"
ELF_STUB_BYTES=193728

WORK="/tmp/kilo/appimage-build"
INPUT="/tmp/kilo/jpkg-input"
APPDIR="/tmp/kilo/appdir"
DIST_LINUX="${ROOT}/dist/linux"
DIST_UNIVERSAL="${ROOT}/dist/universal"

echo "==> [1/7] Building shaded JAR (mvn install -DskipTests)"
(cd "$ROOT" && mvn install -DskipTests -f pom.xml >/dev/null)
JAR="${ROOT}/bootstrap/target/bootstrap-${APP_VERSION}.jar"
if [ ! -s "$JAR" ]; then
    echo "ERROR: expected shaded JAR at $JAR (build produced a different name?)" >&2
    ls -la "${ROOT}/bootstrap/target/"*.jar >&2
    exit 1
fi

echo "==> [2/7] Generating complete jpackage application image"
rm -rf "$WORK" "$INPUT" "$APPDIR"
mkdir -p "$INPUT"
cp "$JAR" "$INPUT/bootstrap-${APP_VERSION}.jar"
mkdir -p "$WORK"
jpackage --type app-image \
    --name "$APP_NAME" \
    --app-version "$APP_VERSION" \
    --input "$INPUT" \
    --main-jar "bootstrap-${APP_VERSION}.jar" \
    --main-class com.library.bootstrap.LibraryApplication \
    --icon "${ROOT}/bootstrap/src/main/resources/icon.png" \
    --java-options "-Xms256m -Xmx1024m" \
    --dest "$WORK"
APPDIR="${WORK}/${APP_NAME}"

echo "==> [3/7] Cleaning lib/app (keep only the shaded JAR)"
cd "$APPDIR"
rm -f lib/app/original-*.jar lib/app/bootstrap-*.jar 2>/dev/null || true
cp "$INPUT/bootstrap-${APP_VERSION}.jar" lib/app/bootstrap-${APP_VERSION}.jar

echo "==> [4/7] Installing custom AppRun + desktop + icon"
cp "${ROOT}/bootstrap/src/main/resources/appimage/AppRun" AppRun
chmod +x AppRun
cp "${ROOT}/bootstrap/src/main/resources/appimage/Librora.desktop" Librora.desktop
sed -i "s/^Version=.*/Version=${APP_VERSION}/" Librora.desktop
cp "${ROOT}/bootstrap/src/main/resources/icon.png" Librora.png
# Interactive privileged avahi setup helper (run in a terminal when sudo -n is unavailable).
cp "${ROOT}/bootstrap/src/main/resources/appimage/librora-avahi-setup.sh" librora-avahi-setup.sh
chmod +x librora-avahi-setup.sh

echo "==> [5/7] Extracting AppImage ELF runtime stub"
REFERENCE_APP=""
for d in "$DIST_LINUX" "$DIST_UNIVERSAL"; do
    # The AppImage ELF runtime stub is version-agnostic, so any shipped
    # Librora AppImage can supply it.
    cand="$(ls -t "$d"/Librora-*.AppImage 2>/dev/null | head -1)"
    if [ -n "$cand" ]; then REFERENCE_APP="$cand"; break; fi
done
if [ ! -s "$REFERENCE_APP" ]; then
    echo "ERROR: no existing AppImage to extract the ELF runtime stub from." >&2
    echo "       Ship at least one correct AppImage in dist/ first, then re-run." >&2
    exit 1
fi
python3 - "$REFERENCE_APP" "$ELF_STUB_BYTES" <<'PY'
import sys, os
src, n = sys.argv[1], int(sys.argv[2])
with open(src, "rb") as f:
    data = f.read(n)
assert data[:4] == b"\x7fELF", "stub is not an ELF binary"
with open("/tmp/kilo/appimage-runtime.elf", "wb") as out:
    out.write(data)
print(f"wrote {len(data)} byte ELF stub")
PY

echo "==> [6/7] Packing squashfs (xz) and concatenating ELF stub"
cd "$APPDIR"
mksquashfs . /tmp/kilo/new.squashfs -comp xz -b 131072 -noappend >/dev/null 2>&1
cat /tmp/kilo/appimage-runtime.elf /tmp/kilo/new.squashfs > "/tmp/kilo/${APP_IMAGE_NAME}"
chmod +x "/tmp/kilo/${APP_IMAGE_NAME}"
ls -l "/tmp/kilo/${APP_IMAGE_NAME}"

echo "==> [7/7] Installing AppImage into dist/linux and dist/universal"
rm -f "${DIST_LINUX}/${APP_NAME}-"*.AppImage "${DIST_UNIVERSAL}/${APP_NAME}-"*.AppImage
cp "/tmp/kilo/${APP_IMAGE_NAME}" "${DIST_LINUX}/${APP_IMAGE_NAME}"
cp "/tmp/kilo/${APP_IMAGE_NAME}" "${DIST_UNIVERSAL}/${APP_IMAGE_NAME}"
chmod +x "${DIST_LINUX}/${APP_IMAGE_NAME}" "${DIST_UNIVERSAL}/${APP_IMAGE_NAME}"

echo "==> Verifying AppImage offset:" "${DIST_LINUX}/${APP_IMAGE_NAME}"
"${DIST_LINUX}/${APP_IMAGE_NAME}" --appimage-offset

echo "==> DONE: ${APP_IMAGE_NAME} (${APP_VERSION}) shipped to dist/linux and dist/universal"
