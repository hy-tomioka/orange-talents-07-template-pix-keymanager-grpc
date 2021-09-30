package br.com.zupacademy.yudi.external.bacen

import br.com.zupacademy.yudi.pix.ChavePix
import br.com.zupacademy.yudi.pix.TipoChavePix
import br.com.zupacademy.yudi.pix.TipoConta
import java.time.LocalDateTime

data class ChavePixBacenRequest(
    val keyType: PixKeyType,
    val key: String,
    val bankAccount: BankAccountRequest,
    val owner: OwnerRequest
) {
    companion object {
        fun cria(chavePix: ChavePix): ChavePixBacenRequest {
            return ChavePixBacenRequest(
                keyType = PixKeyType.from(chavePix.tipoChave),
                key = chavePix.valorChave,
                bankAccount = BankAccountRequest(
                    participant = "60701190",
                    branch = chavePix.conta.agencia,
                    accountNumber = chavePix.conta.numero,
                    accountType = AccountType.from(chavePix.conta.tipoConta)
                ),
                owner = OwnerRequest(
                    type = OwnerType.NATURAL_PERSON,
                    name = chavePix.conta.titularNome,
                    taxIdNumber = chavePix.conta.titularCpf
                )
            )
        }
    }
}

enum class PixKeyType(val tipo: TipoChavePix?) {
    CPF(TipoChavePix.CPF),
    PHONE(TipoChavePix.CELULAR),
    EMAIL(TipoChavePix.EMAIL),
    RANDOM(TipoChavePix.CHAVE_ALEATORIA);

    companion object {
        fun from(tipo: TipoChavePix?): PixKeyType {
            return values().first {
                it.tipo == tipo
            }
        }
    }
}

data class BankAccountRequest(
    val participant: String,
    val branch: String,
    val accountNumber: String,
    val accountType: AccountType
)

enum class AccountType(val tipo: TipoConta?) {
    CACC(TipoConta.CONTA_CORRENTE),
    SVGS(TipoConta.CONTA_POUPANCA);

    companion object {
        fun from(tipoConta: TipoConta?): AccountType {
            return values().first {
                it.tipo == tipoConta
            }
        }
    }
}

data class OwnerRequest(
    val type: OwnerType,
    val name: String,
    val taxIdNumber: String
)

enum class OwnerType {
    NATURAL_PERSON,
    LEGAL_PERSON
}

data class ChavePixBacenResponse(
    // CreatePixKeyResponse
    val keyType: PixKeyType?,
    val key: String?,
    val bankAccount: BankAccountRequest?,
    val owner: OwnerRequest?,
    val createdAt: LocalDateTime?,

    // Problem
    val type: String?,
    val status: Int?,
    val title: String?,
    val detail: String?,
    // falta : violations - lista
)