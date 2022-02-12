package com.example.jpgtopngconvertor

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import coil.clear
import coil.load
import com.example.jpgtopngconvertor.databinding.ActivityMainBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import java.util.concurrent.TimeUnit

class MainActivity : MvpAppCompatActivity(R.layout.activity_main), MainView {

    private lateinit var binding: ActivityMainBinding
    private var converterDisposable: CompositeDisposable? = null

    private val presenter: MainPresenter by moxyPresenter {
        MainPresenter(activityResultRegistry, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageView.setOnClickListener {
            binding.progressCircular.visibility = View.GONE
            presenter.onPickImageViewClick()
        }

        binding.convertBtn.setOnClickListener {
            binding.progressCircular.visibility = View.VISIBLE
            presenter.onConvertImageClick()
        }
    }

    override fun showPickedImage(uri: Uri?) {
        with(binding.imageView) {
            clear()
            load(uri) {
                listener(
                    onError = { request, throwable ->
                        Toast.makeText(
                            request.context,
                            throwable.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                )
            }
        }
    }

    override fun convertImage(uri: Uri) {
        converterDisposable = CompositeDisposable()
        converterDisposable?.add(
            ImageConverter.convertJpgToPng(applicationContext.contentResolver, uri)
                .delay(3, TimeUnit.SECONDS)
                .cache()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Toast.makeText(this, "${it.first} converted to png.", Toast.LENGTH_LONG).show()
                    binding.progressCircular.visibility = View.GONE
                    binding.imageView.setImageBitmap(it.second)
                }, {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                })
        )
    }

    override fun onDestroy() {
        converterDisposable?.dispose()
        super.onDestroy()
    }
}