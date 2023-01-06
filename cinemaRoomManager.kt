fun printMenu() {
    println("1. Show the seats\n" +
            "2. Buy a ticket\n" +
            "3. Statistics\n" +
            "0. Exit")
}

fun main() {
    println("Enter the number of rows:")
    val rows = readln().toInt()
    println("Enter the number of seats in each row:")
    val seats = readln().toInt()
    val schem = MutableList(rows) { MutableList(seats + 1) { mutableListOf<String>("S ") } }

    var currentIncome = 0
    var usrChoice = "something"
    println()
    while (usrChoice != "0") {
        printMenu()
        usrChoice = readln().toString()
        if (usrChoice == "1") {                 //menu -- show seats
            println()
            println("Cinema:")
            print("  ")
            for (num in 1..seats) print("$num ")
            println()
            var rowCounter = 1
            for (el in schem) {
                if (el[0][0] == "S ") el[0][0] = "$rowCounter "
                rowCounter++
                for (x in el) {
                    print(x.joinToString(""))
                }
                println()
            }
            println()

        } else if (usrChoice == "2") {              //menu -- buy a ticket
            println()
            try {
                println("Enter a row number:")
                var rowNumber = readln().toInt()
                println("Enter a seat number in that row:")
                var seatNumber = readln().toInt()
                if (rowNumber > rows) {
                    println("Wrong input!\n")
                    println("Enter a row number:")
                    rowNumber = readln().toInt()
                    println("Enter a seat number in that row:")
                    seatNumber = readln().toInt()
                }
                   else if (seatNumber > seats) {
                    println("Wrong input!\n")
                    println("Enter a row number:")
                    rowNumber = readln().toInt()
                    println("Enter a seat number in that row:")
                    seatNumber = readln().toInt()
                   } else {
                while (schem[rowNumber - 1][seatNumber][0] == "B ") {
                    println()
                    println("That ticket has already been purchased!")
                    println()
                    println("Enter a row number:")
                    rowNumber = readln().toInt()
                    println("Enter a seat number in that row:")
                    seatNumber = readln().toInt()
                }
                    }
                println()
                print("Ticket price:")
                if ((seats * rows) > 60) {
                    val totInc = if (rowNumber <= rows / 2) {
                        currentIncome += 10
                        println("$10")
                    }  // if first part of rows with much price

                    else {
                        currentIncome += 8
                        println("$8")
                    }
                } else {
                    currentIncome += 10
                    println("$10")
                }
                println()
                schem[rowNumber - 1][seatNumber][0] = "B "
            } catch (e: IndexOutOfBoundsException) {
                println("Wrong input!\n")
            }

        } else if (usrChoice == "3") {                  // menu -- statistics
            var seatCounter = 0
            for (line in schem) {
                for (el in line) {
                    if ("B " in el) seatCounter += 1
                }
            }
            println()
            println("Number of purchased tickets: $seatCounter")

            val hallSize = rows * seats
            val percentage = seatCounter.toDouble() / hallSize.toDouble() * 100.0
            val formatPercentage = "%.2f".format(percentage)
            print("Percentage: $formatPercentage%\n")

            println("Current income: $$currentIncome")
            if (rows * seats <= 60) {
                print("Total income: ")
                println("$${(rows * seats ) * 10}")
                println()
            }
            else {
                val totInc = (if (rows % 2 == 0) (((rows / 2) * seats) * 10 + ((rows / 2) * seats) * 8)
                else (((rows / 2) * seats) * 10 + ((rows - rows / 2) * seats) * 8))
                print("Total income: ")
                println("$$totInc")
                println()
            }

        }
    }
}
