PV = "2.36.1"

SRC_URI = "${KERNELORG_MIRROR}/software/scm/git/git-${PV}.tar.gz;name=tarball \
           ${KERNELORG_MIRROR}/software/scm/git/git-manpages-${PV}.tar.gz;name=manpages \
           file://fixsort.patch \
	   file://CVE-2021-21300.patch;apply=no  \
"

SRC_URI[tarball.sha256sum] = "37d936fd17c81aa9ddd3dba4e56e88a45fa534ad0ba946454e8ce818760c6a2c"
SRC_URI[manpages.sha256sum] = "3fcd315976f06b54b0abb9c14d38c3d484f431ea4de70a706cc5dddc1799f4f7"
