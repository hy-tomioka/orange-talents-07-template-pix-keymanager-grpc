package br.com.zupacademy.yudi.pix.cadastro

import br.com.zupacademy.yudi.PixKeymanagerGrpcServiceGrpc
import br.com.zupacademy.yudi.RegistraChavePixRequest
import br.com.zupacademy.yudi.RegistraChavePixResponse
import br.com.zupacademy.yudi.pix.toNovaChavePix
import com.google.protobuf.Any
import com.google.rpc.BadRequest
import io.grpc.Status
import io.grpc.protobuf.StatusProto
import io.grpc.stub.StreamObserver
import io.micronaut.validation.Validated
import jakarta.inject.Singleton
import javax.validation.ConstraintViolationException
import com.google.rpc.Status as ViolationsStatus

@Singleton
@Validated
class NovaChaveEndpoint(
    private val service: NovaChaveService
) : PixKeymanagerGrpcServiceGrpc.PixKeymanagerGrpcServiceImplBase() {

    override fun cadastrar(
        request: RegistraChavePixRequest?,
        responseObserver: StreamObserver<RegistraChavePixResponse>?
    ) {

        if (request == null) {
            throw Status.INVALID_ARGUMENT
                .withDescription("Requisição nula.")
                .asRuntimeException()
        }

        try {
            val chavePixWrapper: NovaChavePixWrapper = service.cadastra(request.toNovaChavePix())
            retornaChavePix(chavePixWrapper, responseObserver)
        } catch (e: ConstraintViolationException) {
            val violationsStatus = mapeiaConstraintViolations(e)
            responseObserver?.onError(StatusProto.toStatusRuntimeException(violationsStatus))
        }
    }

    private fun retornaChavePix(
        chavePixWrapper: NovaChavePixWrapper,
        responseObserver: StreamObserver<RegistraChavePixResponse>?
    ) {
        if (chavePixWrapper.temErro) {
            responseObserver?.onError(
                chavePixWrapper.status?.let {
                    it.asRuntimeException()
                }
            )
            return
        }
        responseObserver?.let {
            it.onNext(
                RegistraChavePixResponse.newBuilder()
                    .setPixId(chavePixWrapper.chavePix!!.id.toString())
                    .build()
            )
            it.onCompleted()
        }
    }

    private fun mapeiaConstraintViolations(exception: ConstraintViolationException): com.google.rpc.Status? {
        val violations = BadRequest.newBuilder()
            .addAllFieldViolations(exception.constraintViolations.map { violation ->
                BadRequest.FieldViolation.newBuilder()
                    .setField(violation.propertyPath.last().name)
                    .setDescription(violation.message)
                    .build()
            }).build()

        return ViolationsStatus.newBuilder()
            .setCode(Status.INVALID_ARGUMENT.code.value())
            .setMessage("dados inválidos")
            .addDetails(Any.pack(violations))
            .build()
    }
}

