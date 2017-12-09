package rodrigo.unitec.localizadorsismico

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Created by rapid on 28/11/2017.
 */

class Clima {

    var coord: Coord? = null
    var weather: Array<Weather>? = null
    var base: String? = null
    var main: Main? = null
    var visibility:String?=null
    var wind: Wind?=null

    @JsonIgnoreProperties(ignoreUnknown = true)
    var clouds:Clouds?=null
    var dt:Int?=null
    var sys:Sys?=null
    var id:Int?=null
    var name:String?=null
    var cod:Int?=null

}
