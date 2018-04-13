SUMMARY = "UNIX Shell similar to the Korn shell"
DESCRIPTION = "Zsh is a shell designed for interactive use, although it is also a \
               powerful scripting language. Many of the useful features of bash, \
               ksh, and tcsh were incorporated into zsh; many original features were added."
HOMEPAGE = "http://www.zsh.org"
SECTION = "base/shell"

LICENSE = "zsh"
LIC_FILES_CHKSUM = "file://LICENCE;md5=1a4c4cda3e8096d2fd483ff2f4514fec"

DEPENDS = "ncurses bison-native libcap libpcre gdbm groff-native"
RDEPENDS_${PN} = "base-files oh-my-zsh zsh-syntax-highlighting zsh-autosuggestions zsh-completions systemd"
SRC_URI = "${SOURCEFORGE_MIRROR}/${BPN}/${BP}.tar.gz"
SRC_URI[md5sum] = "2430c85bd128c59f5c0806ef00d3ea8d"
SRC_URI[sha256sum] = "d05606a545672ae8623828802dbcc4c83d9a4d3dbfb960e94a9fd9f62467c159"

inherit autotools gettext update-alternatives

EXTRA_OECONF = " \
    --bindir=${base_bindir} \
    --enable-etcdir=${sysconfdir} \
    --enable-fndir=${datadir}/${PN}/${PV}/functions \
    --enable-site-fndir=${datadir}/${PN}/site-functions \
    --with-term-lib='ncursesw ncurses' \
    --with-tcsetpgrp \
    --enable-cap \
    --enable-multibyte \
    --enable-gdbm \
    --enable-dynamic \
    zsh_cv_shared_environ=yes \
"

# Configure respects --bindir from EXTRA_OECONF, but then Src/Makefile will read bindir from environment
export bindir="${base_bindir}"

EXTRA_OEMAKE = "-e MAKEFLAGS="

ALTERNATIVE_${PN} = "sh"
ALTERNATIVE_LINK_NAME[sh] = "${base_bindir}/sh"
ALTERNATIVE_TARGET[sh] = "${base_bindir}/${BPN}"
ALTERNATIVE_PRIORITY = "100"

export AUTOHEADER = "true"

do_configure () {
    gnu-configize --force ${S}
    oe_runconf
}

pkg_postinst_ontarget_${PN} () {
    echo "L! /home/root/.zshrc - - - - /etc/zsh/zshrc" >> /etc/tmpfiles.d/00-create-volatile.conf
    chsh -s /bin/zsh
}

FILES_${PN}-dbg += "\
    ${libdir}/${PN}/${PV}/${PN}/.debug/*.so \
    ${libdir}/${PN}/${PV}/${PN}/db/.debug/*.so \
    ${libdir}/${PN}/${PV}/${PN}/net/.debug/*.so \
"

