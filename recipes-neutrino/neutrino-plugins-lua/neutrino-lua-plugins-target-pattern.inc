include neutrino-lua-plugins.inc

# PN = mostly not the same like target name, defined in sub recipes
SRC_NAME = "${PN}"

RDEPENDS_${PN} += "neutrino-lua-plugins-shared-files"

#SRC_SUBPATH = "plugins/${SRC_NAME}"

do_compile[noexec] = "1"

do_install () {
	cp -rf ${S}/* ${D}${N_LUAPLUGIN_DIR}
	rm -f ${D}${N_LUAPLUGIN_DIR}/Makefile.*
}
