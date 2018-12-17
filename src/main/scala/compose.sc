

def compose[A](optF: Option[A => A], optG: Option[A => A]): Option[A => A] =
  optF.fold(optG)(f => optG.fold(Some(f))(g => Some(f andThen g)))
}


def funA(i: Int) = i + 1
def funB(i: Int) = i * 3


compose(Some(funA _), Some(funB _))
