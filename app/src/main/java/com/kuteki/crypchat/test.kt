package com.kuteki.crypchat


fun main(){
    val participant1 = "AGyv90Dlv6bvyD4zLZC0"
    val participant2 = "6MZHtiNigCpve4ZyQDCW"
    val message = "hello my dear firend from Romania"
    var participant1_array = arrayListOf<Int>()
    var participant2_array = arrayListOf<Int>()
    var final_array = arrayListOf<Int>()
    var encryptedMessage = ""
    var decryptedMessage = ""
    for(char in participant1) {
        var n = char.toInt()
        var s = 0

        while(n > 0 || s >= 10){
            if(s >= 10){
                s = s % 10 + s / 10
            }else {
                s = s + n % 10
                n = n / 10
            }
        }
        participant1_array.add(s)
    }
    for(char in participant2) {
        var n = char.toInt()
        var s = 0

        while(n > 0 || s >= 10){
            if(s >= 10){
                s = s % 10 + s / 10
            }else {
                s = s + n % 10
                n = n / 10
            }
        }
        participant2_array.add(s)
    }
    var i=0
    for(z in participant1_array){
        final_array.add(z + participant2_array[i])
        i++
    }

    i=0
    for(letter in message){
        var letter_int = letter.toInt()
        letter_int += final_array[i]
        i++
        encryptedMessage += letter_int.toChar()
        if(i<20)
            i=0
    }
    print(encryptedMessage)
    print("\n")
    i=0
    for(letter in encryptedMessage){
        var letter_int = letter.toInt()
        letter_int -= final_array[i]
        i++
        decryptedMessage += letter_int.toChar()
        if(i<20)
            i=0
    }
    print(decryptedMessage)



}