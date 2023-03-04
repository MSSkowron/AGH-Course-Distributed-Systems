import java.net.InetAddress;
import java.util.Objects;

public class SocketAddress {
    private InetAddress inetAddress;
    private int port;

    public SocketAddress(InetAddress inetAddress, int port) {
        this.inetAddress = inetAddress;
        this.port = port;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public int getPort() {
        return port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        SocketAddress socketAddres = (SocketAddress) o;

        return Objects.equals(inetAddress.getHostAddress(), socketAddres.inetAddress.getHostAddress())
                && Objects.equals(port, socketAddres.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inetAddress.getHostAddress(), port);
    }

    @Override
    public String toString() {
        return inetAddress.getHostAddress() + ":" + Integer.toString(port);
    }
}