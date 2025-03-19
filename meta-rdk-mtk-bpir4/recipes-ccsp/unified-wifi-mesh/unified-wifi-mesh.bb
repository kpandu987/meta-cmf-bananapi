SUMMARY = "Unified-wifi-mesh"
HOMEPAGE = "http://github.com/rdkcentral/unified-wifi-mesh"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e0b1ae637439c7d6f4487fb90163c79a"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI = "git://github.com/rdkcentral/unified-wifi-mesh.git;branch=main;protocol=https;name=Unified-wifi-mesh"
PV = "git${SRCPV}"
SRCREV_Unified-wifi-mesh = "cd6a907112b4e7b65a0938afc020a1a87e9b406c"
#SRCREV_Unified-wifi-mesh = "${AUTOREV}"
SRCREV_FORMAT = "Unified-wifi-mesh"

SRC_URI += "git://github.com/rdkcentral/OneWifi.git;branch=develop;protocol=https;name=OneWifi;destsuffix=git/OneWifi"
SRCREV_OneWifi = "${AUTOREV}"

SRC_URI += " file://ctrl_agent_compilation_support.patch"
S = "${WORKDIR}/git"

DEPENDS = " ccsp-one-wifi rbus halinterface mariadb mysql-connector-cpp "
DEPENDS += "gcc-sanitizers"

inherit autotools pkgconfig systemd

CPPFLAGS_append = " \
    -I${STAGING_INCDIR} \
    -I${STAGING_INCDIR}/rbus \
    -I${STAGING_INCDIR}/ccsp \
    -I${STAGING_INCDIR}/dbus-1.0 \
    -I${STAGING_LIBDIR}/dbus-1.0/include \
"
CPPFLAGS_append = " -g -DEASY_MESH_NODE -DWIFI_HAL_VERSION_3 "

LDFLAGS_append = " \
    -lm \
    -lcjson \
    -lpthread \
    -ldl \
    -luuid \
    -lssl \
    -lcrypto \
    -lrbus \
    -lmysqlcppconn \
"
do_bpi_align() {
     cd ${S}
     mkdir src/cli/libs
     mv src/cli/*.cpp src/cli/libs
     cd -
}

addtask bpi_align after do_unpack before do_patch

do_install_append() {
    install -d ${D}/usr/include/ccsp
    install -d ${D}/usr/ccsp/EasyMesh
    install -m 644 ${S}/inc/*  ${D}/usr/include/ccsp
    install -m 644 ${S}/install/bin/*  ${D}/usr/ccsp/EasyMesh
}

FILES_${PN} += "${libdir}/*.so* /usr/include/ccsp/* ${bindir}/* /usr/ccsp/EasyMesh/* "
