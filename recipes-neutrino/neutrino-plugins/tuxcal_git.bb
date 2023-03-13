include neutrino-plugins-ni-env.inc

DESCRIPTION = "Graphical calendar known as Tuxcal."

DEPENDS += "neutrino-fonts-extra"

## Hack: make install is doing strange things, try to fix it here
do_install () {
	install -d ${D}${bindir}
	install -d ${D}${N_PLUGIN_DIR}
	oe_runmake install DESTDIR=${D}
}
