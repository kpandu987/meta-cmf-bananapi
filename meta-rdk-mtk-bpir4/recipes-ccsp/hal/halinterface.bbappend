FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append = " file://halinterface_build_errors_fixes.patch;apply=no"

CFLAGS_append = " -DWIFI_HAL_VERSION_3"

do_bpi_patches() {
    cd ${S}
    if [ ! -e bpi_patch_applied ]; then
        bbnote "Patching halinterface_build_errors_fixes.patch"
        patch -p1 < ${WORKDIR}/halinterface_build_errors_fixes.patch
        touch bpi_patch_applied
    fi
}

addtask bpi_patches after do_unpack before do_configure
