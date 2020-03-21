用于学习组件化框架
==
*项目说明*
--
## 总体框架 ##
1. View与Model通信采用了MVVM模式
2. 采用了组件化的架构方案
## 模块说明
* **opensource:**  
主要是Google开源的一些依赖库与组件，内部不做任何实现只是为了统一管理开源库

* **third-partylibraries:**  
主要是第三方的库，内部不做任何实现只是为了统一管理第三方库

* **common:**  
一些通用的基础件实现和一些共用的环境整合，比如Them，color，drawable，bitmap等

* **module-common:**  
该模块实现了MVVM模式和完成了基类的定义，所有组件将依赖该模块进行开发

## 配置说明
1. **本地仓库配置**  
    * *配置本地仓库路径，分别修改 build.gradle和local.repository.gradle文件*  
    ```
    build.gradle文件:
    allprojects {
        repositories {
            maven{ url 'file://H://repository/'}//本地仓库路径
            jcenter()
            mavenCentral()
            maven { url 'https://maven.google.com' }
            google()
        }
    }
    
    local.repository.gradle文件：
    uploadArchives{
        repositories.mavenDeployer{
            repository(url:"file://H://repository/")//本地仓库路径
            pom.groupId = "com.xi.wj"
        }
    }
    ```
2. **调试开关**  
*gradle.properties* 文件中的 **isModule** 定义了是否启用单独组件调试  

        isModule=true //开启组件单独调试编译  
        isModule=false //关闭组件单独调试编译

3. **组件配置**
    * *gradle配置*  
    应用公共的文件*module.build.gradle*  
    应用公共的文件*local.repository.gradle*  
    ```
     apply from: "../module.build.gradle"
     apply from: "../local.repository.gradle"
    ```
    * *将组件推送到本地仓库*  
    ```
    uploadArchives{
        repositories.mavenDeployer{
            pom.artifactId = "sign"
            pom.version = "1.0.0"
        }
    }
    ```