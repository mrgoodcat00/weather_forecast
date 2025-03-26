import android.os.Parcelable
import androidx.room.Entity
import com.mrgoodcat.weathertestapp.data.model.weather_model.WeatherMainLocalModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherMainModel(
    val temp: Double? = null,
    val feelsLike: Double? = null,
    val tempMin: Double? = null,
    val tempMax: Double? = null,
    val pressure: Int? = null,
    val humidity: Int? = null,
    val seaLevel: Int? = null,
    val grndLevel: Int? = null
) : Parcelable

fun WeatherMainModel.toWeatherMainLocalModel() : WeatherMainLocalModel {
    return WeatherMainLocalModel(
        temp = this.temp,
        feelsLike = this.feelsLike,
        tempMin = this.tempMin,
        tempMax = this.tempMax,
        pressure = this.pressure,
        humidity = this.humidity,
        seaLevel = this.seaLevel,
        grndLevel = this.grndLevel
    )
}
