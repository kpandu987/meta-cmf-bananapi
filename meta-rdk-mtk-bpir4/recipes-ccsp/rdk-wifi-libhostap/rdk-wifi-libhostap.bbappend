FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append = " file://Bpi_rdkwifilibhostap_changes.patch "
SRC_URI_append = " file://wpa3_compatibility_hostap_2_10.patch "
SRC_URI_append = " file://fix_wpa3_sae_pt.patch "

CFLAGS_append = " -D_PLATFORM_BANANAPI_R4_"
