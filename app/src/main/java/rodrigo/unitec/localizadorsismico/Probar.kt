package rodrigo.unitec.localizadorsismico

import com.fasterxml.jackson.databind.ObjectMapper

import org.springframework.web.client.RestTemplate

import java.io.IOException

/**
 * Created by rapid on 08/12/2017.
 */

class Probar {

    @Throws(IOException::class)
    fun hola() {
        val c = Clima()
        val restTemplate = RestTemplate()
        val response = restTemplate.postForObject("url", c, String::class.java)
        val mapper = ObjectMapper()
        mapper.readValue(response, Estatus::class.java)
    }
}
