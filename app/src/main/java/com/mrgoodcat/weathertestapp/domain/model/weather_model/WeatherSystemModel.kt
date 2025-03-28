import com.mrgoodcat.weathertestapp.data.model.weather_model.WeatherSystemLocalModel
import java.io.Serializable


data class WeatherSystemModel(
    val type: Int? = null,
    val id: Int? = null,
    val country: String? = null,
    val sunrise: Int? = null,
    val sunset: Int? = null
) : Serializable

fun WeatherSystemModel.toWeatherSystemLocalModel(): WeatherSystemLocalModel {
    return WeatherSystemLocalModel(
        type = this.type,
        id = this.id,
        country = this.country,
        sunrise = this.sunrise,
        sunset = this.sunset
    )
}