/*
 * Created by shadowsparky in 2019
 *
 */

package ru.shadowsparky.client.mvc.models

import ru.shadowsparky.client.utils.adb.ADBDevice
import ru.shadowsparky.client.utils.adb.ADBStatus
import ru.shadowsparky.client.utils.objects.Injection
import ru.shadowsparky.client.utils.objects.Parser

class AdbModel {
    private val adb = Injection.provideAdb()


    fun getDevicesRequest() : ArrayList<ADBDevice>? {
        val request = adb.getDevices()
        if (request.status == ADBStatus.OK) {
            return Parser.strToDevices(request.info)
        }
        return null
    }

    fun getDevice(device: String) = Parser.deviceToStr(device)
}