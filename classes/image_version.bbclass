
# set release type, configured in local.conf
image_version_get_release_state() {
	RES=""
	if [ "${DISTRO_TYPE}" == "release" ]; then
		RES="0"
	elif [ "${DISTRO_TYPE}" == "beta" ]; then
		RES="1"
	elif [ "${DISTRO_TYPE}" == "nightly" ]; then
		RES="2"
	else
		RES="3"
	fi
	echo "$RES"
}

image_version_get_flavour_tag() {
	RES=""
	if [ "${FLAVOUR}" != "tuxbox" ]; then
		RES="${FLAVOUR}-flavour_"
	fi
	echo "$RES"
}

image_version_get_poky_version() {
	POKY_TAG=`git -C ${COREBASE} describe --abbrev=0`
	RES=`git -C ${COREBASE} rev-list $POKY_TAG..HEAD --count`
	echo "$RES"
}

image_version_get_meta_tuxbox() {
	RES="meta-neutrino"
	# In case of a changed repository name this should keeping compatibilty.
	# Why: meta-neutrino contains mostly recipes to create an image and
	# neutrino is only a part of image like all the other recipes.
	if [ -e ${COREBASE}/meta-tuxbox ]; then
		RES="meta-tuxbox"
	fi
	echo "$RES"
}

image_version_get_tuxbox_tag() {
	META_TUXBOX=`get_meta_tuxbox`
	RES=`git -C ${COREBASE}/$META_TUXBOX describe --abbrev=0`
	echo "$RES"
}

image_version_get_tuxbox_version() {
	META_TUXBOX=`get_meta_tuxbox`
	TUXBOX_TAG=`get_tuxbox_tag`
	RES=`git -C ${COREBASE}/$META_TUXBOX rev-list $TUXBOX_TAG..HEAD --count`
	echo "$RES"
}

image_version_get_meta_version() {
	# We extract the image version from 'git describe' content which are  primary provided by poky meta layers and
	# secondary from our image layer and the git tags are synchronized with our image layer.
	# The last git tag and the counted describe content will be added for our image version.
	META_TUXBOX_TAG=`get_tuxbox_tag`
	META_POKY_VERSION=`get_poky_version`
	META_TUXBOX_VERSION=`get_tuxbox_version`
	RES="$META_TUXBOX_TAG-$META_POKY_VERSION-$META_TUXBOX_VERSION"
	echo "$RES"
}

image_version_get_flavour_suffix() {
	RES=${IMAGE_FLAVOUR_TAG}v`get_meta_version`_`get_release_type`
	echo "$RES"
}

image_version_get_filename_prefix() {
	RES=${IMAGE_NAME}`get_flavour_suffix`
	echo "$RES"
}

image_version_get_filename_latest_prefix() {
	echo ${DISTRO_NAME}_${MACHINE}
}

image_version_get_yearly_tag() {
	echo `date '+%y'`
}

image_version_get_build_increment() {
	if test -f ${LOCAL_BUILD_INCREMENT_FILE}; then
		line=`sed -n '1p' ${LOCAL_BUILD_INCREMENT_FILE}`
		echo "$line"
	else
		image_build_increment_file=${TMPDIR}/image_build_increment
		if test ! -f $image_build_increment_file; then
			RES=0
			echo "$RES" > $image_build_increment_file
			echo "${IMAGE_NAME}" >> $image_build_increment_file
			echo "$RES"
		else
			line1=`sed -n '1p' $image_build_increment_file`
			line2=`sed -n '2p' $image_build_increment_file`
			if [ ${line2} != "${IMAGE_NAME}" ]; then
				RES=`expr $line1 + 1`
				echo "$RES" > $image_build_increment_file
				echo "${IMAGE_NAME}" >> $image_build_increment_file
				echo "$RES"
			else
				echo "$line1"
			fi
		fi
	fi
}

EXPORT_FUNCTIONS get_release_state  get_flavour_tag  get_poky_version  get_meta_tuxbox get_tuxbox_tag  get_tuxbox_version  get_meta_version  get_flavour_suffix  get_filename_prefix  get_filename_latest_prefix get_yearly_tag get_build_increment

export IMAGE_RELEASE_STATE="`get_release_state`"
export IMAGE_FLAVOUR_TAG="`get_flavour_tag`"
export IMAGE_YEARLY_TAG="`get_yearly_tag`"
export IMAGE_POKY_VERSION="`get_poky_version`"
export IMAGE_META_TUXBOX="`get_meta_tuxbox`"
export IMAGE_TUXBOX_TAG="`get_tuxbox_tag`"
export IMAGE_TUXBOX_VERSION="`get_tuxbox_version`"
export IMAGE_META_VERSION="`get_meta_version`"
export IMAGE_FLAVOUR_SUFFIX="`get_flavour_suffix`"
export IMAGE_FILE_NAME_PREFIX="`get_filename_prefix`"
export IMAGE_FILE_NAME_LATEST_PREFIX="`get_filename_latest_prefix`"
export IMAGE_BUILD_INCREMENT="`get_build_increment`"
