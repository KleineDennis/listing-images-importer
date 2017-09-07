val s: Seq[String] = Seq("a", "b", "c")
val a = "a"
val b = "b"

s.filter(List(a,b).contains(_))

val f: String => String = {
  case "ping" => "pong"
  case _ => ""
}
f("ping")
f("abc")


val strings = List("a", "b", "c")
val inner = "a"
val outer = "b"

def myFunction(s: String) = s.toString

strings.find(x=> x == outer) match {
  case Some(inner) => myFunction("inner")
  case Some(outer) => myFunction("outer")
  case None => myFunction("")
}



def p(s: String): String = {
  if (s == "ThreeSixtyVRImage") "inside"
  else if (s == "Car360Image") "outside"
  else ""
}

def func(p: String=>String): String = ???