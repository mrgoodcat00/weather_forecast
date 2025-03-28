import com.mrgoodcat.weathertestapp.data.model.weather_model.WeatherLocalModel
import java.io.Serializable


data class WeatherModel(
    val main: String? = null,
    val description: String? = null,
    val icon: String? = null,
    var name: String? = null,
    var type: String? = null
) : Serializable

fun WeatherModel.toWeatherLocalModel(): WeatherLocalModel {
    return WeatherLocalModel(
        main = this.main,
        description = this.description,
        icon = this.icon,
        name = this.name
    )
}

fun List<WeatherModel>.toListWeatherLocalModel(): List<WeatherLocalModel> {
    return this.map { it.toWeatherLocalModel() }
}