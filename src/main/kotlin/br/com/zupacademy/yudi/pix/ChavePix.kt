package br.com.zupacademy.yudi.pix

import java.util.*
import javax.persistence.*

@Entity
class ChavePix(
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val tipoChave: TipoChavePix?,

    @Column(nullable = false, unique = true)
    val valorChave: String,

    @Embedded
    val conta: ContaItau
) {
    @Id
    val id: UUID = UUID.randomUUID()
}
