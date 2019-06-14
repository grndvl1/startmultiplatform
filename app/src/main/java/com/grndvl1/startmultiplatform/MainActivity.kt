package com.grndvl1.startmultiplatform

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import io.ktor.client.engine.okhttp.OkHttpConfig
import io.ktor.client.engine.okhttp.OkHttpEngine
import kotlinx.coroutines.*
import okhttp3.logging.HttpLoggingInterceptor
import kotlin.coroutines.CoroutineContext
import com.facebook.stetho.Stetho
import com.squareup.sqldelight.android.AndroidSqliteDriver

class MainActivity : AppCompatActivity(), CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Stetho.initializeWithDefaults(this)

        val engine = OkHttpEngine(OkHttpConfig().apply {
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        })

        //DO NOT DO THE SAME IN YOUR PRODUCTION CODE, use DI
        val config = SupportSQLiteOpenHelper.Configuration.builder(this)
            .name("database1.db")
            .callback(object : SupportSQLiteOpenHelper.Callback(1) {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    val driver = AndroidSqliteDriver(db)
                    AnyNameDatabase.Schema.create(driver)
                }

                override fun onUpgrade(db: SupportSQLiteDatabase?, oldVersion: Int, newVersion: Int) {
                }

            })
            .build()

        val sqlHelper = FrameworkSQLiteOpenHelperFactory().create(config)

        val database = createDatabase(AndroidSqliteDriver(sqlHelper))

        val movieDao = MovieDao(database)

        val movieApi = MovieApi(engine)
        val movieRepository = MovieRepository(movieApi, movieDao)
        launch(Dispatchers.Main) {
            try {
                val result = withContext(Dispatchers.IO) { movieRepository.fetchMovie() }
                Toast.makeText(this@MainActivity, result.toString(), Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }

        launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.IO) {movieRepository.selectFromDb()}
            Toast.makeText(this@MainActivity, "result from db ${result.toString()}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
