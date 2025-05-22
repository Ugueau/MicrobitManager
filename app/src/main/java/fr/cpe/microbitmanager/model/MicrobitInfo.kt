package fr.cpe.microbitmanager.model

data class MicrobitInfo(
    var name : String,
    var id : String,
    var temperature: Int,
    var humidity : Int,
    var luminosity : Int,
    var temperatureConfigIndex : Int,
    var humidityConfigIndex : Int,
    var luminosityConfigIndex : Int
) {
    fun updateData(temperature: Int, humidity: Int, luminosity: Int)
    {
        this.humidity = humidity
        this.temperature = temperature
        this.luminosity = luminosity
    }

    fun updateDisplayConfig(temperatureConfigIndex: Int, humidityConfigIndex: Int, luminosityConfigIndex: Int)
    {
        this.temperatureConfigIndex = temperatureConfigIndex
        this.humidityConfigIndex = humidityConfigIndex
        this.luminosityConfigIndex = luminosityConfigIndex
    }

    fun formatConfig() : String
    {
        val result = CharArray(3)
        result[this.temperatureConfigIndex] = 'T'
        result[this.luminosityConfigIndex] = 'L'
        result[this.humidityConfigIndex] = 'H'

        return String(result)
    }
}