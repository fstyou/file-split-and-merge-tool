# 文件分割与合并工具

一个小工具，可以分割和合并文件，基于 Java & Gluon。

## 使用场景

- 传输文件时有大小限制，无法一次传输一个大文件；
- 微信传输大于 1GiB 的文件；
- 一次传输一个大文件的效率太低，将大文件拆开一起发送可以提高效率 ~~（手动多线程）~~；
- 等等。

## 项目预览

![首页](res/img/1.png)  
![文件分割](res/img/2.png)  
![文件合并](res/img/3.png)

## 下载地址

[从 GitLab 下载](https://gitlab.com/fstyou/file-split-and-merge-tool/-/releases/permalink/latest)  
[从 GitHub 下载](https://github.com/fstyou/file-split-and-merge-tool/releases/latest)

## 开发

本项目基于 [Gluon](https://gluonhq.com/)，下面的步骤参考 [Gluon 文档](https://docs.gluonhq.com/)，如遇到问题请阅读文档

### 环境配置

1. 下载 Java 21 的 [GraalVM](https://www.graalvm.org/downloads)，并配置系统环境变量：
    - `GRAALVM_HOME`：GraalVM 目录
    - `JAVA_HOME`：macOS/Linux 设为 `$GRAALVM_HOME`，Windows 设为 `%GRAALVM_HOME%`
    - `PATH`：macOS/Linux 设为 `$JAVA_HOME\bin;$PATH`，Windows 设为 `%JAVA_HOME%\bin;%PATH%`
2. 下载并安装 [JDK 11](https://www.oracle.com/java/technologies/downloads/#java11)
3. （仅对于 Windows）下载 [Maven 3.8.8](https://dlcdn.apache.org/maven/maven-3/3.8.8/binaries/apache-maven-3.8.8-bin.zip)
   ，并配置系统环境变量：
    - `MVN_HOME`：解压的目录
    - `PATH`：`%MVN_HOME%\bin;%PATH%`
4. （仅对于 Windows）下载 [Visual Studio](https://visualstudio.microsoft.com/zh-hans/downloads/) 2022，选择单个组件，勾选下面的组件：
    - MSVC v143 -VS 2022 C++ x64/x86 生成工具
    - 对 v143 生成工具的 C++/CLI 支持
    - Windows 10 SDK
    - Windows 通用 CRT SDK
   
   选择语言包，勾选且仅勾选英语，开始安装
   
   配置系统环境变量：
    - `VS_HOME`：Visual Studio 2022 安装目录
    - `PATH`：`%VS_HOME%\VC\Tools\MSVC\<MSVC版本>\bin\Hostx64\x64;%PATH%\`
5. （仅对于 macOS）安装 [Homebrew](https://brew.sh/)，并执行 `brew install create-dmg`
6. 使用 [IntelliJ IDEA](https://www.jetbrains.com/idea/) 打开本项目，在文件->项目结构->SDK中添加 JDK 11，并设置项目 SDK 为 JDK 11

### 运行

打开 IntelliJ IDEA 中的 Maven 面板，运行：`file-split-and-merge-tool/插件/javafx/javafx:run`

### 打包

#### macOS Apple Silicon

1. 打开 IntelliJ IDEA 中的 Maven 面板，运行：`file-split-and-merge-tool/插件/gluonfx/gluonfx:build`
2. 运行 `pack/macos-apple.sh`
3. 打包后的文件在 `target/macos-apple-silicon/` 目录下

#### Windows 10 x64

1. 运行 `pack/windows-10-x64.bat`
2. 使用 Inno Setup 运行 `windows-10-x64.iss`
3. 打包后的文件在 `target/windows-10-x64/` 目录下
