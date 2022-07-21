package kim.hsl.protobuf

import android.content.Context
import android.os.Environment
import android.util.Log
import com.example.tutorial.dependencies.AppDependenciesOuterClass
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


/**
 *    author : ChenYuNan
 *    date   : 2022/7/21
 *    desc   :
 */

fun handleParse(){
}
fun parseDependencies() {
    val path = "/storage/emulated/0/aab/dependencies.pb"
    val file = File(path)
    val fileInputStream = FileInputStream(file)
    val dependencies = AppDependenciesOuterClass.AppDependencies.parseFrom(fileInputStream)
    saveLog(dependencies.toString(), "log")
    Log.i(MainActivity.TAG, "pb反序列化 ${dependencies.libraryList}")
}

fun saveLog(message: String, fileName: String) {
    val path = Environment.getExternalStorageDirectory().toString() + "/MyLog"
    val files = File(path)
    if (!files.exists()) {
        files.mkdirs()
    }
    if (files.exists()) {
        try {
            val fw = FileWriter(
                path + File.separator
                    .toString() + fileName + ".txt"
            )
            fw.write(
                """
                    $message
                    
                    """.trimIndent()
            )
            fw.write("\n")
            fw.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
