/**
  * Created by Tom Lous on 10/07/2018.
  * Copyright © 2018 Datlinq B.V..
  */
object TestCase extends App {



  def capitalizeString(str: String): Unit = {



    val newStr = str.replaceAll("""([\.\?!]+)""", "$1##").split("##").toList.map(sentence =>
        sentence.trim.capitalize.replaceAll("""([\:;,])\s*""", "$1 ")
    ).mkString(" ")

    println(str + "\n => " + newStr)
  }

  capitalizeString("first, solve the problem.then, write the code.") // "First, solve the problem. Then, write the code."
  capitalizeString("this is a test... and another test.") // "This is a test... And another test."
  capitalizeString("hello. how are you today? great! i'm fine too.") // "Hello. How are you today? Great! I'm fine too."
  capitalizeString("do.or do not.   there is no try.") // "Do. Or do not. There is no try."
  capitalizeString("the house is on fire!?run!") // "The house is on fire!? Run!"
  capitalizeString("the conference has people who have come from Moscow,Idaho;Paris,Texas;London,Ohio; and other places as well.")
  // "The conference has people who have come from Moscow, Idaho; Paris, Texas; London, Ohio; and other places as well."


  def numberOfCarryOperations(a: Int, b: Int): Unit = {

    def carry(a1: Int, b1: Int, add: Int = 0):Int = {
      val remainderA1 = a1 % 10
      val remainderB1 = b1 % 10

//      println(a1, b1)

      val carryOps = if (remainderA1 + remainderB1 + add >= 10) 1 else 0

      if (a1 > 10 || b1 > 10) {

        val nextA = a1 / 10
        val nextB = b1 / 10

        carryOps + carry(nextA, nextB, carryOps)
      }
      else {
        carryOps
      }


    }

    println(a,b, carry(a,b))
  }

  numberOfCarryOperations(65,55)
  numberOfCarryOperations(123, 456) // 0
  numberOfCarryOperations(555, 555) // 3
  numberOfCarryOperations(900, 11) // 0
  numberOfCarryOperations(145, 55) // 2
  numberOfCarryOperations(0, 0) // 0
  numberOfCarryOperations(1, 99999) // 5
  numberOfCarryOperations(999045, 1055) // 5
  numberOfCarryOperations(101, 809) // 1
  numberOfCarryOperations(189, 209) // 1


}
