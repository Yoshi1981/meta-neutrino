include neutrino-plugins-tuxbox-env.inc

DESCRIPTION = "A terminal controlled message box handler."

DEPENDS += "neutrino-fonts-extra"

SRC_URI += " \
	file://0001-msgbox-Makefile.am-fix-build.patch \
"

do_patch () {
	git -C ${S}/${PLUGIN_NAME} apply ${S}/0001-msgbox-Makefile.am-fix-build.patch
}
