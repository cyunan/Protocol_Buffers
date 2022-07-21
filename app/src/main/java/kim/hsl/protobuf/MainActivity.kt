package kim.hsl.protobuf

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.alibaba.fastjson.JSON
import com.example.tutorial.protos.AddressBook
import com.example.tutorial.protos.Person
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    companion object{
        val TAG = "MainActivity"
        private const val REQUEST_EXTERNAL_STORAGE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermission()
        // 测试 Protobuf 性能
        protobufTest()

        // 测试 Json 性能
        JsonTest().jsonTest()
        parseDependencies()
        parseBundleConfig()
    }

    fun protobufTest(){
        // 创建 Person.PhoneNumber.Builder 对象
        var phoneNumber1Builder: Person.PhoneNumber.Builder =
            Person.PhoneNumber.newBuilder().
            setNumber("666")

        // 创建 Person.Builder 对象
        var person1Builder: Person.Builder =
            Person.newBuilder().
            setName("Tom").
            setId(0).
            addPhones(phoneNumber1Builder)


        // 创建 Person.PhoneNumber.Builder 对象
        var phoneNumber2Builder: Person.PhoneNumber.Builder =
            Person.PhoneNumber.newBuilder().
            setNumber("888")

        // 创建 Person.Builder 对象
        var person2Builder: Person.Builder =
            Person.newBuilder().
            setName("Jerry").
            setId(1).
            addPhones(phoneNumber2Builder)


        // 使用 newBuilder 方法创建 AddressBook.Builder 对象
        var addressBookBuilder: AddressBook.Builder =
            AddressBook.newBuilder().
            addPeople(person1Builder).
            addPeople(person2Builder)

        // 将上述各个 Builder 拼装完毕后 , 最后调用 build
        // 即可得到最终对象
        var addressBook: AddressBook = addressBookBuilder.build()

        // 序列化操作
        var serializeStart = System.currentTimeMillis()

        // 将 addressBook 对象转为字节数组
        var bytes: ByteArray = addressBook.toByteArray()

        Log.i(TAG, "序列化耗时 ${System.currentTimeMillis() - serializeStart} ms , " +
                "序列化大小 ${bytes.size} 字节")


        // 反序列化操作
        var deserializeStart = System.currentTimeMillis()

        var deserializeAddressBook: AddressBook = AddressBook.parseFrom(bytes)

        Log.i(TAG, "反序列化耗时 ${System.currentTimeMillis() - deserializeStart} ms")
    }


    private fun requestPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 先判断有没有权限
            if (Environment.isExternalStorageManager()) {
                //自动获取权限
                autoObtainLocationPermission()
            } else {
                //跳转到设置界面引导用户打开
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.data = Uri.parse("package:" + getPackageName())
                startActivityForResult(intent, REQUEST_EXTERNAL_STORAGE)
            }
        } else {
            //自动获取权限
            autoObtainLocationPermission()
        }
    }

    private fun autoObtainLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                100)
        }
    }

}

