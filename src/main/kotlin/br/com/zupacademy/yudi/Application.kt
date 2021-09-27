package br.com.zupacademy.yudi

import io.micronaut.runtime.Micronaut.*
fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("br.com.zupacademy.yudi")
		.start()
}

