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



