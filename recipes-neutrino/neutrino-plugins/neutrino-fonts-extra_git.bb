include neutrino-plugins-ni-env.inc

DESCRIPTION = "Additional data and fonts for plugins"

SUB_PATH = "data/fonts"

do_configure[noexec] = "1"

do_compile[noexec] = "1"

do_install () {
	install -d ${D}/${N_FONTDIR}
	install -m 0644 ${S}/${PN}/pakenham.ttf ${D}/${N_FONTDIR}
}

FILES_${PN} += " \
	${N_FONTDIR} \
"
