package frins

import java.util.concurrent.atomic.AtomicReference

class Atom[T](private var state: AtomicReference[T]) {

  def get = state.get()

  def swap(f: T => T): T = {
    val v = state.get()
    val newv = f(v)
    if (state.compareAndSet(v, newv)) newv else swap(f)
  }

  def reset(nv: T) = state.set(nv)

  // ----

  override def toString() = get.toString
  override def equals(that: Any) = that match {
    case that: Atom[T]  => get == that.get
    case _              => false
  }
}

object Atom {
  def apply[T](state: T) = new Atom[T](new AtomicReference[T](state))
  def apply[T]() = new Atom[T](new AtomicReference[T]())
}