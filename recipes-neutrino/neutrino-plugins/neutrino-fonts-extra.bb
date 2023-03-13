# for some common variables, e.g. plugin paths
include ../neutrino/neutrino-common-vars.inc

LICENSE = "CC-BY-SA-4.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=4084714af41157e38872e798eb3fe1b1"

## These variables should be defined in plugin recipe and will be override if no description was defined inside plugin recipe
SECTION = "fonts"
DESCRIPTION = "Additional font type for neutrino-plugins"
MAINTAINER = "Community"
HOMEPAGE = "https://github.com/tuxbox-neutrino"

## summary contains the plugin description
SUMMARY = "${DESCRIPTION}"

PACKAGE_ARCH = "allarch"

PROVIDES = "virtual/neutrino-extra-fonts"
RPROVIDES_${PN} = "virtual/neutrino-extra-fonts"
# PROVIDES = "neutrino-extra-fonts"

SRC_URI = " \
	https://www.fontrepo.com/font/4281/pakenham.ttf \
"

SRC_URI[sha256sum] = "4ebb0c780855c08f45a0c98c7e9f4294370ccbdbf45b99f0a78adb6fbcf7ecfb"

PR = "r3"

do_configure[noexec] = "1"

do_compile[noexec] = "1"

do_install () {
	install -d ${D}/${N_FONTDIR}
	install -m 0644 ${WORKDIR}/pakenham.ttf ${D}/${N_FONTDIR}
}

FILES_${PN} += " \
	${N_FONTDIR} \
"
