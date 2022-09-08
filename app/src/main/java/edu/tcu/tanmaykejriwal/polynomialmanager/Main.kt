package edu.tcu.tanmaykejriwal.polynomialmanager
import edu.tcu.tanmaykejriwal.polynomialmanager.PolyTable
import java.util.Scanner

//Should handle all Input
fun main(args : Array<String>) {
    val mainTable = PolyTable()
    while (true){
        val stringInput = readln()!!
        val formattedCmd = stringInput.trim().split("\\p{Space}".toRegex()).toTypedArray()
        mainTable.evaluate(formattedCmd)
    }

}