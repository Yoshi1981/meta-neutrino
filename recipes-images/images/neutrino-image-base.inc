# Base this image on core-image-minimal
include recipes-core/images/core-image-minimal.bb

# inherit image_version

# add default password for root

ROOTFS_POSTPROCESS_COMMAND += "set_root_passwd;"
set_root_passwd() {
	ROOTPW_ENCRYPTED=""
	if [ -n "${ROOTPW}" ]; then
		ROOTPW_ENCRYPTED=`openssl passwd -crypt -quiet ${ROOTPW}`
	else
		ROOTPW_ENCRYPTED="wYNffsf6sozwE"
	fi
	sed "s%^root:[^:]*:%root:${ROOTPW_ENCRYPTED}:%" \
	< ${IMAGE_ROOTFS}/etc/shadow \
	> ${IMAGE_ROOTFS}/etc/shadow.new;
	mv ${IMAGE_ROOTFS}/etc/shadow.new ${IMAGE_ROOTFS}/etc/shadow ;
	rm -rf ${IMAGE_ROOTFS}/tmp/*
}

# ROOTFS_PREPROCESS_COMMAND += "set_image_build_version; "
# set_image_build_version () {
# 	echo ${BUILD_VERSION} > ${DEPLOY_DIR_IMAGE}/build_version
# }

ROOTFS_POSTPROCESS_COMMAND += "set_image_version; "
# We create file /etc/image-version for common use (required by some plugins)
# and for compatibility we make a symlink from /etc/image-version to /.version
# required by neutrino itself.
set_image_version () {
	META_NAME="meta-neutrino"
	META_VERSION=$(git -C ${COREBASE}/$META_NAME rev-list --count $(git -C ${COREBASE}/$META_NAME branch --show-current))
	RELEASE_NAME="${IMAGE_BASENAME}"
	RELEASE_NAME_DISCRIBE=`git -C ${COREBASE}/$META_NAME  describe --always --tags --dirty`

	RELEASE_STATE="3"
	if [ "${DISTRO_TYPE}" == "release" ]; then
		RELEASE_STATE="0"
	elif [ "${DISTRO_TYPE}" == "beta" ]; then
		RELEASE_STATE="1"
	elif [ "${DISTRO_TYPE}" == "nightly" ]; then
		RELEASE_STATE="2"
	fi

	IMAGE_DESCRIPTION="${MACHINE_BRAND}-${MACHINE_NAME}-${DISTRO_VERSION}.${META_VERSION}"

	# get content of ../imageversion file
	# required to synchronize version entry with content of ../imageversion file, which is used for image updates
# 	echo ${IMAGE_NAME} > ${DEPLOY_DIR_IMAGE}/imageversion

# 	IMAGE_STAMP_STR=`cat ${DEPLOY_DIR_IMAGE}/build_version`
	IMAGE_STAMP_STR=${BUILD_VERSION}
	IMAGE_STAMP_LEN=${#IMAGE_STAMP_STR}
	IMAGE_STAMP_START=`expr ${IMAGE_STAMP_LEN}-14`
	IMAGE_STAMP=`echo ${IMAGE_STAMP_STR} | cut -d '-' -f3`
	IMAGE_BUILD_TIME_STAMP=`echo ${IMAGE_STAMP} | cut -c1-12`

	#IMAGE_ROOTFS_ETC_DIR=${D}${sysconfdir}
	IMAGE_ROOTFS_ETC_DIR=${IMAGE_ROOTFS}/etc

	IMAGE_VERSION_FILE="${IMAGE_ROOTFS_ETC_DIR}/image-version"

	# create content for /etc/image-version file
	DD=`echo ${IMAGE_STAMP} | cut -c7-8`
	MM=`echo ${IMAGE_STAMP} | cut -c5-6`
	YY=`echo ${IMAGE_STAMP} | cut -c1-4`
	HH=`echo ${IMAGE_STAMP} | cut -c9-10`
	mm=`echo ${IMAGE_STAMP} | cut -c11-12`
	echo "builddate=${DD}.${MM}.${YY} ${HH}:${mm}" > ${IMAGE_VERSION_FILE}
	echo "box_model=${MACHINE}"                                                     				>> ${IMAGE_VERSION_FILE}
	echo "creator=${CREATOR}"                                                       				>> ${IMAGE_VERSION_FILE}
	echo "distro=${DISTRO}"                                                             				>> ${IMAGE_VERSION_FILE}
	echo "distro_version=${DISTRO_VERSION}"                                                             		>> ${IMAGE_VERSION_FILE}
	echo "homepage=${HOME_URL}"                                                     				>> ${IMAGE_VERSION_FILE}
	echo "imagename=${IMAGE_BASENAME}"  	                                         				>> ${IMAGE_VERSION_FILE}
	echo "machine=${MACHINE}"                                                       				>> ${IMAGE_VERSION_FILE}
	VERSION_STAMP="${RELEASE_STATE}${DISTRO_VERSION_NUMBER_CYCLE}${IMAGE_BUILD_TIME_STAMP}"
	echo "version=${VERSION_STAMP}"   										>> ${IMAGE_VERSION_FILE}
	echo "imagedescription=${IMAGE_DESCRIPTION}"                                    				>> ${IMAGE_VERSION_FILE}
	echo "imageversion=${DISTRO_VERSION}"                                             				>> ${IMAGE_VERSION_FILE}
	echo "describe=${RELEASE_NAME_DISCRIBE}"                                        				>> ${IMAGE_VERSION_FILE}
	echo "neutrino_src=${FLAVOUR}"                                                  				>> ${IMAGE_VERSION_FILE}
	echo "imagedir=${MACHINE}"                                                      				>> ${IMAGE_VERSION_FILE}
	echo "image_update_url=${IMAGE_LOCATION_URL}"                                       				>> ${IMAGE_VERSION_FILE}

	H=$(hostname)
	BUILD_DIR=$(basename ${TOPDIR})

	sed -i -e "s|@hostname@|${H}|g" "${IMAGE_VERSION_FILE}"
	sed -i -e "s|@build-dirname@|${BUILD_DIR}|g" "${IMAGE_VERSION_FILE}"

	# We create a symlink from /etc/image-version to /.version to have all required content
	# and we have a compatible data source.
	ln -sf /etc/image-version ${IMAGE_ROOTFS}/.version

	# Create content for release text file (eg. beta.txt, release.txt or nightly.txt)
	IMAGE_URL=${IMAGE_LOCATION_URL}/${IMAGE_NAME}_usb.zip
	if [ ! -z ${RELEASE_TEXT_LOCATION_FILE} ]; then
		if [ ! -d ${DEPLOY_DIR_IMAGE} ]; then
			mkdir -p ${DEPLOY_DIR_IMAGE}
		fi
		echo "${IMAGE_URL} ${VERSION_STAMP} MD5 ${RELEASE_NAME}-${IMAGE_DESCRIPTION} ${UPDATE_SERVER_URL}" >> ${RELEASE_TEXT_LOCATION_FILE}

		sed -i -e "s|@hostname@|${H}|g" "${RELEASE_TEXT_LOCATION_FILE}"
		sed -i -e "s|@build-dirname@|${BUILD_DIR}|g" "${RELEASE_TEXT_LOCATION_FILE}"
	fi

	# Create content for update.urls file and save it into rootfs
	UPDATE_URL_FILES=$(find ${DEPLOY_DIR_IMAGE} -type f -name "*.txt" -exec basename {} \; | sed 's|^|${IMAGE_LOCATION_URL}/|')
	echo "${UPDATE_URL_FILES}" > ${IMAGE_ROOTFS_ETC_DIR}/update.urls

	sed -i -e "s|@hostname@|${H}|g" "${IMAGE_ROOTFS_ETC_DIR}/update.urls"
	sed -i -e "s|@build-dirname@|${BUILD_DIR}|g" "${IMAGE_ROOTFS_ETC_DIR}/update.urls"
}

IMAGE_POSTPROCESS_COMMAND += "prepare_update_list_file; "
# We create md5 checksum and replace the MD5 place holder with generated checksum
prepare_update_list_file () {
	IMAGEFILE=${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}_usb.zip

	if [ -f "${IMAGEFILE}" ]; then
		CHECKSUM=$(md5sum ${IMAGEFILE} | awk '{ print $1 }')
		sed -i -e "s|MD5|${CHECKSUM}|" "${RELEASE_TEXT_LOCATION_FILE}"
	else
		bbwarn  "[${IMAGEFILE}] is not available or already built with last image and is up to date!"
	fi
}

KERNEL_MODULES_INSTALLED += ""

BIG_IMAGE_FILES = " \
	bash \
	bluez5 \
	bzip2 \
	cifs-utils \
	cronie \
	dvb-femon \
	exfat-utils \
	f2fs-tools \
	fbset \
	fuse-exfat \
	glibc-binary-localedata-en-us \
	glibc-binary-localedata-cs-cz \
	glibc-binary-localedata-de-de \
	glibc-binary-localedata-es-es \
	glibc-binary-localedata-fr-fr \
	glibc-binary-localedata-pl-pl \
	glibc-binary-localedata-ru-ru \
	glibc-charmap-utf-8 \
	grep \
	gzip \
	htop \
	image-config \
	imagemagick \
	kernel-modules \
	links \
	localedef \
	ntfs-3g \
	ntfsprogs \
	oh-my-zsh \
	parted \
	procps \
	proftpd \
	pulseaudio \
	sed \
	usbutils \
	util-linux \
	util-linux-fstrim \
	wpa-supplicant \
	zsh \
"

KERNEL_WIFI_DRIVERS = " \
	firmware-carl9170 \
	firmware-htc7010 \
	firmware-htc9271 \
	firmware-rt2870 \
	firmware-rt73 \
	firmware-rtl8712u \
	firmware-zd1211 \
	\
	kernel-module-ath9k-htc \
	kernel-module-carl9170 \
	kernel-module-r8712u \
	kernel-module-rt2500usb \
	kernel-module-rt2800usb \
	kernel-module-rt73usb \
	kernel-module-zd1211rw \
	"

EXTRA_KERNEL_WIFI_DRIVERS = " \
	firmware-rtl8192cu \
	\
	kernel-module-r8188eu \
	kernel-module-rtl8192cu \
	"

EXTERNAL_WIFI_DRIVERS = " \
	rtl8192cu \
	rt8188eu \
	rtl8192eu \
	"

PLUGIN_INSTALL = " \
	asc2uni \
	getrc \
	input \
	msgbox \
	shellexec \
	tuxcom \
"

PLUGIN_LUA_INSTALL = "	\
	ard-mediathek \
	logoupdater \
	luaplayer \
	mediathek \
	mtv \
	plutotv-vod \
	rss \
	settingsupdater \
	stb-backup \
	stb-flash \
	stb-flash-local \
	stb-restore \
	stb-startup \
	to-web-tv-xml \
	webradio \
	webtv \
	zdfhbbtv \
"

PLUGIN_INSTALL_append= " \
	${PLUGIN_LUA_INSTALL} \
"

IMAGE_INSTALL += " \
	${PLUGIN_INSTALL} \
	autofs \
	bc \
	ca-certificates \
	curl \
	dosfstools \
	e2fsprogs \
	e2fsprogs-resize2fs \
	findutils \
	flash-script \
	gptfdisk \
	hdparm \
	ifupdown \
	less \
	libswscale \
	libusb1 \
	minidlna \
	nano \
	nfs-utils \
	nfs-utils-client \
	ntpdate \
	ofgwrite \
	openssh \
	openssl \
	opkg \
	pv \
	rpcbind \
	rsync \
	samsunglcd4linux \
	tar \
	tzdata \
	tzdata-europe \
	udev-extraconf \
	udpxy \
	util-linux-blkid \
	util-linux-mount \
	util-linux-swaponoff \
	util-linux-umount \
	virtual/neutrino \
	virtual/screengrabber \
	xupnpd \
	zip \
"

## build and create packages, but do not install
EXTRA_IMAGEDEPENDS = " \
	lshw \
	samba \
	webmin \
	\
	browser \
	dropbear \
	etckeeper \
	ethtool \
	favorites-to-bin \
	heizoelpreise \
	logomask \
	myspass \
	netzkino \
	rcu-switcher \
	stb-log \
	stb-move \
	stb-shell \
	\
	autoreboot \
	epgscan \
	imgbackup \
	logomask \
	pr-auto-timer \
	replay \
	tuxcal \
	tuxmail \
	tuxwetter \
	\
	themes-ni \
	themes-tango \
"


EXTRAOPKGCONFIG = "neutrino-feed-config"

inherit image

do_package_index[nostamp] = "1"
do_package_index[depends] += "${PACKAGEINDEXDEPS}"

python do_package_index() {
    from oe.rootfs import generate_index_files
    generate_index_files(d)
}
addtask do_package_index after do_rootfs before do_image_complete
