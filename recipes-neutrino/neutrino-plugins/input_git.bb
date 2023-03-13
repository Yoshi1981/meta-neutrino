include neutrino-plugins-tuxbox-env.inc

DESCRIPTION = "An editor which can be called from scripts."

DEPENDS += "neutrino-fonts-extra"

SRC_URI += " \
	file://0001-input-Makefile.am-remove-install-exec-local.patch \
"

do_patch () {
	git -C ${S}/${PLUGIN_NAME} apply ${S}/0001-input-Makefile.am-remove-install-exec-local.patch
}
