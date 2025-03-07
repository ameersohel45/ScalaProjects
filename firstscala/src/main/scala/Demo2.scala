class Demo2(val xc:Int,val yc:Int) {
  var x:Int = xc
  var   y:Int =yc

  def move(dx:Int,dy:Int): Unit = {
    x = x+dx
    y=y+dy
    println("point x location :"+x)
    println("Point y location :"+y)
  }


}

object Demo2 {
  def main(args: Array[String ]):Unit = {
    val pt = new Demo2(10,20)
    pt.move(10,10)
  }
  
}
