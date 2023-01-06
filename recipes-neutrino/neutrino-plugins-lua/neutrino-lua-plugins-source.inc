SRC_DEST_SUFFIX = "src"

SRC_NAME = "${BPN}"

SRC_URI = " \
	git://github.com/tuxbox-neutrino/plugin-scripts-lua.git;protocol=https;subpath=plugins/${SRC_NAME};destsuffix=${SRC_DEST_SUFFIX} \
"

LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SRCREV = "${AUTOREV}"
PV = "git-${SRCPV}"
PR = "r1"

S = "${WORKDIR}/${SRC_DEST_SUFFIX}"
