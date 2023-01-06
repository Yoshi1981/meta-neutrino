include neutrino-lua-plugins-target-pattern.inc

SRC_NAME = "xupnpd"
SRC_SUBPATH = "${SRC_NAME}"
SUMMARY = "Content for xupnpd"

PROVIDES="virtual/neutrino-lua-plugin-xupnpd"
RPROVIDES_${PN} = "virtual/neutrino-lua-plugin-xupnpd"

SRC_URI = " \
    git://github.com/tuxbox-neutrino/plugin-scripts-lua.git;protocol=https;subpath=${SRC_SUBPATH};destsuffix=${SRC_DEST_SUFFIX} \
"

do_install () {
	# install content
	install -d ${D}/usr/share/xupnpd
	install -m 644 ${S}/xupnpd* ${D}/usr/share/xupnpd/

	# clean up, not required for content
	rm -rf ${D}/share
	rm -rf ${D}/usr/share/tuxbox
}
