
import android.os.Parcelable
import com.mrgoodcat.weathertestapp.data.model.weather_model.WindLocalModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class WindModel(
    val speed: Double? = null,
    val deg: Int? = null,
    val gust: Double? = null
) : Parcelable

fun WindModel.toWindLocalModel(): WindLocalModel {
    return WindLocalModel(
        speed = this.speed,
        deg = this.deg,
        gust = this.gust
    )
}