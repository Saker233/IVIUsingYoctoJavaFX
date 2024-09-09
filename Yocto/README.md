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

