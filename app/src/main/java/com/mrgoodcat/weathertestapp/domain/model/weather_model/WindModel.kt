
import com.mrgoodcat.weathertestapp.data.model.weather_model.WindLocalModel
import java.io.Serializable

data class WindModel(
    val speed: Double? = null,
    val deg: Int? = null,
    val gust: Double? = null
) : Serializable

fun WindModel.toWindLocalModel(): WindLocalModel {
    return WindLocalModel(
        speed = this.speed,
        deg = this.deg,
        gust = this.gust
    )
}