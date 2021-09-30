package br.com.zupacademy.yudi.external.itau

import br.com.zupacademy.yudi.pix.TipoConta
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client

@Client("\${client.itau.erp.url}")
interface ItauErpClient {

    @Get("/{clienteId}/contas")
    fun encontraCliente(
        @PathVariable clienteId: String,
        @QueryValue tipo: TipoConta?
    ): HttpResponse<DadosContaResponse?>

}


