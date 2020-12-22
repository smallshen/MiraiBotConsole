import javax.script.ScriptEngineManager
import kotlin.reflect.KClass


fun main() {
    ScriptEngineManager().getEngineByExtension("kts").eval("awa()\nfun awa() = println(\"awa\")") as KClass<*>
}