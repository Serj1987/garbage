import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.IIOException
import javax.imageio.ImageIO

fun printTask() {
    println("Task (hide, show, exit):")
}

fun saveImage(image: BufferedImage, imageFile: File) {
    ImageIO.write(image, "png", imageFile)
}

fun hide() {
//"/home/sergey/Рабочий стол/square.png"
//    "/home/sergey/Рабочий стол/square1.png")
    try{
        println("Input image file:")
        val inputImageFile: String = readln()
        val imageFile = File(inputImageFile)  // open file of source picture
        val sourceImage = ImageIO.read(imageFile)

        println("Output image file:")
        val outputImageFile: String = readln()
        val newImage = BufferedImage(sourceImage.width, sourceImage.height, BufferedImage.TYPE_INT_RGB)
        val newImageFile = File(outputImageFile)  // open/create a new picture file

        println("Message to hide:")
        var message = (readln() + "0"+"0"+"3"+"3")
        val inputFile = File(inputImageFile)
        val myImage = ImageIO.read(inputFile)

        if (message.length > sourceImage.width * sourceImage.height) {
            println("The input image is not large enough to hold this message.")
        } else {

            for (x in 0 until sourceImage.width)
                loop@ for (y in 0 until sourceImage.height) {

                    val color = Color(myImage.getRGB(x, y))
                    val red = color.red
                    val gre = color.green
                    while (message.length > 1) {
                        val bluNew = message[0].toInt()
                        val setColor = Color(red, gre, bluNew)
                        newImage.setRGB(x, y, setColor.rgb)
                        message = if (message.length > 1) message.substring(1)
                        else {
                            message[0].toString()
                            break@loop
                        }
                        continue@loop
                    }
                    val blue = color.blue
                    val setColor = Color(red, gre, blue)
                    newImage.setRGB(x, y, setColor.rgb)
                    //break
                }

            saveImage(newImage, newImageFile)
            println("Message saved in $outputImageFile image.")
        }
    }
    catch (e: IIOException) {
        println("Can't read input file!")
    }
}

fun show() {
    try{
        println("Input image file:")
        val inputImageFile: String = readln()
        val imageFile = File(inputImageFile)  // open file of source picture
        val sourceImg = ImageIO.read(imageFile)

        fun append(arr: Array<Byte>, element: Int): Array<Byte> {
            val list: MutableList<Byte> = arr.toMutableList()
            list.add(element.toByte())
            return list.toTypedArray()
        }

        var encodeMessage = ""
        var bmessage = emptyArray<Byte>()
        loop@for (x in 0 until sourceImg.width) for (y in 0 until sourceImg.height) {
            val color = Color(sourceImg.getRGB(x, y))
            val blue = color.blue
            bmessage = append(bmessage, blue)
            encodeMessage += blue.toChar().toString()
            if ((encodeMessage.length > 3) && (encodeMessage.substring(encodeMessage.length - 3) == "003"))
                break@loop
        }

        val s = String(bmessage.toByteArray(), Charsets.UTF_8)
        println("Message:")
        println(s.substring(0, s.length - 3))
    }
    catch (e: IIOException) {
        println("Can't read input file!")
    }
}

fun main(){

    do{
        printTask()
        val inp: String = readln()
        when(inp) {
            "hide" -> hide()
            "show" -> show()
            "exit" -> println("Bye!")
            else -> println("Wrong task: $inp")
        }
    }
    while (inp != "exit")
}
