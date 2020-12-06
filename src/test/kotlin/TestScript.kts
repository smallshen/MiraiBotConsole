import io.xiaoshen.commandbuilder.command.dsl.invoke

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


    groups {
        "gcommand" {
            reply {
                +"group only Command"
            }
        }

        123 {
            "spgcommand" {
                reply {
                    +"specific group only command"
                }
            }
        }
    }

    friends {
        "f" {
            reply {
                +"friends only command"
            }
        }
    }
}