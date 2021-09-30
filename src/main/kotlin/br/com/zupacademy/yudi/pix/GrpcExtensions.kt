package br.com.zupacademy.yudi.pix

import br.com.zupacademy.yudi.RegistraChavePixRequest
import br.com.zupacademy.yudi.TipoChaveGrpc
import br.com.zupacademy.yudi.TipoContaGrpc
import br.com.zupacademy.yudi.pix.cadastro.NovaChavePix
import java.util.*

/**
 * Kotlin Extension Function
 */
fun RegistraChavePixRequest.toNovaChavePix(): NovaChavePix {
    return NovaChavePix(
        clienteId = this.clientId,
        tipo = when (this.tipoChave) {
            TipoChaveGrpc.UNKNOWN_TIPO_CHAVE -> null
            else -> TipoChavePix.valueOf(tipoChave.name)
        },
        valor =  this.valorChave,
        tipoConta = when (this.tipoConta) {
            TipoContaGrpc.UNKNOWN_TIPO_CONTA -> null
            else -> TipoConta.valueOf(tipoConta.name)
        }
    )
}