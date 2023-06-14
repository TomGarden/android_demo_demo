package io.github.tomgarden.test.rxjava

/**
 * describe :
 *
 * author : tom
 *
 * time : 2021-07-23
 */
class MyJavaCollection {
    fun textIterator() {
        val list = mutableListOf(null, 123, "asdfas", "asdfa ", null, null, 1232323, null)
        val each = list.iterator()
        while (each.hasNext()) {
            if (each.next() == null) {
                each.remove()
            }
        }
    }
}