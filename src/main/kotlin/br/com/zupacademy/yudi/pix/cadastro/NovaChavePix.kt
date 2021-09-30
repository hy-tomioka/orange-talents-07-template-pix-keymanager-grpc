package br.com.zupacademy.yudi.pix.cadastro

import br.com.zupacademy.yudi.pix.ChavePix
import br.com.zupacademy.yudi.pix.ContaItau
import br.com.zupacademy.yudi.pix.TipoChavePix
import br.com.zupacademy.yudi.pix.TipoConta
import br.com.zupacademy.yudi.shared.validation.ValidUUID
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Introspected
@ValidPixKey
data class NovaChavePix(
    @field:NotNull @field:ValidUUID val clienteId: String,
    @field:NotNull val tipo: TipoChavePix?,
    @field:Size(max = 77) var valor: String,
    @field:NotNull val tipoConta: TipoConta?
) {
    fun toChavePix(cliente: ContaItau): ChavePix {
        return ChavePix(
            tipoChave = tipo,
            valorChave = when (tipo) {
                TipoChavePix.CHAVE_ALEATORIA -> UUID.randomUUID().toString()
                else -> valor
            },
            conta = cliente
        )
    }
}
