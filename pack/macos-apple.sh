#!/bin/zsh

if ! command -v create-dmg &> /dev/null; then
    echo '打包失败，因为未安装 create-dmg，请先按照以下步骤安装 create-dmg：'
    echo '1. 安装 Homebrew，具体方法请自行搜索'
    echo '2. 运行命令：brew install create-dmg'
    exit
fi

appIdentifier='com.main.Main'
appName='文件分割与合并工具'
appDisplayName='文件分割与合并工具'
appVersion=$(awk -F'[><]' '/<version>/{print $3; exit}' '../pom.xml')
appShortVersion=$(awk -F'[><]' '/<version>/{print $3; exit}' '../pom.xml')
dir='../target/macos-apple-silicon'
appDir=$dir'/'$appDisplayName'.app'
dmgDir=$dir'/'$appDisplayName'.dmg'

cd "$(dirname "$0")"
if [ -d $appDir ]; then
    rm -r $appDir
fi
mkdir -p $appDir'/Contents/Resources'
cp '../src/main/resources/icon.icns' $appDir'/Contents/Resources'
mkdir -p $appDir'/Contents/MacOS'
cp '../target/gluonfx/aarch64-darwin/file-split-and-merge-tool' $appDir'/Contents/MacOS/'
touch $appDir'/Contents/Info.plist'
echo '<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
    <key>CFBundleIdentifier</key>
    <string>'$appIdentifier'</string>
    <key>CFBundleName</key>
    <string>'$appName'</string>
    <key>CFBundleDisplayName</key>
    <string>'$appDisplayName'</string>
    <key>CFBundleVersion</key>
    <string>'$appVersion'</string>
    <key>CFBundleShortVersionString</key>
    <string>'$appShortVersion'</string>
    <key>CFBundleIconFile</key>
    <string>icon.icns</string>
    <key>CFBundleExecutable</key>
    <string>file-split-and-merge-tool</string>
</dict>
</plist>
' > $appDir'/Contents/Info.plist'
create-dmg --volname $appDisplayName \
           --volicon '../src/main/resources/icon.icns' \
           --window-pos 200 120 \
           --window-size 800 400 \
           --icon-size 100 \
           --icon $appDisplayName'.app' 200 190 \
           --hide-extension $appDisplayName'.app' \
           --app-drop-link 600 185 \
           $dmgDir \
           $appDir
