# AndroidAspectj
A plugin with aspectj for android projects
一款Android Aspectj的配置库，可以将配置流程，支持第三方jar包

由于没有上传maven仓库，所以需要自己clone下来引入自己本地工程。整个插件是用kotlin开发的

使用方式如下：

1、将tmw-aspectj整个目录拷贝到工程下，并且编译通过，执行uploadArchives task就能在部署在repo下了

2、在项目的根目录下的build.gradle文件下加入本地maven仓库地址

3、项目的根目录下的build.gradle文件得dependencies下加入

```kotlin
    dependencies {
        classpath("com.tmw.plugin:tmw-aspectj:1.1.0")
    }
```

4、在App目录下的build.gradle文件加入plugin

```kotlin
plugins {
    id("com.tmw.aspectj")
}
```

5、如果其它模块也需要，按步骤4即可

6、自定义配置项，尽量使用include，能极大的提升编译速度

```groovy
aspectj {
    enable true
    include "androidx.appcompat"
}
```
