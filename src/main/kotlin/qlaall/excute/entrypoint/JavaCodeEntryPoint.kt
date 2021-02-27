package qlaall.excute.entrypoint

import io.ktor.util.*

class JavaCodeEntryPoint {
    private val javaCodeClassLoader:ClassLoader= object : ClassLoader(){}
    fun loadJavaCode(code:String){
        //加载指定的功能代码
//        javaCodeClassLoader.loadClass()
    }
}