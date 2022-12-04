# for some common variables, e.g. plugin paths
include ../neutrino/neutrino-common-vars.inc

SUMMARY = "Webinterface for Neutrino GUI"
DESCRIPTION = "Web Interface for controlling Neutrino GUI via browser"
HOMEPAGE = "https://github.com/tuxbox-neutrino"
SECTION = "libs"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI = "git://github.com/Tuxbox-Project/neutrino-webif.git;protocol=https"

PACKAGE_ARCH = "all"

DEPENDS = " \
	neutrino-mp \
"

PROVIDES = "virtual/neutrino-webif"
RPROVIDES_${PN} = "virtual/neutrino-webif"

PR = "r1"
VERSION_MAJOR = "3"
VERSION_MINOR = "0"
PV = "${VERSION_MAJOR}.${VERSION_MINOR}-${SRCPV}"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

do_configure[noexec] = "1"

do_compile[noexec] = "1"

inherit gitpkgv

do_install () {
	export VERSION="${VERSION_MAJOR}.${VERSION_MINOR}.${GITPKGV}"
	export PACKAGE_NAME="${PN}"
	export PRETTY_PACKAGE_NAME="${DESCRIPTION}"

	export CONFIGDIR="${N_CONFIG_DIR}"
	export CONTROLDIR="${N_CONTROLDIR}"
	export CONTROLDIR_VAR="${N_CONTROL_DIR_VAR}"
	export DATADIR="${N_DATADIR}"
	export DATADIR_VAR="${N_DATADIR_VAR}"
	export FLAGDIR="${N_FLAGDIR}"
	export FONTDIR="${N_FONTDIR}"
	export FONTDIR_VAR="${N_FONTDIR_VAR}"
	export GAMESDIR="${N_GAMES_DIR}"
	export ICONSDIR_VAR="${N_ICONS_DIR}"
	export LIBDIR="${N_ICONS_DIR_VAR}"
	export LOCALEDIR="${N_LOCALEDIR}"
	export LOCALEDIR_VAR="${N_LOCALEDIR_VAR}"
	export LOGODIR="${N_LOGODIR}"
	export LOGODIR_VAR="${N_LOGODIR_VAR}"
	export LUAPLUGINDIR="${N_LUAPLUGIN_DIR}"
	export LUAPLUGINDIR_VAR="${N_LUAPLUGIN_DIR_VAR}"
	export PLUGINDIR="${N_PLUGIN_DIR}"
	export PLUGINDIR_MNT="${N_PLUGIN_DIR_MNT}"
	export PLUGINDIR_VAR="${N_PLUGIN_DIR_VAR}"
	export THEMESDIR="${N_THEMESDIR}"
	export THEMESDIR_VAR="${N_THEMESDIR_VAR}"
	export WEBRADIODIR="${N_WEBRADIO_DIR}"
	export WEBRADIODIR_VAR="${N_WEBRADIO_DIR_VAR}"
	export ZAPITDIR="${N_ZAPITDIR}"
	export WEBTVDIR="${N_WEBTV_DIR}"
	export WEBTVDIR_VAR="${N_WEBTV_DIR_VAR}"

	export LCD4L_ICONSDIR="${N_LCD4L_ICONSDIR}"
	export LCD4L_ICONSDIR_VAR="${N_LCD4L_ICONSDIR_VAR}"

	export PRIVATE_HTTPDDIR="${N_PRIVATE_HTTPDDIR}"
	export PUBLIC_HTTPDDIR="${N_PUBLIC_HTTPDDIR}"
	export HOSTED_HTTPDDIR="${N_HOSTED_HTTPDDIR}"

	export WEBIF_INSTALL_PREFIX="${D}"

	oe_runmake  install
}

FILES_${PN} = "\
	${N_PRIVATE_HTTPDDIR}\
"	
