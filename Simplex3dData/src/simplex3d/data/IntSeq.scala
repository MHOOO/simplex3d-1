/*
 * Simplex3d, CoreBuffer module
 * Copyright (C) 2010, Simplex3d Team
 *
 * This file is part of Simplex3dBuffer.
 *
 * Simplex3dBuffer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Simplex3dBuffer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package simplex3d.buffer

import java.nio._
import scala.reflect._
import scala.annotation.unchecked._
import simplex3d.buffer.Util._


/**
 * @author Aleksey Nikiforov (lex)
 */
private[buffer] sealed abstract class BaseSInt[+R <: DefinedInt](
  shared: AnyRef, primitive: AnyRef, ro: Boolean,
  off: Int, str: Int
)
extends BaseSeq[SInt, Int, Int, R](shared, primitive, ro, off, str)
with CompositionFactory[SInt, DefinedInt]
{
  final def elemManifest = MetaManifest.SInt
  final def readManifest = Manifest.Int
  final def components: Int = 1
  final def normalized = false

  final def mkReadDataArray[P <: DefinedInt](primitive: ReadDataArray[SInt, P])
  :ReadDataArray[SInt, P] = primitive
  final def mkReadDataBuffer[P <: DefinedInt](primitive: ReadDataBuffer[SInt, P])
  :ReadDataBuffer[SInt, P] = primitive
  protected final def mkReadDataViewInstance[P <: DefinedInt](
    primitive: ReadDataBuffer[SInt, P], off: Int, str: Int
  ) :ReadDataView[SInt, P] = new ViewSInt(primitive, off, str)

  protected final def mkReadDataViewInstance(
    byteBuffer: ByteBuffer, off: Int, str: Int
  ) :ReadDataView[SInt, R] = {
    new ViewSInt(backing.mkReadDataBuffer(byteBuffer), off, str)
  }
  
  final override def mkSerializableInstance() = new PrimitiveSInt(rawType)
}

private[buffer] final class ViewSInt[+R <: DefinedInt](
  primitive: ReadDataBuffer[SInt, R], off: Int, str: Int
) extends BaseSInt[R](primitive, primitive, primitive.readOnly, off, str) with DataView[SInt, R] {
  final def rawType = backing.rawType
  def mkReadOnlyInstance() = new ViewSInt(backing.asReadOnly(), offset, stride)

  def apply(i: Int) :Int = backing(offset + i*stride)
  def update(i: Int, v: Int) :Unit = backing(offset + i*stride) = v

  final def mkDataArray(array: R#Array @uncheckedVariance) :DataArray[SInt, R] =
    backing.mkDataArray(array)
  final def mkReadDataBuffer(byteBuffer: ByteBuffer) :ReadDataBuffer[SInt, R] =
    backing.mkReadDataBuffer(byteBuffer)
}


// Type: SByte
private[buffer] final class ArraySIntSByte(
  rarray: Array[Byte], warray: Array[Byte]
)
extends BaseSInt[SByte](rarray, null, warray == null, 0, 1) with DataArray[SInt, SByte]
with PrimitiveFactory[SInt, SByte]
{
  def this() = this(emptyByte, emptyByte)
  def mkReadOnlyInstance() = new ArraySIntSByte(rarray, null)
  def rawType = RawType.SByte

  def mkDataArray(array: Array[Byte]) =
    new ArraySIntSByte(array, array)
  def mkReadDataBuffer(byteBuffer: ByteBuffer) = {
    new BufferSIntSByte(byteBuffer, byteBuffer.isReadOnly)
  }

  def apply(i: Int) :Int = rarray(i)
  def update(i: Int, v: Int) :Unit = warray(i) = v.toByte
}

private[buffer] final class BufferSIntSByte(
  shared: ByteBuffer, ro: Boolean
) extends BaseSInt[SByte](shared, null, ro, 0, 1) with DataBuffer[SInt, SByte] {
  def mkReadOnlyInstance() = new BufferSIntSByte(shared, true)
  def rawType = RawType.SByte

  def mkDataArray(array: Array[Byte]) =
    new ArraySIntSByte(array, array)
  def mkReadDataBuffer(byteBuffer: ByteBuffer) = {
    new BufferSIntSByte(byteBuffer, byteBuffer.isReadOnly)
  }

  def apply(i: Int) :Int = buff.get(i)
  def update(i: Int, v: Int) :Unit = buff.put(i, v.toByte)
}


// Type: UByte
private[buffer] final class ArraySIntUByte(
  rarray: Array[Byte], warray: Array[Byte]
)
extends BaseSInt[UByte](rarray, null, warray == null, 0, 1) with IndexArray[UByte]
with PrimitiveFactory[SInt, UByte]
{
  def this() = this(emptyByte, emptyByte)
  def mkReadOnlyInstance() = new ArraySIntUByte(rarray, null)
  def rawType = RawType.UByte

  def mkDataArray(array: Array[Byte]) =
    new ArraySIntUByte(array, array)
  def mkReadDataBuffer(byteBuffer: ByteBuffer) = {
    new BufferSIntUByte(byteBuffer, byteBuffer.isReadOnly)
  }

  def apply(i: Int) :Int = rarray(i) & 0xFF
  def update(i: Int, v: Int) :Unit = warray(i) = v.toByte
}

private[buffer] final class BufferSIntUByte(
  shared: ByteBuffer, ro: Boolean
) extends BaseSInt[UByte](shared, null, ro, 0, 1) with IndexBuffer[UByte] {
  def mkReadOnlyInstance() = new BufferSIntUByte(shared, true)
  def rawType = RawType.UByte

  def mkDataArray(array: Array[Byte]) =
    new ArraySIntUByte(array, array)
  def mkReadDataBuffer(byteBuffer: ByteBuffer) = {
    new BufferSIntUByte(byteBuffer, byteBuffer.isReadOnly)
  }

  def apply(i: Int) :Int = buff.get(i) & 0xFF
  def update(i: Int, v: Int) :Unit = buff.put(i, v.toByte)
}


// Type: SShort
private[buffer] final class ArraySIntSShort(
  rarray: Array[Short], warray: Array[Short]
)
extends BaseSInt[SShort](rarray, null, warray == null, 0, 1) with DataArray[SInt, SShort]
with PrimitiveFactory[SInt, SShort]
{
  def this() = this(emptyShort, emptyShort)
  def mkReadOnlyInstance() = new ArraySIntSShort(rarray, null)
  def rawType = RawType.SShort

  def mkDataArray(array: Array[Short]) =
    new ArraySIntSShort(array, array)
  def mkReadDataBuffer(byteBuffer: ByteBuffer) = {
    new BufferSIntSShort(byteBuffer, byteBuffer.isReadOnly)
  }

  def apply(i: Int) :Int = rarray(i)
  def update(i: Int, v: Int) :Unit = warray(i) = v.toShort
}

private[buffer] final class BufferSIntSShort(
  shared: ByteBuffer, ro: Boolean
) extends BaseSInt[SShort](shared, null, ro, 0, 1) with DataBuffer[SInt, SShort] {
  def mkReadOnlyInstance() = new BufferSIntSShort(shared, true)
  def rawType = RawType.SShort

  def mkDataArray(array: Array[Short]) =
    new ArraySIntSShort(array, array)
  def mkReadDataBuffer(byteBuffer: ByteBuffer) = {
    new BufferSIntSShort(byteBuffer, byteBuffer.isReadOnly)
  }

  def apply(i: Int) :Int = buff.get(i)
  def update(i: Int, v: Int) :Unit = buff.put(i, v.toShort)
}


// Type: UShort
private[buffer] final class ArraySIntUShort(
  rarray: Array[Char], warray: Array[Char]
)
extends BaseSInt[UShort](rarray, null, warray == null, 0, 1) with IndexArray[UShort]
with PrimitiveFactory[SInt, UShort]
{
  def this() = this(emptyChar, emptyChar)
  def mkReadOnlyInstance() = new ArraySIntUShort(rarray, null)
  def rawType = RawType.UShort

  def mkDataArray(array: Array[Char]) =
    new ArraySIntUShort(array, array)
  def mkReadDataBuffer(byteBuffer: ByteBuffer) = {
    new BufferSIntUShort(byteBuffer, byteBuffer.isReadOnly)
  }

  def apply(i: Int) :Int = rarray(i)
  def update(i: Int, v: Int) :Unit = warray(i) = v.toChar
}

private[buffer] final class BufferSIntUShort(
  shared: ByteBuffer, ro: Boolean
) extends BaseSInt[UShort](shared, null, ro, 0, 1) with IndexBuffer[UShort] {
  def mkReadOnlyInstance() = new BufferSIntUShort(shared, true)
  def rawType = RawType.UShort

  def mkDataArray(array: Array[Char]) =
    new ArraySIntUShort(array, array)
  def mkReadDataBuffer(byteBuffer: ByteBuffer) = {
    new BufferSIntUShort(byteBuffer, byteBuffer.isReadOnly)
  }

  def apply(i: Int) :Int = buff.get(i)
  def update(i: Int, v: Int) :Unit = buff.put(i, v.toChar)
}


// Type: SInt
private[buffer] final class ArraySIntSInt(
  rarray: Array[Int], warray: Array[Int]
) extends BaseSInt[SInt](rarray, null, warray == null, 0, 1) with DataArray[SInt, SInt]
with PrimitiveFactory[SInt, SInt]
{
  def this() = this(emptyInt, emptyInt)
  def mkReadOnlyInstance() = new ArraySIntSInt(rarray, null)
  def rawType = RawType.SInt

  def mkDataArray(array: Array[Int]) =
    new ArraySIntSInt(array, array)
  def mkReadDataBuffer(byteBuffer: ByteBuffer) = {
    new BufferSIntSInt(byteBuffer, byteBuffer.isReadOnly)
  }

  def apply(i: Int) :Int = rarray(i)
  def update(i: Int, v: Int) :Unit = warray(i) = v
}

private[buffer] final class BufferSIntSInt(
  shared: ByteBuffer, ro: Boolean
) extends BaseSInt[SInt](shared, null, ro, 0, 1) with DataBuffer[SInt, SInt]{
  def mkReadOnlyInstance() = new BufferSIntSInt(shared, true)
  def rawType = RawType.SInt

  def mkDataArray(array: Array[Int]) =
    new ArraySIntSInt(array, array)
  def mkReadDataBuffer(byteBuffer: ByteBuffer) = {
    new BufferSIntSInt(byteBuffer, byteBuffer.isReadOnly)
  }

  def apply(i: Int) :Int = buff.get(i)
  def update(i: Int, v: Int) :Unit = buff.put(i, v)
}


// Type: UInt
private[buffer] final class ArraySIntUInt(
  rarray: Array[Int], warray: Array[Int]
)
extends BaseSInt[UInt](rarray, null, warray == null, 0, 1) with IndexArray[UInt]
with PrimitiveFactory[SInt, UInt]
{
  def this() = this(emptyInt, emptyInt)
  def mkReadOnlyInstance() = new ArraySIntUInt(rarray, null)
  def rawType = RawType.UInt

  def mkDataArray(array: Array[Int]) =
    new ArraySIntUInt(array, array)
  def mkReadDataBuffer(byteBuffer: ByteBuffer) = {
    new BufferSIntUInt(byteBuffer, byteBuffer.isReadOnly)
  }

  def apply(i: Int) :Int = rarray(i)
  def update(i: Int, v: Int) :Unit = warray(i) = v
}

private[buffer] final class BufferSIntUInt(
  shared: ByteBuffer, ro: Boolean
) extends BaseSInt[UInt](shared, null, ro, 0, 1) with IndexBuffer[UInt]{
  def mkReadOnlyInstance() = new BufferSIntUInt(shared, true)
  def rawType = RawType.UInt

  def mkDataArray(array: Array[Int]) =
    new ArraySIntUInt(array, array)
  def mkReadDataBuffer(byteBuffer: ByteBuffer) = {
    new BufferSIntUInt(byteBuffer, byteBuffer.isReadOnly)
  }

  def apply(i: Int) :Int = buff.get(i)
  def update(i: Int, v: Int) :Unit = buff.put(i, v)
}