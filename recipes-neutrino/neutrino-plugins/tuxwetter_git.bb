include neutrino-plugins-ni-env.inc

DESCRIPTION = "Plugin for weather data and forecast known as Tuxwetter."

DEPENDS += "neutrino-fonts-extra"

SRC_URI += " \
	file://0001-tuxwetter-Makefile.am-fix-install.patch \
"

do_patch () {
	git -C ${S}/${PLUGIN_NAME} apply ${S}/0001-tuxwetter-Makefile.am-fix-install.patch
}

# "bindir" will never installed
do_install_prepend () {
	install -d ${D}${bindir}
}
