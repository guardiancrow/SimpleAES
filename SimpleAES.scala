import javax.crypto.Cipher
import javax.crypto.spec.{SecretKeySpec, IvParameterSpec}
import java.util.Base64 //Java 8 or newer
import java.io._

import scala.io.Source

object SimpleAES {
  var enckey = "0123456789012345"
  var encIv =  "abcdefghijklmnop"
  var mode = ""
  var infilename = ""
  var outfilename = ""
  
  def encrypt(text: String): Array[Byte] = {
    val key = new SecretKeySpec(enckey.getBytes("UTF-8"), "AES")
    val iv = new IvParameterSpec(encIv.getBytes("UTF-8"))

    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    cipher.init(Cipher.ENCRYPT_MODE, key, iv)

    val result = cipher.doFinal(text.getBytes("UTF-8"))
    return result
  }

  def decrypt(bytes: Array[Byte]):String = {
    val key = new SecretKeySpec(enckey.getBytes("UTF-8"), "AES")
    val iv = new IvParameterSpec(encIv.getBytes("UTF-8"))

    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    cipher.init(Cipher.DECRYPT_MODE, key, iv)

    val result = cipher.doFinal(bytes)
    return new String(result, "UTF-8")
  }

  def encrypt_file {
    if(!new File(infilename).exists){
      println("[error]" + infilename + " not found.")
      return
    }

    println("[encrypt]")
    println("[input]" + infilename)
    println("[output]" + outfilename)

    val textfile = Source.fromFile(infilename, "UTF-8")
    var text = ""
    textfile.foreach(text += _)
    textfile.close

    val encresult = encrypt(text)
    val encbase64 = Base64.getEncoder.encode(encresult)

    val encryptfile = new PrintWriter(outfilename, "UTF-8")
    encryptfile.print(new String(encbase64, "UTF-8"))
    encryptfile.close
  }

  def decrypt_file {
    if(!new File(infilename).exists){
      println("[error]" + infilename + " not found.")
      return
    }

    println("[decrypt]")
    println("[input]" + infilename)
    println("[output]" + outfilename)

    val textfile = Source.fromFile(infilename, "UTF-8")
    var text = ""
    textfile.foreach(text += _)
    textfile.close

    val decbase64 = Base64.getDecoder.decode(text)
    val decresult = decrypt(decbase64)

    val decryptfile = new PrintWriter(outfilename, "UTF-8")
    decryptfile.print(decresult)
    decryptfile.close
  }

  def parse_switches(args: Array[String]):Boolean ={
    if(args.length < 5){
      return false
    }

    args(0) match {
      case "-e" => mode = "e"
      case "--encrypt" => mode = "e"
      case "-d" => mode = "d"
      case "--decrypt" => mode = "d"
      case _ => return false
    }

    var pass = args(1)
    if(pass.length < 16){
      enckey = pass + "0000000000000000".substring(pass.length)
    }else if(pass.length > 16){
      enckey = pass.substring(0, 16)
    }else{
      enckey = pass
    }

    var iv = args(2)
    if(iv.length < 16){
      encIv = iv + "0000000000000000".substring(iv.length)
    }else if(iv.length > 16){
      encIv = iv.substring(0, 16)
    }else{
      encIv = iv
    }

    val currentdir = new File(".").getAbsoluteFile().getParent()
    infilename = currentdir + File.separator + args(3)
    outfilename = currentdir + File.separator + args(4)

    if(!new File(infilename).exists){
      println("[warning]" + infilename + " not found.")
    }
    if(new File(outfilename).exists){
      println("[warning]" + outfilename + " is an overwrite.")
    }
    return true
  }

  def main(args: Array[String]){
    if(args.length < 5){
      println("usage ")
      println(">scala SimpleAES.scala [options] [password] [IV] [INPUTFILE] [OUTPUTFILE]")
      println("[options] -e or --encrypt : encryptmode")
      println("          -d or --decrypt : decryptmode")
      return;
    }

    if(!parse_switches(args)){
      println("invalid option ...exit")
      return
    }

    mode match {
      case "e" => encrypt_file
      case "d" => decrypt_file
    }
  }
}
