# android-aspectj
A plugin with aspectj for android projects

一款用于android项目的aspectj配置库，可以简化配置流程，支持第三方jar包以及kotlin代码。有良好的的兼容性。

使用方式如下 (注意gradle最低支持6.5版本，4.1.1)：

1、在项目的根目录下的build.gradle文件下加入jitpack maven地址

```groovy
allprojects {
    repositories {
        //....
        maven { url 'https://jitpack.io' }
    }
}
```

2、项目的根目录下的build.gradle文件得dependencies下加入依赖

```groovy
    dependencies {
        // ... 
        classpath 'com.github.tanmingwu:android-aspectj:1.0.0'
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
```

4、在App目录下的build.gradle文件加入plugin

```groovy
apply plugin: 'com.tmw.aspectj'
//或者
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
