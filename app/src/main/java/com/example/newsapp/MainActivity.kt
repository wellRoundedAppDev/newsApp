package com.example.newsapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.newsapp.data.NewsData
import com.example.newsapp.databinding.ActivityMainBinding
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var page = 0
    private var pageSize = 10

    private val newsList: ArrayList<NewsData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.newsSearchButton.setOnClickListener {

            newsList.clear()
            createListOfNews()
        }

        binding.loadMoreButton.setOnClickListener {

            if(page != 0){
                addMoreListOfNews()
            }
        }

    }

    private fun addMoreListOfNews(){

        page++
        connectToTheGuardianAPI()
    }

    private fun createListOfNews() {

        page = 1
        connectToTheGuardianAPI()
    }

    private fun connectToTheGuardianAPI() {

        val queue = Volley.newRequestQueue(this)

        val url = getURL()
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener { response ->
                try {
                    retrieveNewsDataFromTheGuardianAPI(response)

                } catch (exception: Exception) {
                    exception.printStackTrace()
                }


            },
            Response.ErrorListener { error ->
                Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
            }
        )

        queue.add(stringRequest)
    }


    private fun getURL(): String {

        val search = binding.searchNewsEditText.text
        closeKeyboard()
        return "https://content.guardianapis.com/$search?page=$page&page-size=$pageSize&api-key=2ed09c94-e898-4db0-ae77-6c63f6ed0aff"
    }

    private fun closeKeyboard() {
        val view: View? = this.currentFocus
        if (view != null) {

            val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


    private fun retrieveNewsDataFromTheGuardianAPI(response: String) {

        val jsonObject = JSONObject(response)
        val firstObject = jsonObject.getJSONObject("response")
        val resultsArray = firstObject.getJSONArray("results")

        for(i in 0..(pageSize - 1)){

            val webTitle = resultsArray.getJSONObject(i).getString("webTitle")
            val webURL =   resultsArray.getJSONObject(i).getString("webUrl")

            val newsData = NewsData(webTitle, webURL)
            newsList.add(newsData)
        }

        initializeListViewAdapter()
    }
    
    private fun initializeListViewAdapter(){

        val customAdapter = CustomAdaptor(newsList)
        binding.listView.adapter = customAdapter
    }

}



