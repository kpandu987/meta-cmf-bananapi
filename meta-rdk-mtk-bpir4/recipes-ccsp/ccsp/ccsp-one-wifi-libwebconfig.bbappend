FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_remove = "${CMF_GIT_ROOT}/rdkb/components/opensource/ccsp/OneWifi;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH};name=libwebconfig"

SRC_URI = "git://github.com/rdkcentral/OneWifi.git;protocol=https;branch=develop;name=libwebconfig"
SRCREV_libwebconfig = "b6f2c43f5b5a2fb9dcb827bcde8781f11fae2921"

SRC_URI_append = " file://Added_wifi_easymesh_translator.patch "

DEPENDS += " rdk-wifi-libhostap unified-wifi-mesh-header"

CFLAGS += " -Wno-error=maybe-uninitialized -Wno-error=unused-variable -Wno-error=unused-but-set-variable -Wno-error=incompatible-pointer-types -Wno-error=sign-compare -Wno-error -DEASY_MESH_NODE "

do_install_append() {
      install -m 644 ${S}/include/webconfig_external_proto_easymesh.h  ${D}/usr/include/ccsp
}
