object file{

  import sys.process._

  "ls  -al".!
  val path = "pwd".!!
  path.length

//  val l = "ls -al".lines


  Seq("ls", "-a", "-l").!

  val result = Process("ls -al").lineStream_!
  val result2 = Seq("ls","-al").!!

  "ls -al".lineStream_!.toList


//  result2




}