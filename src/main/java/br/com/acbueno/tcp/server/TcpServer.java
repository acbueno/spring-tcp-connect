package br.com.acbueno.tcp.server;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TcpServer {

  @Value("${tcp.server.port}")
  private int port;

  public void start() throws Exception {
    SocketAcceptor acceptor = new NioSocketAcceptor();
    acceptor.getFilterChain().addLast("codec",
        new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
    acceptor.setHandler(new IoHandlerAdapter() {
      public void messageReceived(IoSession session, Object message) {
        String msg = message.toString();
        System.out.println("Received message: " + msg);
        session.write("Echo " + msg);
      }
    });
    acceptor.bind(new InetSocketAddress(port));
    System.out.println("TCP server started on port " + port);
  }

}
