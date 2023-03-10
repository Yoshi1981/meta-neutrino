# for some common variables, e.g. plugin paths
include ../neutrino/neutrino-common-vars.inc

LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

## These variables should be defined in plugin recipe and will be override if no description was defined inside plugin recipe
SECTION = "fonts"
DESCRIPTION = "Additional font type for neutrino-plugins"
MAINTAINER = "Community"
HOMEPAGE = "https://github.com/tuxbox-neutrino"

## summary contains the plugin description
SUMMARY = "${DESCRIPTION}"

PACKAGE_ARCH = "all"

SRC_URI = " \
	file://pakenham.ttf \
"

PV = "1"
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
