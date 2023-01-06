
include neutrino-lua-plugins.inc
## PN (package name = filename)
#PN = "neutrino-lua-plugins-shared-files"
SUMMARY = "Shared files and scripts for neutrino-lua-plugins"

SRC_URI = " \
	git://github.com/tuxbox-neutrino/plugin-scripts-lua.git;protocol=https;subpath=share;destsuffix=${SRC_DEST_SUFFIX} \
"

PV_LUA = "5.2"

SRC_SUBPATH = "lua/${PV_LUA}"

do_install () {
#	install -d ${D}/usr/share/lua/${LUA_VER}
	mkdir -p ${D}/usr/share/lua/${LUA_VER}
	cp -r ${S}/${SRC_SUBPATH}/n_*.lua ${D}/usr/share/lua/${LUA_VER}/
##	json.lua not required, already provided by json package itself
# 	cp -r ${S}/${PV_LUA}/json.lua ${D}/usr/share/lua/${LUA_VER}/json.lua

# 	install -d ${D}/usr/share/lua/${PV_LUA}
# 	cp -r ${S}/${SRC_SUBPATH}/* ${D}/usr/share/lua/${PV_LUA}/

	rm -r ${D}/usr/share/tuxbox
}
