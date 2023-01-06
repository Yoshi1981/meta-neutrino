include neutrino-lua-plugins-target-pattern.inc

MAINTAINER = "vanhofen"
HOMEPAGE = "https://github.com/neutrino-images/ni-neutrino-plugins"
LICENSE = "WTFPLv2"

PV = "ni-git-${SRCPV}"

SRC_SUBPATH = "scripts-lua/plugins/${SRC_NAME}"

SRC_URI = " \
	git://github.com/neutrino-images/ni-neutrino-plugins.git;protocol=https;subpath=${SRC_SUBPATH};destsuffix=${SRC_DEST_SUFFIX} \
"

#SRC_NAME = "plutotv-update"
