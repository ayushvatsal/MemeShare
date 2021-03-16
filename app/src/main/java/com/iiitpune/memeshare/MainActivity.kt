package com.iiitpune.memeshare

import android.app.DownloadManager
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import br.com.simplepass.loadingbutton.customViews.CircularProgressImageButton
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {
    var currentImageUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadMeme()


    }
    private fun loadMeme(){
        // Instantiate the RequestQueue.

        val pbar = findViewById<ProgressBar>(R.id.spin_kit)
        pbar.visibility = View.VISIBLE;
        val url = "https://meme-api.herokuapp.com/gimme"
        val image = findViewById<ImageView>(R.id.image)
        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                { response ->
                    currentImageUrl = response.getString("url")
                    Glide.with(this).load(currentImageUrl).listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {

                         pbar.visibility = View.GONE

                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            pbar.visibility = View.GONE

                            return false
                        }
                    }).into(image)
                },
                {
                    Toast.makeText(this, "Something Went wrong", Toast.LENGTH_SHORT).show()
                })

        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
    fun shareMeme(view: View) {


      val intent  = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Hey Checkout this cool meme I got from reddit $currentImageUrl")
        val chooser = Intent.createChooser(intent,"Share this meme using...")
        startActivity(chooser)

    }
    fun nextMeme(view: View){
        val img  = findViewById<ImageView>(R.id.image)
        img.setImageDrawable(null)
        loadMeme()

    }
}