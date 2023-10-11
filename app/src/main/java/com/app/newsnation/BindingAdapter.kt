package com.app.newsnation

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import com.app.newsnation.utils.Constants.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("android:image_loader")
fun setImage(imageView: ImageView, imgUrl: String?) {
    if (!imgUrl.isNullOrEmpty()) {
        CoroutineScope(Dispatchers.Default).launch {
            imageView.load(imgUrl) {
                crossfade(enable = true)
                placeholder(R.drawable.loading_img)
            }
        }
    } else {
        imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
        imageView.setImageResource(R.drawable.news_nation)
    }
}

@BindingAdapter("android:format_date")
fun setFormatedDate(textView: TextView, date: String?) {

//    2023-10-06T12:12:22Z

    val cal by lazy { Calendar.getInstance() }

    if (!date.isNullOrEmpty()) {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("d MMM, yyyy' . 'h:mm a", Locale.ENGLISH)

        try {
            val parsedDate = inputFormat.parse(date)
            val formattedDate = outputFormat.format(parsedDate!!)
            textView.text = formattedDate
        } catch (e: ParseException) {
            e.printStackTrace()
            // Handle the parsing error if needed
            Log.d(TAG, "setFormatedDate: $e")
        }
    } else {
        // Handle the case where the date is null or empty
        textView.text = "${cal.time}"
    }

}