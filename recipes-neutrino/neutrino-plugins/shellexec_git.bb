include neutrino-plugins-ni-env.inc

DESCRIPTION = "Terminal controlled menu manager known as Flexmenu."

DEPENDS += "neutrino-fonts-extra"

SRC_URI += " \
	file://0001-shellexec-Makefile.am-fix-install.patch \
"

do_patch () {
	git -C ${S}/${PLUGIN_NAME} apply ${S}/0001-shellexec-Makefile.am-fix-install.patch
}

## Hack: make install is doing strange things, try to fix it here
do_install () {
	install -d ${D}${bindir}
	install -d ${D}${N_PLUGIN_DIR}
	oe_runmake install DESTDIR=${D}
}

FILES_${PN} += " \
	/share \
	/var \
"
