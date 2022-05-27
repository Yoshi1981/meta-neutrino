HOMEPAGE = "https://github.com/TangoCash"
MAINTAINER = "TangoCash"

SRC_URI += "git://github.com/TangoCash/libstb-hal-tangos.git;protocol=https \
"

PV = "${FLAVOUR}-git${SRCPV}"

EXTRA_OECONF_append += " \
			--enable-reschange \
                        --with-boxtype=${MACHINE} \
"
