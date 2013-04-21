android-muti-channel-pack-tools
===============================

android多渠道打包工具，支持工程引用library project
注：此工具仅支持windows
运行环境：需安装java，ant和android sdk，同时配置相关环境变量，保证在命令行下命令java，ant，android，aaapt，jarsigner，zipalign均可以执行
使用方法：
	1. 将app-root下的文件复制到你的app工程根目录下，
	2. 修改channels.txt添加你需要得渠道
	3. 修改keystore.bat填写签名相关信息
	4. 在你的AndroidManifest.xml文件中添加待替换的渠道标识，比如使用友盟的话添加<meta-data android:name="UMENG_CHANNEL" android:value="channel_name" />
	   工具在运行的过程中会将channel_name替换成你在channels.txt中定义的渠道
	5. 在命令行模式下进入到app工程根目录，输入main，根据你的工程的复杂程度和渠道的数量，可能需要等待一段时间
	6. 命令执行完成后，可以到apk-release目录下查看多渠道安装包
	7. 以后每次需要多渠道安装包时，只需要做第5，6步即可
执行过程：
	1. 执行build.jar，生成build.bat文件
	2. 执行build.bat，生成各个工程（包括主工程和库工程）的build.xml文件
	3. 执行channel.jar修改主工程的AndroidManifest.xml文件，替换相应的渠道号
	4. 使用命令ant release进行打包
	5. 使用jarsigner进行签名
	6. 使用zipalign进行优化apk