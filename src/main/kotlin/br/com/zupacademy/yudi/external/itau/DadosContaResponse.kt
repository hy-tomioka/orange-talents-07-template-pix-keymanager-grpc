package br.com.zupacademy.yudi.external.itau

import br.com.zupacademy.yudi.pix.ContaItau
import br.com.zupacademy.yudi.pix.TipoConta
import java.util.*

data class DadosContaResponse(
    val tipo: String,
    val titular: TitularResponse,
    val instituicao: InstituicaoResponse,
    val agencia: String,
    val numero: String
) {
    fun toContaItau(): ContaItau {
                return ContaItau(
                    tipoConta = TipoConta.valueOf(this.tipo),
                    agencia = this.agencia,
                    numero = this.numero,
                    titularId = this.titular.id,
                    titularNome = this.titular.nome,
                    titularCpf = this.titular.cpf
        )
    }
}

data class TitularResponse(
    val id: UUID,
    val nome: String,
    val cpf: String
)

data class InstituicaoResponse(
    val nome: String,
    val ispb: String
)
