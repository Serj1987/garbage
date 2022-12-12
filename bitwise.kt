import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.security.MessageDigest
import java.util.BitSet
import javax.imageio.ImageIO
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.experimental.xor


//fun saveImage(image: BufferedImage, imageFile: File) {
//    ImageIO.write(image, "png", imageFile)
//}
/*
 /home/sergey/IdeaProjects/Steganography and Cryptography/Steganography and Cryptography/task/test/testimage.png
*/


////////////////////////////////////////////////////////////////////
fun imageHash(inputImage: BufferedImage) : String {
    val imageByteArray = ByteArray(3 * inputImage.width * inputImage.height)
    var index = 0
    for (y in 0 until inputImage.height) {
        for (x in 0 until inputImage.width) {
            val color = Color(inputImage.getRGB(x, y))
            imageByteArray[index] = color.red.toByte()
            index++
            imageByteArray[index] = color.green.toByte()
            index++
            imageByteArray[index] = color.blue.toByte()
            index++
        }
    }
    val md = MessageDigest.getInstance("SHA-1")
    md.update(imageByteArray)
    return md.digest().joinToString("") { "%02x".format(it) }
}

////////////////////////////////////////////////////////////////////////////

fun decodeMessege() {

    val inputImageFile = "/home/sergey/IdeaProjects/Steganography and Cryptography/Steganography and Cryptography/task/test/testimage.png"//"/home/sergey/Рабочий стол/testimage.png"
    val imageFile = File(inputImageFile)
    val image: BufferedImage = ImageIO.read(imageFile)
    val newImage = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_RGB)
    val newImageFile = File("/home/sergey/Рабочий стол/testimage1.png")

    var messege = ("Hyperskill steganography program." + Integer.toBinaryString(0) + Integer.toBinaryString(0) + Integer.toBinaryString(3))
    val inputFile = File(inputImageFile)
    val myImage = ImageIO.read(inputFile)

     for (y in 0 until myImage.height) loop@for (x in 0 until myImage.width) {
        while (messege.length > 1) {
            val color = Color(myImage.getRGB(x, y))
            val red = color.red//.toString(2)
            val gre = color.green
            val bluNew = messege[0].toByte()  // toInt()
            val setColor = Color(red, gre, bluNew.toInt())
            newImage.setRGB(x, y, setColor.rgb)
            messege = if (messege.length > 1) messege.substring(1)
            else {
                messege[0].toString()
                val bl = messege[0].toByte()
                break@loop
            }
            continue@loop
        }
        break
    }

            saveImage(newImage, newImageFile)
            println("my image hash is: "+ imageHash(newImage))
            println(if (imageHash(newImage) == "c1efea2b60e889c86d38110d68589ac16610a4b1") "YEEEEEEEEE"
                else "NOo")
//        if (imageHash(newImage) != "c1efea2b60e889c86d38110d68589ac16610a4b1") {
//            println("wrong cash output image")
//        } else saveImage(newImage, newImageFile)
    }
//}

fun encodeMessege() {

    fun append(arr: Array<Byte>, element: Int): Array<Byte> {
        val list: MutableList<Byte> = arr.toMutableList()
        list.add(element.toByte())
        return list.toTypedArray()
    }

    val sourceImgFile = File("/home/sergey/Рабочий стол/testimage1.png")
    val sourceImg = ImageIO.read(sourceImgFile)
    var encodeMessege = ""
    val end_of_message = Integer.toBinaryString(0) + Integer.toBinaryString(0) + Integer.toBinaryString(3)
    var bmessege = emptyArray<Byte>()
    loop@for (y in 0 until sourceImg.height) for (x in 0 until sourceImg.width) {
            val color = Color(sourceImg.getRGB(x, y))
            val blue = color.blue
            bmessege = append(bmessege, blue)
            encodeMessege += blue.toChar().toString()
            encodeMessege += ""
            if ((encodeMessege.length > 3) && (encodeMessege.substring(encodeMessege.length - 3) == end_of_message))
                break@loop

    }
    val s = String(bmessege.toByteArray(), Charsets.UTF_8)
    println(s.substring(0, s.length - 3))
    println(imageHash(sourceImg))

}

fun main() {
    //decodeMessege()
    val encStr = "bitWise operators"
    val encBtStr = encStr.encodeToByteArray()
    val str =
        "hello" + "\u0000\u0000\u0003"//Integer.toBinaryString(0) +  Integer.toBinaryString(0) +  Integer.toBinaryString(3)
    var byteMessage = str.encodeToByteArray()
    var tmp: Byte


    for (elem in byteMessage) {
        print(elem.toString(2))
        print(": ")
        val bits =  BitSet.valueOf(byteMessage)
//        println((elem.toInt() shr (3)).toString(2))
        for (i in 8 downTo 0) {
            tmp = (elem.toInt() shr (i)).toByte()
            if (tmp and 1.inv() == tmp) print("0 ")
            else if (tmp and 1 != tmp) print("1 ")
        }
        println()
        ////////////////////////////////////////////////////////////////////////////////
//        for (ind in elem.toInt().toString(2)) if (ind == '0') print("bit = $ind ") else print("bit = $ind ")
//        println(elem.toString(2))
//        println((elem.toInt() shr 1).toString(2))
//        if ((elem and 1.inv()) == elem) println("last bit is: 0")// ${ (elem or 1).toString(2) }") // if last bit is 0 return last bit 1
//        else if (elem and 1 != elem) println("last bit is: 1")// ${ (elem and 1.inv()).toString(2) }") // if last bit is 1 return last bit 0
////////////////////////////////////////////////////////////////////////////////////////////////
    }
}
