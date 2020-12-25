package com.example.search.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.search.data.RetrofitInterface
import com.example.search.data.model.Search
import com.example.search.data.model.SearchModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class MainViewModel(private val network: RetrofitInterface) : ViewModel() {

    val progress = MutableLiveData<Boolean>()
    private var compositeDisposable = CompositeDisposable()
    private val filmBehaviourSubject = BehaviorSubject.create<String>()

    val data = MutableLiveData<SearchModel>()

    init {
        compositeDisposable.add(
            filmBehaviourSubject
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribe({
                    searchFilm(it)
                },{

                })
        )
    }

    fun search(text: String) {
        filmBehaviourSubject.onNext(text)
    }

    private fun searchFilmTwo(text: String){                                      // concat
        val res  = network.searchFilmTwo("4f391043", text)
        val res2  = network.searchFilmTwo("4f391043", text)
        compositeDisposable.add(
            Observable.concat(res, res2)
                .first(SearchModel(response = "", search = listOf(), totalResults = ""))
                .subscribe({

                },{

                })
        )
    }

    private fun searchFilmThree(text: String){                                   //zip
        val res  = network.searchFilmTwo("4f391043", text)
        val res2  = network.searchFilmTwo("4f391043", text)
        compositeDisposable.add(
          Observable.zip(res, res2,
              BiFunction<SearchModel, SearchModel, SearchModel>
              { t1, t2 ->
                  val list = arrayListOf<Search>()
                  list.addAll(t1.search)
                  list.addAll(t2.search)
                  SearchModel(totalResults = t1.totalResults, search = list, response = t1.response)}) //?
                .subscribe({
                Log.d("sdvsv","sdvsvd")
                },{
                    Log.d("sdvsv","sdvsvd")
                })
        )
    }

    private fun searchFilm(text: String) {
        compositeDisposable.add(network.searchFilm("4f391043", text)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                progress.postValue(true)
            }
            .doFinally {
                progress.postValue(false)
            }
//            .map {                                                      //map
//                val result = it.search
//                return@map result
//            }
//            .map {
//                val date = it.map { SearchTwo(
//                  poster = it.poster,
//                  title = "fbdfb",
//                  type = it.type,
//                  year = it.year,
//                  imdbID = it.imdbID,
//                ) }
//                return@map date
//            }
//            .filter {                                                 //filter
//                return@filter it.totalResults.toInt()>0
//            }
            .subscribe({
                data.postValue(it)
                Log.d("____ADssadsad", "success")
            }, {
                Log.d("____ADssadsad", it.localizedMessage)
            })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}