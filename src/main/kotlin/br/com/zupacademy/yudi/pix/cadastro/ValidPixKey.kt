package br.com.zupacademy.yudi.pix.cadastro

import jakarta.inject.Singleton
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.TYPE

@MustBeDocumented
@Target(CLASS, TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = [ValidPixKeyValidator::class])
annotation class ValidPixKey(
    val message: String = "chave Pix inválida (\${validatedValue.tipo})"
)

/**
 * Using Bean Validation API because we wanted to use Custom property paths
 * https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#section-custom-property-paths
 */
@Singleton
class ValidPixKeyValidator: ConstraintValidator<ValidPixKey, NovaChavePix> {

    override fun isValid(chave: NovaChavePix?, context: ConstraintValidatorContext): Boolean {

        // must be validated with @NotNull
        if (chave?.tipo == null) {
            return true
        }

        val valid = chave.tipo.valida(chave.valor)
        if (!valid) {
            // https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#section-custom-property-paths
            context.disableDefaultConstraintViolation()
            context
                .buildConstraintViolationWithTemplate(context.defaultConstraintMessageTemplate) // or "chave Pix inválida (${value.tipo})"
                .addPropertyNode("chave").addConstraintViolation()
        }

        return valid
    }
}