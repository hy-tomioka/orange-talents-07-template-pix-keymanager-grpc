package br.com.zupacademy.yudi.external.bacen

import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType.APPLICATION_XML
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client

@Client("\${client.bacen.url}")
interface BacenClient {

    @Post(produces = [APPLICATION_XML], consumes = [APPLICATION_XML])
    fun registra(@Body request: ChavePixBacenRequest) : HttpResponse<ChavePixBacenResponse>
}
