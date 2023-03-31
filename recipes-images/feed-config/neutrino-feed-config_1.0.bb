DESCRIPTION = "Neutrino-HD image feed configuration"
# derived from poky-feed-config
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
PR = "r2"
PACKAGE_ARCH = "${MACHINE_ARCH}"
INHIBIT_DEFAULT_DEPS = "1"


do_compile () {
	# Create a new directory with the path ${S}/${sysconfdir}/opkg/, if not exists.
	mkdir -p ${S}/${sysconfdir}/opkg/

	# Set variable basefeedconf to the path ${S}/${sysconfdir}/opkg/base-feeds.conf.
	basefeedconf=${S}/${sysconfdir}/opkg/base-feeds.conf

	# Remove the file at path $basefeedconf if it exists, and then create an empty file at the same path.
	rm -f $basefeedconf
	touch $basefeedconf

	# If the environment variable IPK_FEED_SERVER is set, append a few lines of comments to the file at path $basefeedconf.
	# If IPK_FEED_SERVER is not set, append a different set of comments. In either case, add an additional comment line to $basefeedconf.
	if [ -n "${IPK_FEED_SERVER}" ]; then
		echo "# URI prefix '${IPK_FEED_SERVER}'" >> $basefeedconf
		echo "# is set by the IPK_FEED_SERVER variable." >> $basefeedconf
		echo "# Architectures which had no packages available" >> $basefeedconf
		echo "# at image creation time are commented out." >> $basefeedconf
	else
		echo "# set the IPK_FEED_SERVER variable during build to" >> $basefeedconf
		echo "# configure real feeds here." >> $basefeedconf
	fi
	echo "#" >> $basefeedconf

	# Use the find command to locate all subdirectories in the directory ${DEPLOY_DIR_IPK} that contain packages.
	# For each subdirectory, set the variable FNAME to the subdirectory name, and if IPK_FEED_SERVER is set,
	# construct a URI for the subdirectory and append a line to $basefeedconf with the format src/gz FNAME URI.
# 	ipkgarchs="${PACKAGE_ARCHS}"
	ipkgarchs=$(find ${DEPLOY_DIR_IPK} -mindepth 1 -maxdepth 1 -type d -printf '%f\n')
	for arch in $ipkgarchs; do
# 		if [[ $arch == "any" || $arch == "noarch" ]]; then
# 			continue
# 		fi
		FNAME="$arch"
		if [ -n "${IPK_FEED_SERVER}" ]; then
			URI="${IPK_FEED_SERVER}/$arch"
			printf "src/gz\t$FNAME\t$URI\n" >> $basefeedconf
		fi
	done

	# Set the variable H to the current hostname, and set BUILD_DIR to the base name of the directory ${TOPDIR}.
	# Use the sed command to replace instances of @hostname@ and @build-dirname@ in the file at path $basefeedconf with the values of H and BUILD_DIR, respectively. 
	# NOTE: If the user has provided a custom URL or IP address using the IPK_FEED_SERVER variable, the placeholders have no effect.
	H=$(hostname)
	BUILD_DIR=$(basename ${TOPDIR})
	sed -i -e "s|@hostname@|${H}|g" "$basefeedconf"
	sed -i -e "s|@build-dirname@|${BUILD_DIR}|g" "$basefeedconf"
}

do_install () {
	install -d ${D}${sysconfdir}/opkg
	install -m 0644  ${S}/${sysconfdir}/opkg/* ${D}${sysconfdir}/opkg/
}

FILES_${PN} = "${sysconfdir}/opkg/ "

CONFFILES_${PN} += "${sysconfdir}/opkg/base-feeds.conf"
