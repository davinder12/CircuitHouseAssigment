@file:Suppress("unused", "UNCHECKED_CAST")

package com.example.moviescreen.widgets

import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class AndroidUiBindingAdapters


@BindingAdapter("visibleGone")
fun showHide(view: View, show: Boolean) {
    view.visibility = if (show) View.VISIBLE else View.GONE
}

@BindingAdapter("goneVisible")
fun hideShow(view: View, show: Boolean) {
    view.visibility = if (show) View.GONE else View.VISIBLE
}

//
//@BindingAdapter("invisibleShow")
//fun invisibleShow(view: View, show: Boolean) {
//    view.visibility = if (show) View.INVISIBLE else View.VISIBLE
//}
//
//
//@BindingAdapter("app:listData")
//fun <T> RecyclerView.listData(list: List<T>?) {
//    (adapter as? ListAdapter<T, *>)?.submitList(list)
//}
//
//@BindingAdapter("app:src")
//fun ImageView.setImageDrawable(resource: Drawable?) {
//    resource?.let { this.setImageDrawable(it) }
//}
//
//@BindingAdapter("app:backSrc")
//fun ImageView.setImageSource(resource: Int?) {
//    resource?.let { this.setImageResource(it) }
//}
//
//
//@BindingAdapter("app:htmlText")
//fun AppCompatTextView.htmlText(text: String?) {
//    if (!text.isNullOrEmpty()) {
//        val message = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
//        } else Html.fromHtml(text)
//        this.text = message
//    }
//}
//
//
//@BindingAdapter("app:strike")
//fun MaterialTextView.strike(text: String) {
//    this.text = text
//    this.paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//}
//
//@BindingAdapter("url")
//fun ImageView.imageUrl(url: String?) {
//     when{
//         !url.isNullOrEmpty() && url.contains("http") -> Glide.with(this.context).load(url)
//             .placeholder(getCountryName(this.context)).fitCenter().centerCrop().into(this)
//         !url.isNullOrEmpty() -> Glide.with(this.context).load(File(url)).fitCenter().centerCrop()
//             .into(this)
//         else -> Glide.with(this.context).load("htts://www.google.com")
//             .placeholder(getCountryName(this.context)).fitCenter().centerInside().into(this)
//     }
//}
//
//
//@BindingAdapter("coverProfile")
//fun ImageView.converProfile(url: String?) {
//    when{
//        !url.isNullOrEmpty() && url.contains("http") -> Glide.with(this.context).load(url)
//            .placeholder(R.color.light_gray).fitCenter().centerCrop().into(this)
//        !url.isNullOrEmpty() -> Glide.with(this.context).load(File(url)).fitCenter().centerInside()
//            .into(this)
//        else -> Glide.with(this.context).load("htts://www.google.com")
//            .placeholder(R.color.light_gray).fitCenter().centerCrop().into(this)
//    }
//
//}



