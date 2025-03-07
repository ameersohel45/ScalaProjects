object Singleton{
  def main(args:Array[String]): Unit = {
   new Singleton().greeyt();      // No need to create object.
  }
}


class Singleton {
  def greeyt(): Unit = {
    println("hii ")
  }
}
