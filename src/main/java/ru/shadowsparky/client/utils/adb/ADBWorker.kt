/*
 * Created by shadowsparky in 2019
 */

@file:Suppress("UNREACHABLE_CODE")

package ru.shadowsparky.client.utils.adb

import ru.shadowsparky.client.utils.Extras
import ru.shadowsparky.client.utils.Extras.Companion.APP_SWITCH_BUTTON
import ru.shadowsparky.client.utils.Extras.Companion.BACK_BUTTON
import ru.shadowsparky.client.utils.Extras.Companion.HOME_BUTTON
import ru.shadowsparky.client.utils.Injection

class ADBWorker {
    private val log = Injection.provideLogger()
    private val executor = Injection.provideExecutor()

    fun forwardPort(device_id: String) : ADBResult {
        val result = executor.executeCommand(listOf("adb", "-s", device_id, "forward", "tcp:${Extras.FORWARD_PORT}", "tcp:${Extras.PORT}"))
        return baseEmptyChecking(result)
    }

    fun tapToScreen(x: Double, y: Double) : ADBResult {
        val result = executor.executeCommand(listOf("adb", "shell", "input", "tap", "$x", "$y"))
        return baseEmptyChecking(result)
    }

    private fun baseEmptyChecking(result: String) : ADBResult {
        if (result.isEmpty())
            return ADBResult(ADBStatus.OK)
        return ADBResult(ADBStatus.ERROR, result)
    }

    private fun baseNotEmptyChecking(result: String) : ADBResult {
        if (result.isNotEmpty())
            return ADBResult(ADBStatus.OK, result)
        return ADBResult(ADBStatus.ERROR)
    }

    fun invokeHomeButton() : ADBResult {
        return baseInvokeKeyEvent(HOME_BUTTON)
    }

    fun invokeBackButton() : ADBResult {
        return baseInvokeKeyEvent(BACK_BUTTON)
    }

    private fun baseInvokeKeyEvent(keycode: String) : ADBResult {
        val result = executor.executeCommand(listOf("adb", "shell", "input", "keyevent", keycode))
        return baseEmptyChecking(result)
    }

    fun invokeRecentApplicationsButton() : ADBResult {
        return baseInvokeKeyEvent(APP_SWITCH_BUTTON)
    }

    // TODO: Проверка на "битые" девайсы и существование adb
    fun getDevices() : ADBResult {
        val result = executor.executeCommand(listOf("adb", "devices", "-l"))
        return baseNotEmptyChecking(result)
    }
}