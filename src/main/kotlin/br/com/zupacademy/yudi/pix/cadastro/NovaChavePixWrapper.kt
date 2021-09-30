package br.com.zupacademy.yudi.pix.cadastro

import br.com.zupacademy.yudi.pix.ChavePix
import io.grpc.Status

class NovaChavePixWrapper(
    val novaChavePix: NovaChavePix,
    var temErro: Boolean = false,
    var status: Status? = null
) {

    var chavePix: ChavePix? = null

    fun insereChavePix(chavePix: ChavePix) {
        this.chavePix = chavePix
    }

    companion object {
        fun reportaErro(novaChavePix: NovaChavePix, mensagem: String, status: Status) : NovaChavePixWrapper {
            return NovaChavePixWrapper(
                novaChavePix = novaChavePix,
                temErro = true,
                status = status.withDescription(mensagem)
            )
        }
    }
}