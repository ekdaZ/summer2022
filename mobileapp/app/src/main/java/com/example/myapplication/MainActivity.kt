package com.example.myapplication

import android.app.DownloadManager
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.databinding.ActivityMainBinding

data class Item(val id: Int, val name: String, val price: Double )

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->

            val textView = findViewById<TextView>(R.id.textview_first)

            val queue = Volley.newRequestQueue(this)
            val url = "http://192.168.9.198:5000"
            val stringRequest =
                JsonObjectRequest(Request.Method.GET, url,null, { response ->
                    val items:ArrayList<Item> = ArrayList()
                    val data = response.getJSONArray("data")
                    for(index in 0 until data.length()) {
                        val indexedItem = data.getJSONObject(index)
                        val item = Item(indexedItem.getInt("id"), indexedItem.getString("name"), indexedItem.getDouble("price"))
                        items.add(item)

                    }
                    textView.text = "Response is: ${items[0].name}"
                    Snackbar.make(view, items[0].name, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                }, { error -> Log.d("error", error.toString()) })

            queue.add(stringRequest)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}