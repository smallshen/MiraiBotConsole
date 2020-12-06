import de.swirtz.ktsrunner.objectloader.KtsObjectLoader
import java.io.File

fun KtsObjectLoader.loadNoCast(script: String) = engine.eval(script)


fun main() {
    val scriptContent = File("test.kts").readText()
    val fromScript = KtsObjectLoader().loadNoCast(scriptContent)
}

class KtsTest(val root: String, body: KtsTest.() -> Unit = {}) {
    var string = "awa"

    init {

        this.apply(body)
    }
}