FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " \
           file://no-docs.patch \
           file://processcreds.patch \
           "
