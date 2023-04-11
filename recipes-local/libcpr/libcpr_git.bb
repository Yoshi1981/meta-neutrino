SUMMARY = "libcpr library"
DESCRIPTION = "libcpr is a C++ HTTP library with a simple and clean API."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=08beaae5deae1c43c065592da8f38095"

CPR_BRANCH = "1.9"
PV = "${CPR_BRANCH}.3"

SRC_URI = "git://github.com/libcpr/cpr.git;branch=${CPR_BRANCH}.x;tag=${PV};protocol=https"

S = "${WORKDIR}/git"

inherit cmake pkgconfig

DEPENDS = "curl openssl zlib"

do_install() {
	# Remove non-symlink .so files from libcpr-dev package
	rm -f ${D}${libdir}/libcpr.so ${D}${libdir}/libcpr.a

	install -d ${D}${libdir}
	install -m 0644 ${WORKDIR}/build/lib/libcpr.so.${PV} ${D}${libdir}/libcpr.so.${PV}
	ln -sf libcpr.so.${PV} ${D}${libdir}/libcpr.so
	ln -sf libcpr.so.${PV} ${D}${libdir}/libcpr.so.1
}

#do_rm_work[noexec] = "1"
