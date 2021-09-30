package br.com.zupacademy.yudi.pix.cadastro

import br.com.zupacademy.yudi.external.bacen.BacenClient
import br.com.zupacademy.yudi.external.bacen.ChavePixBacenRequest
import br.com.zupacademy.yudi.external.itau.ItauErpClient
import br.com.zupacademy.yudi.pix.ChavePixRepository
import br.com.zupacademy.yudi.pix.ContaItau
import io.grpc.Status.*
import io.micronaut.http.HttpStatus.UNPROCESSABLE_ENTITY
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.validation.Validated
import jakarta.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid

@Singleton
@Validated
class NovaChaveService(
    val repository: ChavePixRepository,
    val itauClient: ItauErpClient,
    val bacenClient: BacenClient
) {

    @Transactional
    fun cadastra(@Valid novaChavePix: NovaChavePix): NovaChavePixWrapper {

        // anotação não @ValidPixKey não esta funcionando
        if (!novaChavePix.tipo?.valida(novaChavePix.valor)!!) {
            return NovaChavePixWrapper.reportaErro(
                novaChavePix = novaChavePix,
                mensagem = "Erro no bean validation",
                status = INVALID_ARGUMENT
            )
        }

        // verifica se a chave já existe
        if (repository.existsByValorChave(novaChavePix.valor)) {
            return NovaChavePixWrapper.reportaErro(
                novaChavePix = novaChavePix,
                mensagem = "Chave pix com valor '${novaChavePix.valor}' já existente.",
                status = ALREADY_EXISTS
            )
        }

        // verifica se o cliente existe
        val clienteItauResponse = itauClient.encontraCliente(
            clienteId = novaChavePix.clienteId.toString(),
            tipo = novaChavePix.tipoConta
        )
        val contaItau: ContaItau = (clienteItauResponse.body()?.toContaItau())
            ?: return NovaChavePixWrapper.reportaErro(
                novaChavePix = novaChavePix,
                mensagem = "Cliente '${novaChavePix.clienteId}' não existe.",
                status = FAILED_PRECONDITION
            )

        // mapeia chave pix para objeto de domínio
        val chavePix = novaChavePix.toChavePix(contaItau)

        // registra chave pix no Bacen
        val novaChaveBacen = ChavePixBacenRequest.cria(chavePix)

        try {
            bacenClient.registra(novaChaveBacen)
            val chavePixCorreta = NovaChavePixWrapper(novaChavePix)
            chavePixCorreta.insereChavePix(chavePix)
            repository.save(chavePixCorreta.chavePix)
            return chavePixCorreta
        } catch (e: HttpClientResponseException) {
            return when (e.status) {
                UNPROCESSABLE_ENTITY -> NovaChavePixWrapper.reportaErro(
                    novaChavePix = novaChavePix,
                    mensagem = e.message!!,
                    status = ALREADY_EXISTS
                )
                else -> {
                    NovaChavePixWrapper.reportaErro(
                        novaChavePix = novaChavePix,
                        mensagem = "Ocorreu um erro inesperado.",
                        status = INTERNAL
                    )
                }
            }
        }
    }
}
