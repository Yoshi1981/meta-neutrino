# for some common variables, e.g. plugin paths
include ../neutrino/neutrino-common-vars.inc

LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

## PLUGIN_NAME is defined by recipe name
PLUGIN_NAME = "${PN}"

## These variables should be defined in plugin recipe and will be override if no description was defined inside plugin recipe
SECTION = "neutrino-plugin"
DESCRIPTION ?= "Neutrino plugin: ${PLUGIN_NAME}"
MAINTAINER ?= "${FLAVOR}-developers"
HOMEPAGE ?= "https://github.com/tuxbox-neutrino"

## summary contains the plugin description
SUMMARY = "${DESCRIPTION}"

DEPENDS = "freetype ffmpeg zlib libxml2 virtual/libiconv openssl libpng curl giflib libjpeg-turbo"

PROVIDES = "virtual/neutrino-plugin-${PLUGIN_NAME}"
RPROVIDES_${PN} = "virtual/neutrino-plugin-${PLUGIN_NAME}"

PV = "git+${SRCPV}"

SRCREV = "${AUTOREV}"

inherit gitpkgv

# we need an independent file stack for the build, some templates for configure.ac and Makefile.am
# The plugin source and other required sources itself will be defined inside the file: neutrino-plugins-<provider>-env.inc
SRC_URI = " \
	file://acinclude.m4 \
	file://autogen.sh \
	file://COPYING \
	\
	file://configure.ac \
	file://Makefile.am \
"

S = "${WORKDIR}"

ALLOW_EMPTY_neutrino-plugins = "1"

inherit autotools pkgconfig gettext

EXTRA_OECONF = " \
	--enable-maintainer-mode \
	--with-target=native \
	--prefix=${N_PREFIX} \
	--with-boxtype=${TARGET_ARCH}box \
	--with-boxmodel=${MACHINE} \
	\
	--with-plugindir=${N_PLUGIN_DIR} \
	--with-plugindir_var=${N_PLUGIN_DIR_VAR} \
	--with-plugindir_mnt=${N_PLUGIN_DIR_MNT}\
	--with-webtvdir=${N_WEBTV_DIR} \
	--with-webtvdir_var=${N_WEBTV_DIR_VAR} \
	--with-webradiodir=${N_WEBRADIO_DIR} \
	--with-webradiodir_var=${N_WEBRADIO_DIR_VAR} \
	--with-luaplugindir=${N_LUAPLUGIN_DIR} \
	--with-luaplugindir_var=${N_LUAPLUGIN_DIR_VAR} \
	--with-gamesdir=${N_GAMES_DIR}  \
	--with-iconsdir=${N_ICONS_DIR}  \
	--with-iconsdir_var=${N_ICONS_DIR}  \
	--with-configdir=${N_CONFIG_DIR} \
	--with-zapitdir=${N_ZAPITDIR} \
	--with-datadir=${N_DATADIR} \
	--with-datadir_var=${N_DATADIR_VAR} \
	--with-controldir=${N_CONTROLDIR} \
	--with-controldir_var=${N_CONTROLDIR_VAR} \
	--with-fontdir=${N_FONTDIR} \
	--with-fontdir_var=${N_FONTDIR_VAR} \
	--with-libdir=${N_LIBDIR}  \
	--with-localedir=${N_LOCALEDIR} \
	--with-localedir_var=${N_LOCALEDIR_VAR} \
	--with-themesdir=${N_THEMESDIR} \
	--with-themesdir_var=${N_THEMESDIR_VAR} \
	--with-logodir=${N_LOGODIR} \
	--with-logodir_var=${N_LOGODIR_VAR} \
	--with-private_httpddir=${N_PRIVATE_HTTPDDIR} \
	--with-public_httpddir=${N_PUBLIC_HTTPDDIR} \
	--with-hosted_httpddir=${N_HOSTED_HTTPDDIR}  \
	--with-flagdir=${N_FLAGDIR} \
"

N_CFLAGS = "-Wall -W -Wshadow -g -O2 -funsigned-char -I${STAGING_INCDIR}/freetype2 -fcommon"
N_CXXFLAGS = "${N_CFLAGS}"
N_LDFLAGS += "-Wl,--hash-style=gnu -Wl,-rpath-link,${STAGING_DIR_HOST}${libdir},-lfreetype -lcrypto -lssl -lpng -lcurl -lz -lpthread"

do_compile () {
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
	oe_runmake CFLAGS="${N_CFLAGS}" CXXFLAGS="${N_CXXFLAGS}" LDFLAGS="${N_LDFLAGS}"
}

do_install_append() {
	rm -rf ${D}/usr/share/tuxbox/neutrino/plugins/*.la
	rm -rf ${D}/usr/share/tuxbox/neutrino/luaplugins
}

FILES_${PN} = " \
	/usr \
	/etc \
"

FILES_${PN}-dbg += " \
	/usr/share/tuxbox/neutrino/plugins/.debug \
"
