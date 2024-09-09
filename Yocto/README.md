# Yocto Image Configuration for Running JavaFX Applications

## Table of Contents

- [Yocto Image Configuration for Running JavaFX Applications](#yocto-image-configuration-for-running-javafx-applications)
  - [Table of Contents](#table-of-contents)
  - [1.Introduction](#1introduction)
  - [2.Prerequisites](#2prerequisites)
  - [3. Layers And Firmware Configuration Added To The Yocto Project](#3-layers-and-firmware-configuration-added-to-the-yocto-project)
    - [1. General Configuration In `local.conf` inside the `build` directory](#1-general-configuration-in-localconf-inside-the-build-directory)
    - [2. Adding Required Layers](#2-adding-required-layers)
    - [3. Create A Custom Layer For The Application](#3-create-a-custom-layer-for-the-application)
      - [1. Machine Configuration](#1-machine-configuration)
      - [2. Distro Configuration](#2-distro-configuration)
      - [3.Image Configuration](#3image-configuration)
    - [4. Add JavaFX To The Image](#4-add-javafx-to-the-image)

## 1.Introduction

This document describes how to configure a Yocto image for running JavaFX applications in an infotainment system. The image will be tailored to support a complete GUI environment, including JavaFX for graphical user interfaces streaming web applications, and live location tracking.

## 2.Prerequisites

1. **Hardware**: raspberrypi4-64
2. **Software**:
   1. Yocto(in our case, we are using the kirkstone branch)

        ```cmd
        git clone --depth=1 https://github.com/yoctoproject/poky.git -b kirkstone
        ```

       >> If it was your first time using Yocto, you need to install the required dependencies. You can find the required dependencies in the [Yocto Project Quick Start](https://docs.yoctoproject.org/brief-yoctoprojectqs/index.html) guide.

## 3. Layers And Firmware Configuration Added To The Yocto Project

### 1. General Configuration In `local.conf` inside the `build` directory

First, Source `oe-init-build-env` inside `poky` to set up the environment variables.

After that, build directory will be created. Edit the `local.conf` file inside the `build` directory.

- Add Your Custom Distro and Machine Configuration

    ```Yocto
    DISTRO = "project-distro"
    MACHINE = "project-machine"
    ```

- For multi threading and faster build, add the following configuration

    ```Yocto
    BB_NUMBER_THREADS = "6"
    PARALLEL_MAKE = "-j 6"
    ```

    >> The number of threads should be less than the number of cores in your machine.
        -- To know the number of cores in your machine, use `nproc` command.

- For removing the old image before building a new one, add the following configuration

    ```Yocto
    RM_OLD_IMAGE = "1"
    ```



### 2. Adding Required Layers

- Find then clone the required layers at [Yocto Layers Index](https://layers.openembedded.org/) searching for the layer name setting the branch to `kirkstone` or whatever branch you are using.

- Add the layers to the `bblayers.conf` file inside the `build` directory or use the `bitbake-layers` command to add the layers.

    ```cmd
    bitbake-layers add-layer <path-to-layer>
    ```

    >> The order of the layers is important. The layers should be added in the correct order. If you want to edit there priority, you can edit `BBFILE_PRIORITY_meta-project` in the `conf/layer.conf` file of the layer.

The following layers are required for our application with their priorities:

```Yocto
$ bitbake-layers show-layers
NOTE: Starting bitbake server...
layer                 path                                      priority
==========================================================================
meta                  <path-to-layer>/meta            5
meta-poky             <path-to-layer>/meta-poky       5
meta-yocto-bsp        <path-to-layer>/meta-yocto-bsp  5
meta-oe               <path-to-layer>/meta-openembedded/meta-oe  5
meta-python           <path-to-layer>/meta-openembedded/meta-python  5
meta-networking       <path-to-layer>/meta-openembedded/meta-networking  5
meta-multimedia       <path-to-layer>/meta-openembedded/meta-multimedia  5
meta-project          <path-to-layer>/meta-project    22
meta-raspberrypi      <path-to-layer>/meta-raspberrypi  13
meta-ffmpeg4          <path-to-layer>/meta-ffmpeg4    13
```

### 3. Create A Custom Layer For The Application

- Create a custom layer for the application wherever you want but it is recommended to gather all your custom and cloned recipes in one place then add it.

    ```cmd
    bitbake-layers create-layer <path-to-layers>/meta-project
    ```

    >> This layer is the one added with the highest priority in the `bblayers.conf` file previously mentioned.

- Layer structure

    ```tree
    .
    ├── conf
    │   ├── distro
    │   │   └── project-distro.conf
    │   ├── layer.conf
    │   └── machine
    │       └── project-machine.conf
    ├── COPYING.MIT
    └── recipes-core
        ├── base-files
        │   └── base-files_%.bbappend
        └── images
            └── project-image.bb

#### 1. Machine Configuration

```Yocto
# Extending the name only
MACHINEOVERRIDES =. "raspberrypi4-64:${MACHINE}"
# inherit the raspberrypi4-64 configuration from the meta-raspberrypi layer
require conf/machine/raspberrypi4-64.conf

ENABLE_UART = "1"
DISABLE_RPI_BOOT_LOGO = "1"
DISABLE_SPLASH = "1"

MACHINE_FEATURES:append = " \
    sound \
    bluetooth \
    wifi \
    vc4graphics \
"
```


#### 2. Distro Configuration

```Yocto
# General Configuration
require conf/distro/poky.conf

DISTRO = "project-distro"
DISTRO_NAME = "project Distro Version 1.0"
DISTRO_VERSION = "1.0"
BOOTDD_VOLUME_ID = "boot"

# Distro Features
# Remove unnecessary features
DISTRO_FEATURES:remove = "ext2 3g nfc"
# Add essential features
DISTRO_FEATURES:append = " wifi bluetooth ext4 bluez bluez5 python3-bluez pcmcia usbhost usbgadget pulseaudio pi-bluetooth linux-firmware-bcm43430 systemd usrmerge ipv4"

# Init Manager Configuration
INIT_MANAGER = "systemd"

# Autoload sound module for Raspberry Pi
KERNEL_MODULE_AUTOLOAD:rpi = "snd-bcm43455"

# Install X11 server, drivers, terminal, and window manager
IMAGE_INSTALL:append = " xserver-xorg xf86-video-fbdev xf86-input-evdev xterm matchbox-wm"

# Install OpenSSH for remote login and file transfers
TASK_BASIC_SSHDAEMON = "openssh-sshd openssh-sftp openssh-sftp-server"
```

#### 3.Image Configuration

```Yocto
inherit core-image
include recipes-sato/images/core-image-sato.bb

IMAGE_ROOTFS_SIZE = "10485760"
IMAGE_FSTYPES = "wic"

CXXFLAGS:remove = " -fno-implicit-templates -Wall -Wextra -Wstrict-prototypes -Wmissing-prototypes -Werror"

# Remove unnecessary SSH-dropbear packages
IMAGE_FEATURES:remove = " core-ssh-dropbear ssh-server-dropbear packagegroup-core-ssh-dropbear connman-gnome"

# Base image packages
IMAGE_INSTALL += " strace packagegroup-core-boot nano vim util-linux busybox matchbox-keyboard"
IMAGE_INSTALL:append = " python3 bash git python3-pip make cmake wpa-supplicant rpm"

# Flask dependencies
IMAGE_INSTALL:append = " python3-flask python3-flask-socketio"

# Network-related packages
IMAGE_INSTALL:append = " gpsd gpsd-client libgps gps-msgs bluez5 gps-utils jq"

# Multimedia and sound packages
PACKAGECONFIG:pn-qtmultimedia = " gstreamer alsa"
IMAGE_INSTALL:append = " gstreamer1.0 gstreamer1.0-omx gstreamer1.0-plugins-good gstreamer1.0-plugins-base gstreamer1.0-plugins-ugly gstreamer1.0-libav mpg123 pulseaudio \
    pulseaudio-module-dbus-protocol pulseaudio-server pulseaudio-module-bluetooth-discover \
    pulseaudio-module-bluetooth-policy pulseaudio-module-bluez5-device pulseaudio-module-bluez5-discover \
    alsa-utils alsa-lib alsa-plugins alsa-tools alsa-state dbus ffmpeg4"

# Raspberry Pi and sound support
DISTRO_FEATURES:append = " pulseaudio"
KERNEL_MODULE_AUTOLOAD:rpi = " snd-bcm2835"
MACHINE_FEATURES:append = " sound"

# USB support
ENABLE_DWC2_PERIPHERAL = "1"
ENABLE_DWC2_HOST = "1"

# Bluetooth and WiFi firmware
IMAGE_INSTALL:append = " bluez5 i2c-tools bridge-utils hostapd iptables pi-bluetooth bluez5-testtools udev-rules-rpi \
    linux-firmware iw kernel-modules linux-firmware-ralink linux-firmware-rtl8192ce \
    linux-firmware-rtl8192cu linux-firmware-rtl8192su linux-firmware-rpidistro-bcm43430 linux-firmware-bcm43430"

# Networking tools
IMAGE_INSTALL:append = " dhcpcd connman connman-client openssh"

# Splash screen support
IMAGE_INSTALL:append = " psplash psplash-raspberrypi"

# Core utilities
IMAGE_INSTALL:append = " coreutils"

# Video support
DISTRO_FEATURES:append = " vc4graphics"
```

### 4. Add JavaFX To The Image

Encountered a problem with JavaFX, as it is not included in the Yocto repository. To solve this problem, we need to add the JavaFX SDK to the Yocto image manually after building the image.

- Download the JavaFX SDK from the [OpenJFX azul](https://www.azul.com/downloads/?version=java-22&os=linux&architecture=arm-64-bit&package=jdk-fx#zulu).
- Download Java SE Development Kit [Linux Arm 64 RPM Package](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- Transfer the downloaded files to the Raspberry Pi.
- Install the Java SE Development Kit on the Raspberry Pi.

    ```cmd
    sudo rpm -i jdk-17.0.2_linux-aarch64_bin.rpm
    ```

- Extract the JavaFX SDK to the Raspberry Pi.

    ```cmd
    sudo tar -xvf openjfx-17.0.2_linux-aarch64_bin-sdk.zip -C /usr/lib/jvm/
    ```

- use JavaFX to run the JavaFX application on the Raspberry Pi.

    ```cmd
    java --module-path "$JAVAFX_HOME/lib" --add-modules javafx.base,javafx.controls,javafx.graphics,javafx.fxml,javafx.media,javafx.web -jar your-app.jar
    ```

