SUMMARY = "MySQL database connector for C++"
DESCRIPTION = "MySQL Connector/C++ provides a C++ API for connecting client applications \
to the MySQL Server. It is designed for ease of use and familiar coding style \
for C++ developers."

HOMEPAGE = "https://dev.mysql.com/downloads/connector/cpp/"
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://Licenses_for_Third-Party_Components.txt;md5=b2cee022e19f3b50d086e8570b50376a"

PV = "1.1.9"

DEPENDS = "boost openssl mariadb protobuf protobuf-native zlib"

inherit cmake

# Add compiler and linker configurations
TOOLCHAIN_OPTIONS += "-Wl,-rpath-link,${STAGING_LIBDIR}"

# Add proper compiler and linker flags
TARGET_CFLAGS += "--sysroot=${STAGING_DIR_TARGET}"
TARGET_CXXFLAGS += "--sysroot=${STAGING_DIR_TARGET}"
TARGET_LDFLAGS += "--sysroot=${STAGING_DIR_TARGET}"

TARGET_CFLAGS += "-I${STAGING_DIR_TARGET}/usr/include/mysql/mysql -fpermissive "
TARGET_CXXFLAGS += "-I${STAGING_DIR_TARGET}/usr/include/mysql/mysql -fpermissive "
TARGET_CFLAGS += "-I${STAGING_DIR_TARGET}/usr/include/mysql/server "
TARGET_CXXFLAGS += "-I${STAGING_DIR_TARGET}/usr/include/mysql/server "
#CMAKE_VERBOSE = "VERBOSE=1"

SRC_URI = "https://downloads.mysql.com/archives/get/p/20/file/mysql-connector-c++-${PV}.tar.gz"
SRC_URI[sha256sum] = "3e31847a69a4e5c113b7c483731317ec4533858e3195d3a85026a0e2f509d2e4"
S = "${WORKDIR}/mysql-connector-c++-${PV}"

EXTRA_OECMAKE += " \
    -DCMAKE_INSTALL_PREFIX=${prefix} \
    -DINSTALL_LIB_DIR=${baselib} \
    -DINSTALL_INCLUDE_DIR=${includedir} \
    -DWITH_SSL=system \
    -DMYSQL_INCLUDE_DIR=${STAGING_INCDIR}/mysql \
    -DWITH_BOOST=${STAGING_INCDIR}/boost \
    -DBUILD_STATIC=OFF \
    -DBUILD_SHARED=ON \
    -DWITH_PROTOBUF=system \
    -DProtobuf_INCLUDE_DIR=${STAGING_INCDIR} \
    -DProtobuf_LIBRARY=${STAGING_LIBDIR}/libprotobuf.so \
    -DProtobuf_PROTOC_EXECUTABLE=${STAGING_BINDIR_NATIVE}/protoc \
    -DMYSQLCLIENT_STATIC_LINKING=OFF \
    -DCMAKE_C_FLAGS='${CFLAGS}' \
    -DCMAKE_CXX_FLAGS='${CXXFLAGS}' \
    -DCMAKE_EXE_LINKER_FLAGS='${LDFLAGS}' \
    -DCMAKE_SKIP_RPATH=ON \
"

# Fix for parallel build issues
PARALLEL_MAKE = ""
CXXFLAGS=" -std=c++14 -fpermissive "
# Specify the proper toolchain file
EXTRA_OECMAKE += "-DCMAKE_TOOLCHAIN_FILE=${WORKDIR}/toolchain.cmake"

# Create toolchain file
do_configure:prepend() {
    cat > ${WORKDIR}/toolchain.cmake <<EOF
    set(CMAKE_SYSTEM_NAME Linux)
    set(CMAKE_SYSTEM_VERSION 3.22.3)
    set(CMAKE_SYSTEM_PROCESSOR aarch64)

    set(CMAKE_SYSROOT ${STAGING_DIR_TARGET})
    set(CMAKE_FIND_ROOT_PATH ${STAGING_DIR_TARGET})

    set(CMAKE_C_COMPILER ${CC})
    set(CMAKE_CXX_COMPILER ${CXX})

    set(CMAKE_FIND_ROOT_PATH_MODE_PROGRAM NEVER)
    set(CMAKE_FIND_ROOT_PATH_MODE_LIBRARY ONLY)
    set(CMAKE_FIND_ROOT_PATH_MODE_INCLUDE ONLY)
    set(CMAKE_FIND_ROOT_PATH_MODE_PACKAGE ONLY)

    set(CMAKE_C_FLAGS "${CFLAGS} ${TOOLCHAIN_OPTIONS}")
    set(CMAKE_CXX_FLAGS "${CXXFLAGS} ${TOOLCHAIN_OPTIONS}")
    set(CMAKE_EXE_LINKER_FLAGS "${LDFLAGS} ${TOOLCHAIN_OPTIONS}")
EOF
}

do_install_append() {
       rm ${D}/usr/README ${D}/usr/COPYING ${D}/usr/Licenses_for_Third-Party_Components.txt ${D}${libdir}/*.a
}

FILES:${PN} = " \
    ${libdir}/lib*.so.* \
    ${includedir}/mysql/* \
    ${includedir}/* \
    ${includedir}/cppconn/* \
"

FILES:${PN}-dev = " \
    ${includedir}/mysql/* \
    ${includedir}/mysqlx/* \
    ${libdir}/lib*.so \
    ${libdir}/cmake/* \
"

FILES:${PN}-dbg += "${libdir}/.debug/lib*"

PACKAGES = "${PN} ${PN}-dev ${PN}-dbg"

RDEPENDS:${PN} += "openssl mariadb protobuf zlib"

BBCLASSEXTEND = "native nativesdk"

COMPATIBLE_HOST = '(x86_64|i.86|arm|aarch64).*-linux'
