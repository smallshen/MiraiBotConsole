import io.xiaoshen.commandbuilder.dsl.invoke

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