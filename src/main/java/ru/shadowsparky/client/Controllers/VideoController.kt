package ru.shadowsparky.client.Controllers

import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.image.Image
import javafx.scene.input.MouseEvent
import javafx.scene.layout.*
import javafx.stage.Stage
import javafx.stage.WindowEvent
import ru.shadowsparky.client.Client.Client
import ru.shadowsparky.client.Utils.ADB.ADBStatus
import ru.shadowsparky.client.Utils.ADB.ADBWorker
import ru.shadowsparky.client.Utils.Controllerable
import ru.shadowsparky.client.Utils.ImageCallback
import ru.shadowsparky.client.Utils.Injection

class VideoController() : ImageCallback, Controllerable {
    private var client: Client? = null
    @FXML private lateinit var videoPane: GridPane
    private val log = Injection.provideLogger()
    private var stage: Stage? = null
    private var infelicity_width: Double = 0.0
    private var infelicity_height: Double = 0.0
    private val adb = Injection.provideAdb()

    override fun handleImage(image: Image) = Platform.runLater {
        val bImage = BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize(100.0, 100.0, true, true, true, false))
        val background = Background(bImage)
        videoPane.background = background
        infelicity_width = image.width / videoPane.width
        infelicity_height = image.height / videoPane.height
    }

    fun onDestroy(event: WindowEvent) {
        log.printInfo("Window destroyed")
        client!!.handling = false
    }

    fun attachClient(client: Client) {
        this.client = client
        if (this.client!!.addr == "127.0.0.1") {
            enableClicking()
        }
    }

    //FIXME Неправильно работает в вертикальном режиме
    fun enableClicking() {
        videoPane.addEventHandler(MouseEvent.MOUSE_CLICKED) {
            val x = infelicity_width * it.x
            val y = infelicity_height * it.y
            val res = adb.tapToScreen(x, y)
            if (res.status == ADBStatus.ERROR) {
                log.printError("TAP ERROR: ${res.info}")
            }
            log.printInfo("coordX: $x coordY: $y || VideoPane: ${videoPane.width} ${videoPane.height}")
        }
    }

    fun attachStage(stage: Stage?) {
        this.stage = stage
    }

    fun start() {
        client!!.start()
    }

}