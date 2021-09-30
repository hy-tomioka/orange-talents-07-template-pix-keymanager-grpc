package br.com.zupacademy.yudi.pix

import java.util.*
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EnumType.STRING
import javax.persistence.Enumerated

@Embeddable
class ContaItau(
    @Enumerated(STRING)
    @Column(nullable = false)
    val tipoConta: TipoConta,

    @Column(nullable = false)
    val agencia: String,

    @Column(nullable = false)
    val numero: String,

    @Column(nullable = false)
    val titularId: UUID,

    @Column(nullable = false)
    val titularNome: String,

    @Column(nullable = false)
    val titularCpf: String
)