package com.example.newsapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import com.example.newsapp.data.NewsData

class CustomAdaptor(private var arrayList: ArrayList<NewsData>) : BaseAdapter() {

    override fun getCount() = arrayList.size

    override fun getItem(position: Int) = arrayList[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val context = parent?.context
        var rowView: View? = convertView

        val inflater: LayoutInflater =
            context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        if (rowView == null)
            rowView = inflater.inflate(R.layout.list_view_item, parent, false)

        val item = arrayList[position]

        val newsTitleTextView = rowView?.findViewById<TextView>(R.id.news_title_text_view)
        newsTitleTextView?.text = item.webTitle

        val newsPageButton = rowView?.findViewById<Button>(R.id.news_web_page_button)
        newsPageButton?.setOnClickListener {

            val webpage: Uri = Uri.parse(item.webURL)
            context.startActivity(Intent(Intent.ACTION_VIEW, webpage))

        }

        return rowView!!

    }
}