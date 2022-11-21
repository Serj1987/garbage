import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.IIOException
import javax.imageio.ImageIO

fun printTask() {
    println("Task (hide, show, exit):")
}

fun hide() {
    try{
        println("Input image file:")
        val inputImageFile: String = readln()
        val imageFile = File(inputImageFile)  // open file of source picture
        val sourceImage = ImageIO.read(imageFile)

        println("Output image file:")
        val outputImageFile: String = readln()
        val newImage = BufferedImage(sourceImage.width, sourceImage.height, BufferedImage.TYPE_INT_RGB)
        val newImageFile = File(outputImageFile)  // open/create a new picture fail

        println("Input Image: $inputImageFile")
        println("Output Image: $outputImageFile")

        for (x in 0 until sourceImage.width) {
            for (y in 0 until sourceImage.height) {
                val color = Color(sourceImage.getRGB(x, y))
                val red = color.red.toString(2)  // get colors from source image and take it to binary
                val gre = color.green.toString(2)
                val blu = color.blue.toString(2)

                val redNew = red.substring(0..red.length - 2) + "1"  // newcolors to binary and cahnge the last to character "1"
                val greNew = gre.substring(0..gre.length - 2) + "1"
                val bluNew = blu.substring(0..blu.length - 2) + "1"

                val setColor = Color(redNew.toInt(2), greNew.toInt(2), bluNew.toInt(2))
                newImage.setRGB(x, y, setColor.rgb)  // set new color in newImage
            }
        }
        saveImage(newImage, newImageFile)
        println("Image $outputImageFile is saved.")

    }
    catch (e: IIOException) {
        println("Can't read input file!")
    }
}

fun saveImage(image: BufferedImage, imageFile: File) {
    ImageIO.write(image, "png", imageFile)
}

fun main(){

    do{
        printTask()
        val inp: String = readln()
        when(inp) {
            "hide" -> hide()
            "show" -> println("show another massege")
            "exit" -> println("Bye!")
            else -> println("Wrong task: $inp")
        }

    }
    while (inp != "exit")

}
