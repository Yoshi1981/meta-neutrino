

# Some additional lines for /usr/lib/os-release -> /etc/os-release
#
OS_RELEASE_FIELDS_append = " HOME_URL SUPPORT_URL BUG_REPORT_URL VERSION_CODENAME NAME PRETTY_NAME DISTRO_VERSION"

HOME_URL ?= "https://github.com/tuxbox-neutrino"
SUPPORT_URL ?= "https://wiki.tuxbox-neutrino.org"
BUG_REPORT_URL ?= "https://forum.tuxbox-neutrino.org"
NAME = "${ID}-Image"
PRETTY_NAME = "OpenEmbedded ${ID}"
VERSION = "${DISTRO_BUILD_VERSION}"
