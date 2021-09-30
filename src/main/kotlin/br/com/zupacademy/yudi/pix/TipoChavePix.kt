package br.com.zupacademy.yudi.pix

enum class TipoChavePix {

    CPF {
        override fun valida(valor: String): Boolean {
            if (!valor.matches("^[0-9]{11}\$".toRegex())) {
                return false
            }
            return true
        }
    },
    CELULAR {
        override fun valida(valor: String): Boolean {
            if (!valor.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())) {
                return false
            }
            return true
        }
    },
    EMAIL {
        override fun valida(valor: String): Boolean {
            if (!valor.matches("^[A-Za-z0-9+_.-]+@(.+)$".toRegex())) {
               return false
            }
            return true
        }
    },
    CHAVE_ALEATORIA {
        override fun valida(valor: String): Boolean {
            if (valor.isNotBlank()) {
                return false
            }
            return true
        }
    };

    abstract fun valida(valor: String) : Boolean

}
