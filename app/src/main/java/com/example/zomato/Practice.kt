package com.example.zomato

fun main(){
    var totalAmount = 0
    var cartPrice = ArrayList<Int>()
    cartPrice.add(20)
    cartPrice.add(50)
    cartPrice.add(10)

    var cartQuantity = ArrayList<Int>()
    cartQuantity.add(1)
    cartQuantity.add(2)
    cartQuantity.add(3)

    for (i in 0 until cartPrice.size){
        val price = cartPrice[i]
        val quantity = cartQuantity[i]
        totalAmount+=price * quantity
    }
    println(totalAmount)
}
