# for some common variables, e.g. plugin paths
include ../neutrino/neutrino-common-vars.inc

SUMMARY = "Channellogos for Neutrino GUI"
DESCRIPTION = "${SUMMARY}"
SECTION = "neutrino-plugin"
MAINTAINER = "Fred Feuerstein"
HOMEPAGE = "https://github.com/neutrino-images/ni-logo-stuff.git.git"

# There is no license defined...but considers itself as a free project.
# Pretty strange conditions actually because it's public on Github, but the provider
# insists on nobody is it allowed to distribute these graphics. Of course we don't do that.
# In addition, the users are tracked by whose integrated updater, and we will not use it.
# We do only package the git version to be able to do it properly. The graphics will be
# downloaded and installed directly from GitHub on the STB by users.
# We use our own logo updater for this, which can also be used without the package manager.

LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

PACKAGE_ARCH = "all"

DEPENDS = " \
	curl \
	logoupdater \
"

PROVIDES = "virtual/neutrino-logos"
RPROVIDES_${PN} = "virtual/neutrino-logos"

SRC_URI = " \
	git://github.com/neutrino-images/ni-logo-stuff.git;protocol=https \
"

PR = "r0"
SRCREV = "${AUTOREV}"
PV = "git-${SRCPV}"

S = "${WORKDIR}/git"

inherit gitpkgv

do_patch () {
	echo "${GITPKGV}" > "${S}/.rev";
}

do_configure[noexec] = "1"

do_compile[noexec] = "1"

do_install () {
	install -d ${D}/${N_LOGODIR}
	install -m 644 ${S}/.rev ${D}${N_LOGODIR}
}

FILES_${PN} = "\
	${N_LOGODIR}\
"

pkg_postinst_ontarget_${PN} () {
	touch /tmp/.keep_background
	echo -e "starting logo updater..."
	curl http://localhost/control/startplugin?name=logoupdater &>/dev/null
	curl http://localhost/control/rcem?KEY_GREEN &>/dev/null
}
