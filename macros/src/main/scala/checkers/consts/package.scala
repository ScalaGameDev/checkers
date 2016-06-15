package checkers

import language.experimental.macros
import scala.reflect.macros.blackbox

package object consts {
  type Color = Int

  def DARK: Color = macro darkImpl
  def LIGHT: Color = macro lightImpl

  type PieceType = Int

  def MAN: PieceType = macro manImpl
  def KING: PieceType = macro kingImpl

  type Occupant = Int

  def EMPTY: Occupant = macro emptyImpl
  def LIGHTMAN: Occupant  = macro lightManImpl
  def DARKMAN: Occupant  = macro darkManImpl
  def LIGHTKING: Occupant  = macro lightKingImpl
  def DARKKING: Occupant  = macro darkKingImpl

  def COLOR(occupant: Occupant): Color = macro colorImpl
  def PIECETYPE(occupant: Occupant): PieceType = macro pieceTypeImpl
  def ISMAN(occupant: Occupant): Boolean = macro isManImpl
  def ISKING(occupant: Occupant): Boolean = macro isKingImpl
  def ISPIECE(occupant: Occupant): Boolean = macro isPieceImpl
  def ISEMPTY(occupant: Occupant): Boolean = macro isEmptyImpl
  def OCCUPANTENCODE(occupant: Occupant): Int = macro occupantEncodeImpl

  def darkImpl(c: blackbox.Context): c.Expr[Color] = c.universe.reify(0)
  def lightImpl(c: blackbox.Context): c.Expr[Color] = c.universe.reify(1)

  def manImpl(c: blackbox.Context): c.Expr[PieceType] = c.universe.reify(0)
  def kingImpl(c: blackbox.Context): c.Expr[PieceType] = c.universe.reify(2)

  def emptyImpl(c: blackbox.Context): c.Expr[Occupant] = c.universe.reify(0)
  def lightManImpl(c: blackbox.Context): c.Expr[Occupant] = c.universe.reify(4)
  def darkManImpl(c: blackbox.Context): c.Expr[Occupant] = c.universe.reify(5)
  def lightKingImpl(c: blackbox.Context): c.Expr[Occupant] = c.universe.reify(6)
  def darkKingImpl(c: blackbox.Context): c.Expr[Occupant] = c.universe.reify(7)

  def colorImpl(c: blackbox.Context)(occupant: c.Expr[Occupant]): c.Expr[Color] = {
    import c.universe._
    c.Expr[Color](q"$occupant & 1")
  }

  def pieceTypeImpl(c: blackbox.Context)(occupant: c.Expr[Occupant]): c.Expr[PieceType] = {
    import c.universe._
    c.Expr[PieceType](q"$occupant & 2")
  }

  def isManImpl(c: blackbox.Context)(occupant: c.Expr[Occupant]): c.Expr[Boolean] = {
    import c.universe._
    c.Expr[Boolean](q"($occupant & 6) == 4")
  }

  def isKingImpl(c: blackbox.Context)(occupant: c.Expr[Occupant]): c.Expr[Boolean] = {
    import c.universe._
    c.Expr[Boolean](q"($occupant & 6) == 6")
  }

  def isEmptyImpl(c: blackbox.Context)(occupant: c.Expr[Occupant]): c.Expr[Boolean] = {
    import c.universe._
    c.Expr[Boolean](q"$occupant < 4")
  }

  def isPieceImpl(c: blackbox.Context)(occupant: c.Expr[Occupant]): c.Expr[Boolean] = {
    import c.universe._
    c.Expr[Boolean](q"$occupant >= 4")
  }

  def occupantEncodeImpl(c: blackbox.Context)(occupant: c.Expr[Occupant]): c.Expr[Int] = {
    import c.universe._
    occupant
  }
}