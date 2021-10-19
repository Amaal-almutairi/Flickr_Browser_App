package com.example.flickrbrowserapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {
    lateinit var Img: ArrayList<image>
    lateinit var rvimage: RecyclerView
    lateinit var RVAdap: myAdapter
    lateinit var edimage: EditText
    lateinit var btnimage: Button
    lateinit var mainId: ConstraintLayout
    lateinit var mainIMG: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Img = arrayListOf()
        rvimage = findViewById(R.id.rv)
        edimage = findViewById(R.id.edimage)
        btnimage = findViewById(R.id.b1)
        RVAdap = myAdapter(this, Img)
        mainId = findViewById(R.id.mainId)
        mainIMG = findViewById(R.id.imagV)
        rvimage.adapter = RVAdap
        rvimage.layoutManager = LinearLayoutManager(this)
        var Search = edimage.text
        btnimage.setOnClickListener {
            if (Search.isNotEmpty()) {
                getAPI()
            } else {
                Toast.makeText(this, "Search Field is Empty", Toast.LENGTH_SHORT).show()
            }
        }
        mainIMG.setOnClickListener {
            closeItem()
        }

    }

    private fun getitems(): String {
        var response = ""
        try {
            response =
                URL("https://www.flickr.com/services/rest/?method=flickr.photos.search&api_key=238472206f07478ae8e403a700ba95f9&tags=${edimage.text}&text=&per_page=15&format=json&nojsoncallback=1")
                    .readText(Charsets.UTF_8)
        } catch (e: Exception) {
            println("Issue:$e")
        }
        return response


    }

    private fun getAPI() {
        CoroutineScope(IO).launch {
            val Data = async { getitems() }.await()
            if (Data.isNotEmpty()) {
                println(Data)
                Displayphotos(Data)
            } else {
                Toast.makeText(this@MainActivity, "No Images Found ", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private suspend fun Displayphotos(data: String) {
// https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}.jpg
        withContext(Main) {
            val jsobj = JSONObject(data)

            val IMAGES = jsobj.getJSONObject("photos").getJSONArray("photo")
            println(IMAGES)
            println(IMAGES.getJSONObject(0))
            println(IMAGES.getJSONObject(0).getString("farm"))
            for (i in 0 until IMAGES.length()) {
                val Title = IMAGES.getJSONObject(i).getString("title")
                val farmID = IMAGES.getJSONObject(i).getString("farm")
                val serverID = IMAGES.getJSONObject(i).getString("server")
                val id = IMAGES.getJSONObject(i).getString("id")
                val secret = IMAGES.getJSONObject(i).getString("secret")
                val Imagelink = "https://farm$farmID.staticflickr.com/$serverID/${id}_$secret.jpg"

                Img.add(image(Title, Imagelink))

            }
            RVAdap.notifyDataSetChanged()
        }

    }

    fun openItem(Link: String) {
        Glide.with(this).load(Link).into(mainIMG)
        mainIMG.isVisible = false
        mainIMG.isVisible = true
        mainIMG.isVisible = true

    }

    private fun closeItem() {
        mainIMG.isVisible = false
        mainIMG.isVisible = true
        mainIMG.isVisible = true

    }
}