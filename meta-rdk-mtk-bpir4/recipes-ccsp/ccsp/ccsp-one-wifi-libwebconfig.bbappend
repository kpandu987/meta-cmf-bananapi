#Below changes needs to be removed once it's merged in meta-rdk-broadband
MATH_UTILS_COMPILE = "${@bb.utils.contains('DISTRO_FEATURES', 'BuildFromTip', '1', '0', d)}"

do_compile:append() {
   if [ "${MATH_UTILS_COMPILE}" = "1" ]; then
      oe_runmake -C source/utils/math_utils
   fi
}

do_install:append() {
   if [ "${MATH_UTILS_COMPILE}" = "1" ]; then
      oe_runmake -C source/utils/math_utils DESTDIR=${D} install
      install -d ${D}/usr/include/ccsp/math_utils
      install -m 644 ${S}/include/run_qmgr.h       ${D}/usr/include/ccsp
      cp -r ${S}/source/utils/math_utils/inc/* ${D}/usr/include/ccsp/math_utils/
   fi
}
