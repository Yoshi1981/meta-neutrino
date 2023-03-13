include neutrino-plugins-tuxbox-env.inc

DESCRIPTION = "Get rc-signals for scripts..."


SRC_URI += " \
	file://Makefile.am.${PLUGIN_NAME} \
"

do_configure_prepend () {
	if test -f ${S}/Makefile.am.${PLUGIN_NAME}; then
		mv -v ${S}/Makefile.am.${PLUGIN_NAME} ${S}/${PLUGIN_NAME}/Makefile.am
	fi
}
