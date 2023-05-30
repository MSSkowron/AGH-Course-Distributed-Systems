//
// Copyright (c) ZeroC, Inc. All rights reserved.
//
//
// Ice version 3.7.9
//
// <auto-generated>
//
// Generated from file `calculator.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package Demo;

public class A implements java.lang.Cloneable,
                          java.io.Serializable
{
    public short a;

    public long b;

    public float c;

    public String d;

    public A()
    {
        this.d = "";
    }

    public A(short a, long b, float c, String d)
    {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public boolean equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        A r = null;
        if(rhs instanceof A)
        {
            r = (A)rhs;
        }

        if(r != null)
        {
            if(this.a != r.a)
            {
                return false;
            }
            if(this.b != r.b)
            {
                return false;
            }
            if(this.c != r.c)
            {
                return false;
            }
            if(this.d != r.d)
            {
                if(this.d == null || r.d == null || !this.d.equals(r.d))
                {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    public int hashCode()
    {
        int h_ = 5381;
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, "::Demo::A");
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, a);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, b);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, c);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, d);
        return h_;
    }

    public A clone()
    {
        A c = null;
        try
        {
            c = (A)super.clone();
        }
        catch(CloneNotSupportedException ex)
        {
            assert false; // impossible
        }
        return c;
    }

    public void ice_writeMembers(com.zeroc.Ice.OutputStream ostr)
    {
        ostr.writeShort(this.a);
        ostr.writeLong(this.b);
        ostr.writeFloat(this.c);
        ostr.writeString(this.d);
    }

    public void ice_readMembers(com.zeroc.Ice.InputStream istr)
    {
        this.a = istr.readShort();
        this.b = istr.readLong();
        this.c = istr.readFloat();
        this.d = istr.readString();
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, A v)
    {
        if(v == null)
        {
            _nullMarshalValue.ice_writeMembers(ostr);
        }
        else
        {
            v.ice_writeMembers(ostr);
        }
    }

    static public A ice_read(com.zeroc.Ice.InputStream istr)
    {
        A v = new A();
        v.ice_readMembers(istr);
        return v;
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, java.util.Optional<A> v)
    {
        if(v != null && v.isPresent())
        {
            ice_write(ostr, tag, v.get());
        }
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, A v)
    {
        if(ostr.writeOptional(tag, com.zeroc.Ice.OptionalFormat.FSize))
        {
            int pos = ostr.startSize();
            ice_write(ostr, v);
            ostr.endSize(pos);
        }
    }

    static public java.util.Optional<A> ice_read(com.zeroc.Ice.InputStream istr, int tag)
    {
        if(istr.readOptional(tag, com.zeroc.Ice.OptionalFormat.FSize))
        {
            istr.skip(4);
            return java.util.Optional.of(A.ice_read(istr));
        }
        else
        {
            return java.util.Optional.empty();
        }
    }

    private static final A _nullMarshalValue = new A();

    /** @hidden */
    public static final long serialVersionUID = 626014925L;
}
