include ../../recipes-devtools/lua/lua.inc

# for some common variables, e.g. plugin paths
include ../neutrino/neutrino-common-vars.inc

include neutrino-lua-plugins-source.inc

SECTION = "neutrino-plugin"
MAINTAINER = "tuxbox"
PRE_DESCRIPTION ?= "Neutrino Lua plugin ${SRC_NAME}"
HOMEPAGE = "https://github.com/tuxbox-neutrino/plugin-scripts-lua.git"
SUMMARY = "${PRE_DESCRIPTION} by ${MAINTAINER}"

RDEPENDS_${PN} = "lua-feedparser lua-expat lua-json luaposix"

do_install_prepend () {
	install -d ${D}${N_LUAPLUGIN_DIR}
	install -d ${D}/share/tuxbox/neutrino
#	ln -sf ${N_LUAPLUGIN_DIR} ${D}/share/tuxbox/neutrino/plugins
}

FILES_${PN} += " \
	/usr \
	/share \
	${N_LUAPLUGIN_DIR} \
"
