package br.com.zupacademy.yudi.pix

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
class TitularItau(
    val uuid: UUID,
    val nome: String,
    val cpf: String
) {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    var id: Long? = null

//    @OneToMany(mappedBy = "titular")
//    val contas: MutableList<ContaItau> = mutableListOf()
}