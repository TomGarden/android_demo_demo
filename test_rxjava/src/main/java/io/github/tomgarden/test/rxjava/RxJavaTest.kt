package io.github.tomgarden.test.rxjava

import io.reactivex.*
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*


/**
 * describe :
 *
 * author : tom
 *
 * time : 2021-07-15
 */
class RxJavaTest {
    fun intToString(intVal: Int): String {
        System.out.println(intVal)
        Thread.sleep(1000)
        return intVal.toString()
    }


    fun testRepeat() {
        var intVal = 1
        Observable.fromCallable { intToString(intVal++) }
            .repeatUntil { intVal >= 10 }
            .doOnNext { System.out.println("onNext") }
            .doOnError { System.out.println("doOnError") }
            .doOnComplete { System.out.println("doOnComplete") }
            .subscribe()
    }

    fun intToStrObservable(intVal: Int): Observable<Int> = Observable.just(intVal)


    fun testRepeat2() {
        var intVal = 1
        Observable.fromCallable { intToString(intVal++) }
            .flatMapCompletable(object :
                io.reactivex.functions.Function<String, CompletableSource> {
                override fun apply(t: String): CompletableSource {
                    System.out.println("----" + t)
                    return Completable.complete()
                }
            })
            .andThen(Observable.just(intVal))
            .doOnNext { System.out.println("+++" + it.toString()) }
            .repeatUntil { intVal >= 10 }
            .doOnNext { System.out.println("onNext" + it) }
            .doOnError { System.out.println("doOnError") }
            .doOnComplete { System.out.println("doOnComplete") }
            .subscribe()
    }


    fun customSleep(intVal: Int, onNext: Consumer<Int>, onError: Consumer<Throwable>) {
        Observable.create(object : ObservableOnSubscribe<Int> {
            override fun subscribe(emitter: ObservableEmitter<Int>) {
                try {
                    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.getDefault())
                    val time = System.currentTimeMillis()
                    print(Thread.currentThread().name + "\t")
                    println("开始一个睡觉💤\t" + sdf.format(time))
                    println(intVal)
                    Thread.sleep(1000)
                    print(Thread.currentThread().name + "\t")
                    println("睡醒了-------\t" + sdf.format(time) + "\t" + sdf.format(System.currentTimeMillis()))

                    if (intVal == 3) {
                        //throw RuntimeException()
                    }

                    emitter.onNext(intVal * 10)
                    emitter.onComplete()
                } catch (exception: Exception) {
                    emitter.onError(exception)
                }
            }
        })
            //.blockingSubscribe(onNext)
            .subscribeOn(Schedulers.io())
            .blockingSubscribe(onNext, onError)
    }

    fun testRepeat3() {
        var stopval: Int = 0
        var throwable: Throwable? = null
        var intVal = 1
        Observable
            .fromCallable {
                customSleep(
                    intVal++,
                    Consumer<Int> { stopval = it },
                    Consumer<Throwable> { throwable = it }
                )
            }
            .repeatUntil {
                throwable?.let { throw it }
                stopval >= 100
            }
            .observeOn(Schedulers.newThread())
            .doOnNext {
                print(Thread.currentThread().name + "\t")
                System.out.println("------onNext")
            }
            .doOnError { System.out.println("doOnError") }
            .doOnComplete { System.out.println("doOnComplete") }
            .subscribe()
    }


    fun testRepeat4() {
        var stopval: Int = 0
        var throwable: Throwable? = null
        var intVal = 1
        val observable1 = Observable
            .fromCallable {
                customSleep(
                    intVal++,
                    Consumer<Int> { stopval = it },
                    Consumer<Throwable> { throwable = it }
                )
            }
            .repeatUntil {
                throwable?.let { throw it }
                stopval >= 30
            }

        val observable2 = Observable
            .fromCallable {
                customSleep(
                    intVal.apply { intVal += 2 },
                    Consumer<Int> { stopval = it },
                    Consumer<Throwable> { throwable = it }
                )
            }
            .repeatUntil {
                throwable?.let { throw it }
                stopval >= 100
            }

        Observable.concat(observable1, observable2)
            .observeOn(Schedulers.newThread())
            .doOnNext {
                print(Thread.currentThread().name + "\t")
                System.out.println("------onNext")
            }
            .doOnError { System.out.println("doOnError") }
            .doOnComplete { System.out.println("doOnComplete") }
            .subscribe()
    }


    fun completableTest() {
        Completable
            .create { emitter: CompletableEmitter ->
                try {
                    print(".....")
                    for (intVal in 1..10) {
                        print(intVal)
                        print("...")
                        Thread.sleep(1000)
                        if (intVal >= 8) {
                            throw RuntimeException("handle exception")
                        }
                    }
                    println("done")
                    emitter.onComplete()
                } catch (exception: Exception) {
                    emitter.onError(exception)
                }
            }
            .doOnError {
                it.printStackTrace()
                Thread.sleep(3000)
                println("doOnError \t doOnError \t doOnError \t doOnError")
            }
            .doOnComplete { println("操作完成 doOnComplete;;;;") }
            .blockingGet()
            ?.printStackTrace()
    }

    fun concatObservable1(): Observable<Int> {

        return Observable.create(ObservableOnSubscribe<Int> { emitter ->
            try {
                Thread.sleep(3000)
                for (index in 1 until 4) {
                    Thread.sleep(1000)
                    emitter.onNext(index)
                }
                emitter.onComplete()
            } catch (throwable: Throwable) {
                emitter.onError(throwable)
            }
        }).doOnNext { println(it) }

    }

    fun concatObservable2(): Observable<Int> {

        return Observable.create(ObservableOnSubscribe<Int> { emitter ->
            try {
                for (index in 10 until 14) {
                    emitter.onNext(index)
                }
                emitter.onComplete()
            } catch (throwable: Throwable) {
                emitter.onError(throwable)
            }
        }).doOnNext { println(it) }

    }

    fun concatTest() {
        Observable.concat(concatObservable2(), concatObservable1())
            .subscribe()
    }

    fun textFlatMap() {
        val completableToObservable = Completable
            .create { emitter ->
                try {
                    println("completeable.complete")
                    Thread.sleep(3000)
                    emitter.onComplete()
                } catch (throwable: Throwable) {
                    emitter.onError(throwable)
                }
            }
            .toObservable<Any>()
            .flatMap {
                Observable.create<String> { emitter ->
                    try {
                        for (index in 0..6) {
                            Thread.sleep(1000)
                            emitter.onNext("flatMap.Observable." + index)
                        }
                        Thread.sleep(1000)
                        emitter.onComplete()
                    } catch (throwable: Throwable) {
                        emitter.onError(throwable)
                    }
                }
            }
            .doOnNext { println("completableToObservable.println:" + it) }

        val observable = Observable.create(object : ObservableOnSubscribe<String> {
            override fun subscribe(emitter: ObservableEmitter<String>) {
                for (index in 0..6) {
                    Thread.sleep(1000)
                    emitter.onNext("Observable." + index)
                }
            }

        })

        val publish = Observable.concat(completableToObservable, observable).publish()

        val disposable = publish.subscribe(
            { println("publish." + it) },
            { println("publish.onError") },
            { println("publish.onComplete") },
        )

        publish.connect()

    }

    fun textFlatMap2() {
        Observable
            .create<Int> { emitter ->
                try {
                    for (index in 6 downTo 0) {
                        System.out.println("Observable.create : " + index)
                        emitter.onNext(index)
                    }
                    emitter.onComplete()
                } catch (throwable: Throwable) {
                    emitter.onError(throwable)
                }
            }
            .flatMap {
                Observable
                    .create<String> { emitter ->
                        try {
//                            val numList = Array(it) { index -> it.toString() }.toMutableList()
////                            val numText = StringUtils.join(numList, "")
////                            val spaceList = Array(it) { index -> "　" }.toMutableList()
////                            val spaceText = StringUtils.join(spaceList, "")
//                            System.out.println("${spaceText}\tflatMap.int:$numText")
//                            Thread.sleep(it * 1000L)
//
//                            emitter.onNext(numText)
                            emitter.onComplete()
                        } catch (throwable: Throwable) {
                            emitter.onError(throwable)
                        }
                    }
                    .subscribeOn(Schedulers.newThread())
            }
            .subscribe(
                { System.out.println("======= onNext: " + it) },
                { it.printStackTrace() },
                { System.out.println("************ onComplete: ") }
            )
    }
}