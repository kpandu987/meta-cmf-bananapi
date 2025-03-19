SUMMARY = "Unified-wifi-mesh for cli "
HOMEPAGE = "http://github.com/rdkcentral/unified-wifi-mesh"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${WORKDIR}/src/import/LICENSE;md5=e0b1ae637439c7d6f4487fb90163c79a"

SRC_URI = "git://github.com/rdkcentral/unified-wifi-mesh.git;branch=main;protocol=https;name=Unified-wifi-mesh-cli"
PV = "git${SRCPV}"
SRCREV_Unified-wifi-mesh-cli = "${AUTOREV}"
SRCREV_FORMAT = "Unified-wifi-mesh-cli"

GO_IMPORT = "import"

S = "${WORKDIR}/git"

inherit goarch
inherit go

DEPENDS = " readline ccsp-one-wifi ccsp-one-wifi-libwebconfig unified-wifi-mesh go "
EXTRA_OEMAKE = "GO='${GO}'"

CFLAGS_append = " \
    -I${STAGING_INCDIR} \
    -I${STAGING_INCDIR}/ccsp \
    -I=${includedir}/rbus \
    -I${STAGING_INCDIR}/dbus-1.0 \
    -I${STAGING_LIBDIR}/dbus-1.0/include \
"
CFLAGS_append = " -g -DEASY_MESH_NODE -fPIC "

LDFLAGS_append = " -lemcli "

do_compile() {
	export GOARCH="${TARGET_GOARCH}"
	export GOROOT="${STAGING_LIBDIR}/go"

	export GOPATH="${S}"
        export GO111MODULE=on 
        export GOPROXY='https://proxy.golang.org,direct' 
	# Pass the needed cflags/ldflags so that cgo
	# can find the needed headers files and libraries
	export CGO_ENABLED="1"
	export CFLAGS=""
	export LDFLAGS=""
	export CGO_CFLAGS="${TARGET_CFLAGS} ${CFLAGS}"
	export CGO_LDFLAGS="${TARGET_LDFLAGS} ${LDFLAGS}"
         
        cd ${S}/src/import/src/cli
        go get -a
	oe_runmake build 
        cd -
}

do_install() {
        install -d ${D}/usr/bin
        install -m 755 ${S}/src/import/src/cli/onewifi_em_cli  ${D}/usr/bin   
}

FILES_${PN} += " ${bindir}/* "

