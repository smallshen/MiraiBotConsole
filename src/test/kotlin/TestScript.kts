import io.xiaoshen.commandbuilder.dsl.invoke

/**
 * @see com.github.smallshen.miraibot.script.loadScripts
 */
"!" {
    "test" {
        "a" {
            reply {
                +"a"
            }
        }

        "b" {
            reply {
                +"b"
            }
        }
    }
}