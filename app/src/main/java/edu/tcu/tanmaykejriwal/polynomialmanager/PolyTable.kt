package edu.tcu.tanmaykejriwal.polynomialmanager
import edu.tcu.tanmaykejriwal.polynomialmanager.Polynomial
import edu.tcu.tanmaykejriwal.polynomialmanager.PolyTerm
import kotlin.math.abs
//implement all the operations

class PolyTable(){
    var polynomialList = mutableListOf<Polynomial>()

    //check what opration is requested by user
    fun evaluate(cmd:Array<String>){
        val typeofOperation = cmd.get(0)
        if (typeofOperation=="QUIT"){
            quit()
        }

        if(typeofOperation=="INSERT"){
            insert(cmd.sliceArray(1..cmd.size-1))
        }
        if(typeofOperation=="DELETE"){
            var toBeDeleted=cmd.get(1)
            var retval = delete(toBeDeleted)
            if(retval==-1){
                println("POLYNOMIAL "+ toBeDeleted + " DOES NOT EXIST")
            }
            else{
                println("POLYNOMIAL " + toBeDeleted + " SUCCESSFULLY DELETED")
            }
        }
        if(typeofOperation=="SEARCH"){
            var toBeSearched=cmd.get(1)
            var retval = search(toBeSearched)
            if (retval==-1){
                println("POLYNOMIAL "+ toBeSearched + " DOES NOT EXIST")
            }
            else{
                printPolynomial(polynomialList[retval])
            }



        }
        if(typeofOperation=="UPDATE"){
            var toBeUpdated=cmd.get(1)
            var retval = update(toBeUpdated,cmd.sliceArray(1..cmd.size-1))
            if(retval==-1){
                println("POLYNOMIAL "+ toBeUpdated + " DOES NOT EXIST")
            }
        }

        if(typeofOperation=="ADD"){
            add(cmd.get(1),cmd.get(2),cmd.get(3))
        }
        if(typeofOperation=="SUB"){
            sub(cmd.get(1),cmd.get(2),cmd.get(3))
        }
        if(typeofOperation=="MULT"){
            multi(cmd.get(1),cmd.get(2),cmd.get(3))
        }
    }

    fun insert(polyArray: Array<String>){
        val name = polyArray.get(0)

        //check if the polynomial with same name exists
        for (elem in polynomialList){
            if (elem.name == name){
                println("POLYNOMIAL "+ name + " ALREADY INSERTED")
                return
            }
        }
        //check if name is in polynomial List -> Print out polynomial already exists
        val coeffAndPowers=arrFormatter(polyArray.sliceArray(1..polyArray.size-1))
        //create polyterms and store them in a Mutable List
        var listofPolyTerm=mutableListOf<PolyTerm>()
        //run a for loop
        for (index in 0..coeffAndPowers.size-4 step 4){
            var polyTerm = PolyTerm(coeffAndPowers.get(index),coeffAndPowers.get(index+1),coeffAndPowers.get(index+2),coeffAndPowers.get(index+3))
            listofPolyTerm+=polyTerm
        }
        var currPolynomial=Polynomial(name,listofPolyTerm)
        printPolynomial(currPolynomial)
        //Adding to the Polynomial table
        polynomialList+=currPolynomial
    }

    fun delete(name: String):Int{
        for (elem in polynomialList){
            if(elem.name==name) {
                polynomialList.remove(elem)
                return 1
            }
        }
        return -1

    }

    fun update(name: String,polyArray: Array<String>):Int{
        for (elem in polynomialList){
            if(elem.name==name){
                delete(name)
                insert(polyArray)
                return 1
            }
        }
        return -1
    }
    fun search(name: String):Int{
        var ctr = 0
        for (elem in polynomialList){
            if(elem.name==name){
                return ctr
            }
            ctr+=1
        }
        return -1

    }
    fun quit(){
        System.exit(-1)
    }

    fun add(name: String, p1:String,p2:String){
        var listofPolyTerm=mutableListOf<PolyTerm>()
        var p1idx = search(p1)
        var p2idx = search(p2)
        if(p1idx==-1 || p2idx == -1){
            println("INVALID OPERAND POLYNOMIALS")
            return
        }
        var poly1 = polynomialList.get(p1idx)
        var poly2 = polynomialList.get(p2idx)
        for (elem in poly1.list){
            listofPolyTerm+=elem
        }
        for(elem in poly2.list){
            listofPolyTerm+=elem
        }
        var reducedList =reduction(listofPolyTerm)
        var finalPolynomial = Polynomial(name,reducedList)
        if(search(name)==-1){
            polynomialList+=finalPolynomial
        }
        else{
            delete(name)
            polynomialList+=finalPolynomial
        }
        printPolynomial(finalPolynomial)

    }

    fun sub(name: String, p1:String,p2:String){
        var listofPolyTerm=mutableListOf<PolyTerm>()
        var p1idx = search(p1)
        var p2idx = search(p2)
        if(p1idx==-1 || p2idx == -1){
            println("INVALID OPERAND POLYNOMIALS")
            return
        }
        var poly1 = polynomialList.get(p1idx)
        var poly2 = polynomialList.get(p2idx)
        for (elem in poly1.list){
            listofPolyTerm+=elem
        }
        for(elem in poly2.list){
            var temp = PolyTerm(-1*elem.coeff,elem.xpow,elem.ypow,elem.zpow)
            listofPolyTerm+=temp
        }
        var reducedList =reduction(listofPolyTerm)
        var finalPolynomial = Polynomial(name,reducedList)
        if(search(name)==-1){
            polynomialList+=finalPolynomial
        }
        else{
            delete(name)
            polynomialList+=finalPolynomial
        }
        printPolynomial(finalPolynomial)

    }

    private fun multi(name: String, p1:String,p2:String){
        var listofPolyTerm=mutableListOf<PolyTerm>()
        var p1idx = search(p1)
        var p2idx = search(p2)
        if(p1idx==-1 || p2idx == -1){
            println("INVALID OPERAND POLYNOMIALS")
            return
        }
        var poly1 = polynomialList.get(p1idx)
        var poly2 = polynomialList.get(p2idx)
        for (elem in poly1.list){
            for(secElem in poly2.list){
                var temp = PolyTerm(elem.coeff*secElem.coeff,elem.xpow+secElem.xpow,elem.ypow+secElem.ypow,elem.zpow+secElem.zpow)
                listofPolyTerm+=temp
            }
        }
        var reducedList =reduction(listofPolyTerm)
        var finalPolynomial = Polynomial(name,reducedList)
        if(search(name)==-1){
            polynomialList+=finalPolynomial
        }
        else{
            delete(name)
            polynomialList+=finalPolynomial
        }
        printPolynomial(finalPolynomial)
    }
    //helper functions to debug

    private fun reduction(polyList: MutableList<PolyTerm>):MutableList<PolyTerm>{
        var reducedlist = mutableListOf<PolyTerm>()
        var i = 0
        while (i<polyList.size){
            var poly1 = polyList[i]
            var j = i+1
            var flag = true
            while(j<polyList.size){
                var poly2 = polyList[j]
                var c1 = poly1.coeff
                var c2 = poly2.coeff
                poly1.coeff = 1
                poly2.coeff = 1
                if(poly1==poly2){
                    var newterm = PolyTerm(c1+c2,poly1.xpow,poly1.ypow,poly2.zpow)
                    if (newterm.coeff !=0){
                        reducedlist+=newterm
                    }
                    flag = false
                    polyList.removeAt(j)
                }
                poly1.coeff = c1
                poly2.coeff = c2
                j+=1
            }
            if(flag==true){
                reducedlist+=poly1
            }
            i+=1
        }
        return reducedlist;
    }

    private fun printArrStrings(arr:Array<String>){
        for (elem in arr){
            println(elem)
        }
    }

    private fun printPolynomial(polynomial: Polynomial){
        print(polynomial.name+ " = ")
        if (polynomial.list.size==0){
            println("0")
            return
        }
        for(index in 0..polynomial.list.size-1){
            var elem = polynomial.list.get(index)
            if(index !=0){
                var temp = elem.coeff
                if(temp<0){
                    print(" - ")
                }
                else{
                    print(" + ")

                }
            }
            else{
                var temp = elem.coeff
                if(temp<0){
                    print("- ")
                }
            }
            printPolyTerm(elem)
        }
        println()
    }

    private fun printPolyTerm(polyTerm: PolyTerm){
        var coeff = ""
        var xpow = ""
        var ypow = ""
        var zpow =""
        var ans = ""
        if(polyTerm.coeff != 1 && polyTerm.coeff != -1){
            coeff+=abs(polyTerm.coeff).toString()
            ans+=coeff
        }
        else{
            // handling when polyterm is 1,0,0,0
            if(polyTerm.xpow==0 && polyTerm.ypow==0 && polyTerm.zpow==0){
                coeff+=abs(polyTerm.coeff).toString()
                ans+=coeff
            }
        }
        if(polyTerm.xpow != 0){
            if(polyTerm.xpow !=1){
                xpow+="^"+polyTerm.xpow.toString()
            }
            ans+="(x"+xpow+")"
        }
        if(polyTerm.ypow != 0){
            if(polyTerm.ypow !=1){
                ypow+="^"+polyTerm.ypow.toString()
            }
            ans+="(y"+ypow+")"
        }
        if(polyTerm.zpow != 0){
            if(polyTerm.zpow !=1){
                zpow+="^"+polyTerm.zpow.toString()
            }
            ans+="(z"+zpow+")"
        }
        print(ans)
    }



    private fun printArrInt(arr:Array<Int>){
        for (elem in arr){
            println(elem)
        }
    }

    private fun arrFormatter(arr:Array<String>):Array<Int>{
        var numbers = arrayOf<Int>()
        for (elem in arr){
            //splitting numbers by commas
            val temp = elem.split(",")
            //getting strings to numbers
            val num = temp.map { it.toInt() }
            for(k in num){
                numbers+=k
            }

        }
        return numbers
    }

}