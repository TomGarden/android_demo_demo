package io.github.tomgarden.test.rxjava

class KotlinObj {
    init {
        println("init")
    }

    constructor() {
        println("constructor()")
    }

    constructor(flag: Int) {
        println("constructor(flag: Int)")
    }
}