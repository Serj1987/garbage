package cryptography
import java.awt.Color
import java.io.File
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

import java.io.FileNotFoundException

fun printTask() {
    println("Task (hide, show, exit):")
}

fun hide() {
    try {
        println("Input image file:")
        val inputImageFile = readLine().toString()
        println("Output image file:")
        val outputImageFile = readln().toString()
        println("Input Image: $inputImageFile")
        println("Output Image: $outputImageFile")

        val inputFile = File(inputImageFile)
        val myImage = ImageIO.read(inputFile)

        for (x in 0 until myImage.width) {
            for (y in 0 until myImage.height) {
                val color = Color(myImage.getRGB(x, y))
                val red = color.red.toString(2)
                val gre = color.green.toString(2)
                val blu = color.blue.toString(2)

                val redNew = red.substring(0..red.length - 2) + "1"
                val greNew = gre.substring(0..gre.length - 2) + "1"
                val bluNew = blu.substring(0..blu.length - 2) + "1"

                println("${red.toInt(2)}, ${gre.toInt(2)}, ${blu.toInt(2)}")
                val setColor = Color(redNew.toInt(2), greNew.toInt(2), bluNew.toInt(2))
                myImage.setRGB(x, y, setColor.rgb)
            }
        }

        saveImage(myImage, outputImageFile)

        println("Image $outputImageFile is saved.")
    }
    catch (e: FileNotFoundException) {
        println("Can't read input file!")
    }
}

fun main() {

    do {
        printTask()
        val inp: String = readln()
        when (inp) {
            "hide" -> hide()
            "show" -> println("Obtaining message from image.")
            "exit" -> println("Bye!")
            else -> println("Wrong task: $inp")
        }
    }
    while (inp != "exit")

}

