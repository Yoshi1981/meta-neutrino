DESCRIPTION = "Links is graphics and text mode WWW \
browser, similar to Lynx."
HOMEPAGE = "http://links.twibright.com/"
SECTION = "console/network"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=b0c80473f97008e42e29a9f80fcc55ff"
DEPENDS = "gpm jpeg libpng flex openssl zlib"

SRC_URI = " \
		http://links.twibright.com/download/links-${PV}.tar.bz2 \
		file://0001-port-to-neutrino.patch \
		file://0002-ac-prog-cxx.patch \
		file://0003-accept_https_play.patch \
		file://bookmarks \
		file://bookmarks.html \
		file://links-browser.cfg \
		file://links-plugin.cfg \
		file://tables.tar.gz;unpack=0 \
"

S = "${WORKDIR}/links-${PV}"

PACKAGECONFIG ??= "bzip2 lzma"
PACKAGECONFIG[bzip2] = "--with-bzip2,--without-bzip2,bzip2"
PACKAGECONFIG[lzma] = "--with-lzma,--without-lzma,xz"

inherit autotools pkgconfig

EXTRA_OECONF = " \
	--enable-graphics \
	--with-fb \
	--with-libjpeg \
	--with-ssl=${STAGING_LIBDIR}/.. --enable-ssl-pkgconfig \
	--with-zlib  \
	--without-atheos \
	--without-directfb \
	--without-libtiff \
	--without-lzma \
	--without-pmshell \
	--without-svgalib \
	--without-x \
"

do_install_append() {
	install -d ${D}${datadir}/tuxbox/neutrino/plugins -d ${D}${sysconfdir}/neutrino/config/links
	ln -sf /usr/bin/links ${D}${datadir}/tuxbox/neutrino/plugins/links.so
	install -m 0644 ${WORKDIR}/bookmarks ${D}${sysconfdir}/neutrino/config
	install -m 0644 ${WORKDIR}/links-browser.cfg ${D}${sysconfdir}/neutrino/config/links/links.cfg
	install -m 0644 ${WORKDIR}/links-plugin.cfg ${D}${datadir}/tuxbox/neutrino/plugins/links.cfg
	touch ${D}${sysconfdir}/neutrino/config/links/links.his
	install -m 0644 ${WORKDIR}/bookmarks.html ${D}${sysconfdir}/neutrino/config/links
	install -m 0644 ${WORKDIR}/tables.tar.gz ${D}${sysconfdir}/neutrino/config/links
}

FILES_${PN} += "/usr"

SRC_URI[md5sum] = "2aa45c8827d9210e936ee91b5b8801b9"
SRC_URI[sha256sum] = "2dd78508698e8279ef4f09a3a2a21e9595040113402da6c553974414fb49dd2c"

INSANE_SKIP_${PN}+= "dev-so"
